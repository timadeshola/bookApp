package co.zonetechpark.booktest.booktest.jpa.entity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "RATINGS")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Audited
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
}
