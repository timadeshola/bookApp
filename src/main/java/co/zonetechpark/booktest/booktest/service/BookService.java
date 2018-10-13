package co.zonetechpark.booktest.booktest.service;

import co.zonetechpark.booktest.booktest.jpa.entity.Books;
import co.zonetechpark.booktest.booktest.resources.model.request.BookResource;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    Books createBook(BookResource resource);
    Books updateBook(BookResource resource);
    void deleteBook(Long bookId);
    Page<Books> viewAllBooks(Predicate predicate, Pageable pageable);
    Optional<Books> viewBookById(Long bookId);
    Optional<Books> viewBookByTitle(String title);
    Optional<Books> viewBookByAuthor(String author);
    void toggleBookStatus(Long bookId);
    void rateBook(Long bookId);

}
