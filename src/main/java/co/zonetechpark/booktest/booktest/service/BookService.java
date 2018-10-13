package co.zonetechpark.booktest.booktest.service;

import co.zonetechpark.booktest.booktest.jpa.entity.Book;
import co.zonetechpark.booktest.booktest.resources.model.request.BookResource;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    Book createBook(BookResource resource);
    Book updateBook(BookResource resource);
    void deleteBook(Long bookId);
    Page<Book> viewAllBooks(Predicate predicate, Pageable pageable);
    Optional<Book> viewBookById(Long bookId);
    Optional<Book> viewBookByTitle(String title);
    Optional<Book> viewBookByAuthor(String author);
    void toggleBookStatus(Long bookId);
    void rateBook(Long bookId);

}
