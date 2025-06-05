package jon.modern_infra.common.store;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EntityListeners(UserAuditListener.class) //AuditingEntityListener.class from the springframework
public abstract class AuditingColumns {
    //@CreatedBy
    @Column(updatable = false)
    protected String createdBy;

    //@CreatedDate
    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    //@LastModifiedBy
    @Column(insertable = false)
    protected String modifiedBy;

    //@LastModifiedDate
    @UpdateTimestamp
    @Column(insertable = false)
    protected LocalDateTime modifiedAt;
}
