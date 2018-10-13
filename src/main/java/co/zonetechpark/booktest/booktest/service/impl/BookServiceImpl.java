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
    public Books createBook(BookResource resource) {
        Optional<Books> optionalBooks = bookRepository.findByTitle(resource.getTitle());
        if(optionalBooks.isPresent()) {
            throw new CustomException("Book with the title already exist", HttpStatus.CONFLICT);
        }
        Books books = new Books();
        books.setTitle(resource.getTitle());
        books.setAuthor(resource.getAuthor());
        books.setStatus(true);
        return bookRepository.save(books);
    }

    @Override
    public Books updateBook(BookResource resource) {
        Optional<Books> optionalBooks = bookRepository.findById(resource.getId());
        if(!optionalBooks.isPresent()) {
            throw new CustomException("Book not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Books books = optionalBooks.get();
        books.setTitle(resource.getTitle());
        books.setAuthor(resource.getAuthor());
        return bookRepository.save(books);
    }

    @Override
    public void deleteBook(Long bookId) {
        Optional<Books> books = bookRepository.findById(bookId);
        if(books.isPresent()) {
            throw new CustomException("Book not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        books.ifPresent(book -> bookRepository.delete(book));
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
    public Optional<Books> viewBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public Optional<Books> viewBookByAuthor(String author) {
        return bookRepository.findByAuthor(author);
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

    @Override
    public void rateBook(Long bookId) {
        Optional<Books> optionalBooks = bookRepository.findById(bookId);
        if(!optionalBooks.isPresent()) {
            throw new CustomException("Book not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Books books = optionalBooks.get();


        for (int i = 0; i<=5; i++) {
//            books.setRating();
            bookRepository.saveAndFlush(books);
        }

    }
}
