package com.booktest.testcases.model;

import com.booktest.jpa.entity.Book;
import com.booktest.jpa.entity.Rating;
import com.booktest.jpa.entity.Role;
import com.booktest.jpa.entity.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class RatingTest {

    private Rating rating;

    @Before
    public void setUp() {
        rating = new Rating();
    }

    @Test
    public void testRating() {
        rating = Rating.builder().id(10L).rating(75D).comment("nice job").dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).book(
                new Book(10L, "ISBN-200-0P91", "ElasticsearchBook App", "John Adeshola", true, new Timestamp(System.currentTimeMillis()), null, null)).user(
                new User(10L, "jadeshola", "Password@123", "John", "Adeshola", "John Adeshola", "timadeshola@gmail.com", "2347030239942", true, new Timestamp(System.currentTimeMillis()), null, null, null, null, new HashSet<>(Arrays.asList(new Role("AUTHOR"))))).build();

        assertEquals("Check if comment is correct", rating.getComment(), "nice job");
        assertEquals(75D, rating.getRating(), 75D);
    }

    @Test
    public void testRatingComment() {
        rating.setComment("Great Job!");
        assertEquals("Comment test case", rating.getComment(), "Great Job!");
    }
}
