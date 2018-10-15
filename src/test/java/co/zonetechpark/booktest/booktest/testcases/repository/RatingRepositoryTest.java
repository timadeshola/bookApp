package co.zonetechpark.booktest.booktest.testcases.repository;

import co.zonetechpark.booktest.booktest.jpa.entity.Book;
import co.zonetechpark.booktest.booktest.jpa.entity.Rating;
import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.jpa.repos.RatingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    private Rating rating;

    @Before
    public void setUp() {
        rating = new Rating();
    }
    @Test
    public void ratingTest() {
        rating = Rating.builder().id(10L).rating(75D).comment("nice job").dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).book(
                new Book(10L, "ISBN-200-0P91", "Book App", "John Adeshola", true, new Timestamp(System.currentTimeMillis()), null, null)).user(
                new User(10L, "jadeshola", "Password@123", "John", "Adeshola", "John Adeshola", "timadeshola@gmail.com", "2347030239942", true, new Timestamp(System.currentTimeMillis()), null, null, null, null, new HashSet<>(Arrays.asList(new Role("AUTHOR"))))).build();

        rating = ratingRepository.saveAndFlush(rating);
        assertThat(rating.getComment()).isEqualTo("nice job");
    }

    @Test
    public void viewRatingByBookId() {
        List<Rating> ratings = ratingRepository.findRatingByBook_Id(1L);
        if(!ratings.isEmpty()) {
            rating = ratings.get(0);
            assertThat(rating.getComment()).isEqualTo("John Adeshola");
        }
    }


}
