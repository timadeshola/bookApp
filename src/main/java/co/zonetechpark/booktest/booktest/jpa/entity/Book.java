package co.zonetechpark.booktest.booktest.jpa.entity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "AUTHOR_BOOK")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Audited
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "isbn")
    private Long id;

    @Column(name = "title", unique = true)
    @NonNull
    private String title;

    @Column(name = "author")
    @NonNull
    private String author;

    @Column(name = "status")
    private Boolean status;

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
