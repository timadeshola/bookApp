package co.zonetechpark.booktest.booktest.service;

import co.zonetechpark.booktest.booktest.jpa.entity.Books;
import co.zonetechpark.booktest.booktest.resources.model.request.BookResource;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    Books createBooks(BookResource resource);
    Books updateBooks(BookResource resource);
    void deleteBooks(Long bookId);
    Page<Books> viewAllBooks(Predicate predicate, Pageable pageable);
    Optional<Books> viewBookById(Long bookId);
    Optional<Books> viewBookByName(String name);
    void toggleBookStatus(Long bookId);

}
