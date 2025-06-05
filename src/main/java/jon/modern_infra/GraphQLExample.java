package jon.modern_infra;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jon.modern_infra.common.store.User;
import jon.modern_infra.common.store.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.Serial;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static jon.modern_infra.help.TraceIdGenerator.generateTraceId;

@Controller
@RequiredArgsConstructor
@Slf4j
class GraphQLExample {
    private final RegisterUser registerUser;
    private final UserRepo userRepo;

    @Observed(name = "registerUser")
    @Operation(summary = "Register a new user")
    @MutationMapping
    public ResponseEntity<User> registerUser(@Argument RegisterUser.Command message) {
        generateTraceId();

        log.info("BEGIN registerUser for userId: {}", message.id());
        var userId = registerUser.handle(message);
        var user = userRepo.findByIdOrElseThrow(userId);
        log.info("END registerUser for userId: {}", message.id());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userId).toUri();
        return ResponseEntity.created(location).body(user);
    }

    @QueryMapping
    public User userById(@Argument String id) {
        return userRepo.findByIdOrElseThrow(UUID.fromString(id));
    }

    @QueryMapping
    public Iterable<User> users() {
        return userRepo.findAll();
    }
}

@Slf4j
@RequiredArgsConstructor
@Service
@Validated
@Transactional
class RegisterUser {
    private final UserRepo repository;

    public UUID handle(final @Valid RegisterUser.Command command) {
        log.info("BEGIN RegisterUser");

        repository.findById(command.id()).ifPresent(user -> {
            throw new UserAlreadyExistsException(format("User [%s] already exists", command.id()));
        });

        //Complex logic to decide the user
        var user = User.Factory.register(
                command.id(),
                User.RealName.of(command.realname().orElse("")),
                User.UserName.of(command.username()),
                User.Email.of(command.email()),
                User.Password.of(command.password()),
                User.PhoneNumbers.of(command.phoneNumbers())
        );
        repository.registerUser(user);

        log.info("END RegisterUser");
        return user.id();
    }

    private static class UserAlreadyExistsException extends RuntimeException {
        @Serial private static final long serialVersionUID = 1604523616703390261L;

        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public record Command(UUID id, Optional<String> realname, String username, String email, String password, List<String> phoneNumbers) {
    }
}