package co.zonetechpark.booktest.booktest.jpa.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "ROLES")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Role implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    @NonNull
    private String name;

    @Column(name = "status")
    private Boolean status;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String modifiedBy;

    @Column(name = "date_created", nullable = false, updatable = false)
    @CreatedDate
    private Timestamp dateCreated;

    @Column(name = "date_updated")
    @LastModifiedDate
    private Timestamp dateUpdated;

    @Override
    public String getAuthority() {
        return name;
    }
}
