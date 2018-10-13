package co.zonetechpark.booktest.booktest.jpa.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    @NonNull
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    @NonNull
    private String firstName;

    @Column(name = "last_name")
    @NonNull
    private String lastName;

    @Transient
    private String fullName;

    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name = "phone_number")
    @NonNull
    private String phoneNumber;

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

    @Column(name = "last_login_date")
    private Timestamp lastLoginDate;

    @Column(name = "last_logout_date")
    private Timestamp lastLogoutDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Role> roles;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
