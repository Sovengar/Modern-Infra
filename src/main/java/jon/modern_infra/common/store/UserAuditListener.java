package jon.modern_infra.common.store;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class UserAuditListener {

    @Value("${spring.application.name}")
    private String appUser;

    @PrePersist
    public void setCreatedBy(AuditingColumns auditingColumns) {
        final var currentUser = getCurrentUser();
        final var userCreatingEntity = auditingColumns.getCreatedBy();
        final var finalUser = StringUtils.hasText(userCreatingEntity) ? userCreatingEntity : currentUser;

        auditingColumns.setCreatedAt(LocalDateTime.now());
        auditingColumns.setCreatedBy(finalUser);
    }

    @PreUpdate
    public void setUpdatedBy(AuditingColumns auditingColumns) {
        String currentUser = getCurrentUser();
        auditingColumns.setModifiedBy(currentUser);
        auditingColumns.setModifiedAt(LocalDateTime.now());
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //For cron operations
        if (isNotAuthenticated(authentication)) {
            return appUser;
        }

        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            var identifier = userDetails.getUsername(); //Username should be the UUID or the ID.
            var uuidSize = 36;

            if (identifier.length() == uuidSize) {
                return identifier;
            } else {
                for (GrantedAuthority authority : userDetails.getAuthorities()) {
                    if (authority.getAuthority().startsWith("UUID:")) {
                        return authority.getAuthority().substring(5);
                    }
                }
            }
        }

        return authentication.getName();
    }

    private boolean isNotAuthenticated(final Authentication authentication) {
        return authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser");
    }
}