package co.zonetechpark.booktest.booktest.testcases.services;

import co.zonetechpark.booktest.booktest.jpa.entity.Book;
import co.zonetechpark.booktest.booktest.jpa.entity.Rating;
import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.resources.model.request.RatingResource;
import co.zonetechpark.booktest.booktest.service.RatingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RatingServiceTest {

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

        assertEquals("Book creation test", rating.getComment(), "nice job");
    }

    @Test
    public void ratingAverageTest() {

        rating = Rating.builder().id(10L).rating(75D).comment("nice job").dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).book(
                new Book(10L, "ISBN-200-0P91", "Book App", "John Adeshola", true, new Timestamp(System.currentTimeMillis()), null, null)).user(
                new User(10L, "jadeshola", "Password@123", "John", "Adeshola", "John Adeshola", "timadeshola@gmail.com", "2347030239942", true, new Timestamp(System.currentTimeMillis()), null, null, null, null, new HashSet<>(Arrays.asList(new Role("AUTHOR"))))).build();

        assertEquals("rating average test", rating.getComment(), "nice job");

    }
}
