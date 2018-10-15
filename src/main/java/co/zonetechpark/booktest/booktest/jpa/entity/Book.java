package co.zonetechpark.booktest.booktest.jpa.entity;

import co.zonetechpark.booktest.booktest.core.utils.AppUtils;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "BOOK")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Audited
@Builder
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isbn", unique = true)
    private String isbn;

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
        setIsbn(getIsbn());
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

    public String getIsbn() {
        return "ISBN" + "-" + AppUtils.generateUUIDNumber();
    }



}
