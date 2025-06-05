package jon.modern_infra.common.store;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "roles", schema = "mi")
public record Role(@Id String role_code, String description) {
}
