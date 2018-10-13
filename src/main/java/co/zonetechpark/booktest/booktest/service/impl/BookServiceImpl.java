package co.zonetechpark.booktest.booktest.service.impl;

import co.zonetechpark.booktest.booktest.core.CustomException;
import co.zonetechpark.booktest.booktest.jpa.entity.Book;
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
    public Book createBook(BookResource resource) {
        Optional<Book> optionalBooks = bookRepository.findByTitle(resource.getTitle());
        if(optionalBooks.isPresent()) {
            throw new CustomException("Book with the title already exist", HttpStatus.CONFLICT);
        }
        Book book = new Book();
        book.setTitle(resource.getTitle());
        book.setAuthor(resource.getAuthor());
        book.setStatus(true);
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(BookResource resource) {
        Optional<Book> optionalBooks = bookRepository.findById(resource.getId());
        if(!optionalBooks.isPresent()) {
            throw new CustomException("Book not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Book book = optionalBooks.get();
        book.setTitle(resource.getTitle());
        book.setAuthor(resource.getAuthor());
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        Optional<Book> books = bookRepository.findById(bookId);
        if(books.isPresent()) {
            throw new CustomException("Book not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        books.ifPresent(book -> bookRepository.delete(book));
    }

    @Override
    public Page<Book> viewAllBooks(Predicate predicate, Pageable pageable) {
        return bookRepository.findAll(predicate, pageable);
    }

    @Override
    public Optional<Book> viewBookById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    public Optional<Book> viewBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public Optional<Book> viewBookByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public void toggleBookStatus(Long bookId) {
        Optional<Book> optionalBooks = bookRepository.findById(bookId);
        if(optionalBooks.isPresent()) {
            Book book = optionalBooks.get();
            if(book.getStatus().equals(true)) {
                book.setStatus(false);
            }else {
                book.setStatus(true);
            }
            bookRepository.saveAndFlush(book);
        }
    }

    @Override
    public void rateBook(Long bookId) {
        Optional<Book> optionalBooks = bookRepository.findById(bookId);
        if(!optionalBooks.isPresent()) {
            throw new CustomException("Book not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Book book = optionalBooks.get();


        for (int i = 0; i<=5; i++) {
//            book.setRating();
            bookRepository.saveAndFlush(book);
        }

    }
}
