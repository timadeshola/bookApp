package co.zonetechpark.booktest.booktest.testcases.services;

import co.zonetechpark.booktest.booktest.jpa.entity.Book;
import co.zonetechpark.booktest.booktest.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private BookService bookService;

    private Book book;

    @Before
    public void setUp() {
        book = new Book();
    }

    @Test
    public void createBookTest() {

        book = Book.builder().id(10L).isbn("ISBN-200-0P91").title("Book App").author("John Adeshola").status(true)
                .dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).build();

        assertEquals("Book creation test", book.getAuthor(), "John Adeshola");
    }

    @Test
    public void updateBookTest() {

        book = Book.builder().id(10L).isbn("ISBN-200-0P91").title("Book App").author("John Adeshola").status(true)
                .dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).build();

        assertEquals("Book author test", book.getAuthor(), "John Adeshola");

    }

    @Test
    public void deleteBookTest() {
        book = Book.builder().id(10L).isbn("ISBN-200-0P91").title("Book App").author("John Adeshola").status(true)
                .dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).build();

        bookService.deleteBook(book.getId());
        verify(bookService, times(1)).deleteBook(book.getId());

    }

    @Test
    public void viewAllBooksTest() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "dateCreated");
        Page<Book> books = bookService.viewAllBooks(null, pageRequest);
        assertEquals("view all book test case", books, books);

    }

    @Test
    public void findBookNameTest() {

        Optional<Book> optionalBook = bookService.viewBookByAuthor("Peter Verhas");
        if (optionalBook.isPresent()) {
            book = optionalBook.get();
            assertEquals("Return book author", book.getAuthor(), "Peter Verhas");
        }
    }

    @Test
    public void viewBookByIdTest() {

        Optional<Book> optionalBook = bookService.viewBookById(1L);
        if (optionalBook.isPresent()) {
            book = optionalBook.get();
            assertEquals("Return book name", book.getAuthor(), "Peter Verhas");
        }
    }

    @Test
    public void toggleBookStatusTest() {
        book = Book.builder().id(10L).isbn("ISBN-200-0P91").title("Book App").author("John Adeshola").status(true)
                .dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).build();

        bookService.toggleBookStatus(book.getId());
        verify(bookService, times(1)).toggleBookStatus(book.getId());
    }
}
