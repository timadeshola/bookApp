package co.zonetechpark.booktest.booktest.jpa.entity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "RATINGS")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Audited
@Builder
public class Rating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_fk")
    private User user;

    @OneToOne
    @JoinColumn(name = "book_fk")
    private Book book;

    @Column(name = "rating")
    @NonNull
    private Double rating;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Column(name = "date_updated")
    private Timestamp dateUpdated;

    @Column(name = "date_deleted")
    private Timestamp dateDeleted;

    @PrePersist
    protected void onCreate() {
        setDateCreated(new Timestamp(System.currentTimeMillis()));
    }

    @PreUpdate
    protected void onUpdate() {
        setDateUpdated(new Timestamp(System.currentTimeMillis()));
    }

    @PreRemove
    protected void onDelete() {
        setDateDeleted(new Timestamp(System.currentTimeMillis()));
    }
}
