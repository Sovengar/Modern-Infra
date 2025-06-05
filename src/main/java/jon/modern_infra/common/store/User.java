package jon.modern_infra.common.store;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table(value = "users", schema = "mi")
public record User(
        @Id @Column("user_id") UUID id,
        @Embedded.Empty RealName realname,
        @Embedded.Empty User.UserName username,
        @Embedded.Empty User.Email email,
        @Embedded.Empty User.Password password,
        @Embedded.Empty User.PhoneNumbers phoneNumbers,
        String roleCode,
        Integer version,
        String createdBy,
        java.time.LocalDateTime createdAt,
        String modifiedBy,
        java.time.LocalDateTime modifiedAt,
        Boolean deleted
) {
    public record RealName(String realname) {
        public static User.RealName of(String realname) {
            return new User.RealName(realname);
        }
    }

    public record UserName(String username) {
        public static User.UserName of(String username) {
            return new User.UserName(username);
        }
    }

    public record Email(String email) {
        public static User.Email of(String email) {
            return new User.Email(email);
        }
    }

    public record Password(String password) {
        public static User.Password of(String password) {
            return new User.Password(password);
        }
    }

    public record PhoneNumbers(List<String> phoneNumbers) {
        public static User.PhoneNumbers of(List<String> phoneNumbers) {
            return new User.PhoneNumbers(phoneNumbers);
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Factory {
        public static User register(UUID id, User.RealName realName, User.UserName userName, User.Email email, User.Password pw, User.PhoneNumbers phoneNumbers) {
            return new User(id, realName, userName, email, pw, phoneNumbers, null, 0, "Me", LocalDateTime.now(), null, null, false);
        }
    }
}
