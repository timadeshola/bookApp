package co.zonetechpark.booktest.booktest.jpa.entity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
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
    private Books books;

    @Column(name = "rating")
    @NonNull
    private Double rating;

    @Column(name = "comment")
    private String comment;
}
