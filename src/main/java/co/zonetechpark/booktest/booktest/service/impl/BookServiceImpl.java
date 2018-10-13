package co.zonetechpark.booktest.booktest.service.impl;

import co.zonetechpark.booktest.booktest.core.CustomException;
import co.zonetechpark.booktest.booktest.jpa.entity.Books;
import co.zonetechpark.booktest.booktest.jpa.repos.BookRepository;
import co.zonetechpark.booktest.booktest.resources.model.request.BookResource;
import co.zonetechpark.booktest.booktest.service.BookService;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Books createBooks(BookResource resource) {
        Optional<Books> optionalBooks = bookRepository.findByName(resource.getName());
        if(optionalBooks.isPresent()) {
            throw new CustomException("Book with the title already exist", HttpStatus.CONFLICT);
        }
        Books books = new Books();
        books.setName(resource.getName());
        books.setRating(resource.getRating());
        books.setStatus(true);
        return bookRepository.save(books);
    }

    @Override
    public Books updateBooks(BookResource resource) {
        Optional<Books> optionalBooks = bookRepository.findById(resource.getId());
        if(optionalBooks.isPresent()) {
            Books books = optionalBooks.get();
            books.setName(resource.getName());
            books.setRating(resource.getRating());
            return bookRepository.save(books);
        }
        return null;
    }

    @Override
    public void deleteBooks(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public Page<Books> viewAllBooks(Predicate predicate, Pageable pageable) {
        return bookRepository.findAll(predicate, pageable);
    }

    @Override
    public Optional<Books> viewBookById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    public Optional<Books> viewBookByName(String name) {
        return bookRepository.findByName(name);
    }

    @Override
    public void toggleBookStatus(Long bookId) {
        Optional<Books> optionalBooks = bookRepository.findById(bookId);
        if(optionalBooks.isPresent()) {
            Books books = optionalBooks.get();
            if(books.getStatus().equals(true)) {
                books.setStatus(false);
            }else {
                books.setStatus(true);
            }
            bookRepository.saveAndFlush(books);
        }
    }
}
