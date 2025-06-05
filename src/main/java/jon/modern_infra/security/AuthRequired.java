package jon.modern_infra.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authRequired")
@RequiredArgsConstructor
class AuthRequiredController {
    private final AuthRequired authRequired;

    @RequestMapping
    public void handle() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        authRequired.handle("123456789", "Reason");
    }
}

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthRequired {

    @Transactional
    public void handle(final String accountNumber, final String reason) {
        log.info("BEGIN DeleteAccount");


        log.info("END DeleteAccount");
    }
}