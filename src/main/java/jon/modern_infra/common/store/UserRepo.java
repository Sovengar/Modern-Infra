package jon.modern_infra.common.store;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

import static java.lang.String.format;

public interface UserRepo extends CrudRepository<User, UUID> {
    default User findByIdOrElseThrow(UUID id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException(format("User [%s] not found", id)));
    }

    default UUID registerUser(User user) {
        var savedUser = save(user);
        return savedUser.id();
    }
}
