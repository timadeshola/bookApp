package com.booktest.testcases.repository;

import com.booktest.jpa.entity.Book;
import com.booktest.jpa.repos.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @Before
    public void setUp() {
        book = new Book();
    }
    @Test
    public void createBookTest() {
        book = Book.builder().id(10L).isbn("ISBN-200-0P91").title("ElasticsearchBook App").author("John Adeshola").status(true).build();
        book = bookRepository.save(book);
        assertThat(book.getAuthor()).isEqualTo("John Adeshola");
    }

    @Test
    public void updateBookTest() {
        book = Book.builder().id(10L).isbn("ISBN-200-0P91").title("ElasticsearchBook App").author("John Adeshola").status(true).build();
        book = bookRepository.save(book);
        assertThat(book.getAuthor()).isEqualTo("John Adeshola");
    }

    @Test
    public void viewBookByTitle() {
        Optional<Book> optionalBook = bookRepository.findByTitle("Java 9 Programming By Example");
        if(optionalBook.isPresent()) {
            book = optionalBook.get();
            assertThat(book.getAuthor()).isEqualTo("Peter Verhas");
            assertThat(book.getAuthor()).isNotNull();
        }
    }

    @Test
    public void viewBookByAuthor() {
        Optional<Book> optionalBook = bookRepository.findByAuthor("Peter Verhas");
        if(optionalBook.isPresent()) {
            book = optionalBook.get();
            assertThat(book.getAuthor()).isEqualTo("Peter Verhas");
            assertThat(book.getAuthor()).isNotNull();
        }
    }

    @Test
    public void viewBookByIsbn() {
        Optional<Book> optionalBook = bookRepository.findBookByIsbn("ISBN-4294-B547");
        if(optionalBook.isPresent()) {
            book = optionalBook.get();
            assertThat(book.getAuthor()).isEqualTo("Peter Verhas");
            assertThat(book.getAuthor()).isNotNull();
        }
    }

    @Test
    public void viewBookById() {
        Optional<Book> optionalBook = bookRepository.findById(1L);
        if(optionalBook.isPresent()) {
            book = optionalBook.get();
            assertThat(book.getAuthor()).isEqualTo("Peter Verhas");
            assertThat(book.getAuthor()).isNotNull();
        }
    }

    @Test
    public void deleteBookById() {
        Optional<Book> optionalBook = bookRepository.findById(1L);
        if(optionalBook.isPresent()) {
            book = optionalBook.get();
            bookRepository.deleteById(book.getId());
            assertThat(book.getAuthor()).isNotIn(book);
        }
    }
}
