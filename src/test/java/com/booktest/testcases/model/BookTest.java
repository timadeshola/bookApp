package com.booktest.testcases.model;

import com.booktest.jpa.entity.Book;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

public class BookTest {

    private Book book;

    @Before
    public void setUp() {
        book = new Book();
    }

    @Test
    public void testBook() {
        book = Book.builder().id(10L).isbn("ISBN-200-0P91").title("ElasticsearchBook App").author("John Adeshola").status(true)
                .dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).build();

        assertEquals("Author Test", book.getAuthor(), "John Adeshola");
    }

    @Test
    public void testBookTitle() {
        book.setTitle("ElasticsearchBook App");
        assertEquals("ElasticsearchBook title test case", book.getTitle(), "ElasticsearchBook App");
    }
}
