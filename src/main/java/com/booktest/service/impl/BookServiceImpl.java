package com.booktest.service.impl;

import com.booktest.core.exceptions.CustomException;
import com.booktest.jpa.entity.Book;
import com.booktest.jpa.entity.elasticsearch.ElasticsearchBook;
import com.booktest.jpa.repos.BookRepository;
import com.booktest.jpa.repos.elasticsearch.BookElasticsearchRepository;
import com.booktest.resources.model.request.BookResource;
import com.booktest.service.BookService;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private ModelMapper modelMapper;
    private BookElasticsearchRepository elasticsearchRepository;
    private ElasticsearchTemplate elasticsearchTemplate;

    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper, BookElasticsearchRepository elasticsearchRepository, ElasticsearchTemplate elasticsearchTemplate) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.elasticsearchRepository = elasticsearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Book createBook(BookResource resource) {
        Optional<Book> optionalBooks = bookRepository.findByTitle(resource.getTitle());
        if (optionalBooks.isPresent()) {
            throw new CustomException("ElasticsearchBook with the title already exist", HttpStatus.CONFLICT);
        }
        Book book = new Book();
        book.setTitle(resource.getTitle());
        book.setAuthor(resource.getAuthor());
        book.setStatus(true);
        bookRepository.save(book);
        ElasticsearchBook elasticsearchBook = modelMapper.map(book, ElasticsearchBook.class);
        elasticsearchRepository.save(elasticsearchBook);
        return book;
    }

    @Override
    public Book updateBook(BookResource resource) {
        Optional<Book> optionalBooks = bookRepository.findById(resource.getId());
        if (!optionalBooks.isPresent()) {
            throw new CustomException("ElasticsearchBook not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Book book = optionalBooks.get();
        book.setTitle(resource.getTitle());
        book.setAuthor(resource.getAuthor());
        bookRepository.save(book);
        ElasticsearchBook elasticsearchBook = modelMapper.map(book, ElasticsearchBook.class);
        elasticsearchRepository.save(elasticsearchBook);
        return book;
    }

    @Override
    public void deleteBook(Long bookId) {
        Optional<Book> books = bookRepository.findById(bookId);
        if (!books.isPresent()) {
            throw new CustomException("ElasticsearchBook not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        books.ifPresent(book -> {
            bookRepository.delete(book);
            elasticsearchRepository.deleteById(bookId);
        });
    }

    @Transactional
    @Override
    public void deleteBooks(List<Long> bookIds) {
        List<Book> books = bookRepository.findAllById(bookIds);
        if (books.isEmpty()) {
            throw new CustomException("ElasticsearchBook is not available", HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteAll(books);
        books.parallelStream().forEach(book -> elasticsearchRepository.deleteById(book.getId()));
    }

    @Override
    public Page<Book> viewAllBooks(Predicate predicate, Pageable pageable) {
        return bookRepository.findAll(predicate, pageable);
    }

    @Override
    public Optional<ElasticsearchBook> viewBookById(Long bookId) {
        return elasticsearchRepository.findById(bookId);
    }

    @Override
    public Optional<ElasticsearchBook> viewBookByTitle(String title) {
        return elasticsearchRepository.findByTitle(title);
    }

    @Override
    public Optional<ElasticsearchBook> viewBookByAuthor(String author) {
        return elasticsearchRepository.findByAuthor(author);
    }

    @Override
    public Optional<ElasticsearchBook> viewBookByIsbn(String isbn) {
        return elasticsearchRepository.findBookByIsbn(isbn);
    }

    @Transactional
    @Override
    public void toggleBookStatus(Long bookId) {
        Optional<Book> optionalBooks = bookRepository.findById(bookId);
        if (optionalBooks.isPresent()) {
            Book book = optionalBooks.get();
            ElasticsearchBook elasticsearchBook = modelMapper.map(book, ElasticsearchBook.class);
            if (book.getStatus().equals(true)) {
                book.setStatus(false);
                elasticsearchBook.setStatus(false);
            } else {
                book.setStatus(true);
                elasticsearchBook.setStatus(true);
            }
            bookRepository.save(book);
            elasticsearchRepository.save(elasticsearchBook);
        }
    }

    @Override
    public Page<ElasticsearchBook> findAll(String text, int start, int limit) {

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(
                        QueryBuilders.queryStringQuery(text)
                                .lenient(true)
                                .field("author")
                                .field("title")
                                .field("isbn")
                                .field("status")
                ).should(QueryBuilders.queryStringQuery("*" + text + "*")
                        .lenient(true)
                        .field("author")
                        .field("title")
                        .field("isbn")
                        .field("status"));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(start, limit))
                .build();

        return elasticsearchTemplate.queryForPage(searchQuery, ElasticsearchBook.class);
    }
}
