package com.booktest.service;

import com.booktest.jpa.entity.Book;
import com.booktest.jpa.entity.elasticsearch.ElasticsearchBook;
import com.booktest.resources.model.request.BookResource;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book createBook(BookResource resource);

    Book updateBook(BookResource resource);

    void deleteBook(Long bookId);

    void deleteBooks(List<Long> bookIds);

    Page<Book> viewAllBooks(Predicate predicate, Pageable pageable);

    Optional<ElasticsearchBook> viewBookById(Long bookId);

    Optional<ElasticsearchBook> viewBookByTitle(String title);

    Optional<ElasticsearchBook> viewBookByAuthor(String author);

    Optional<ElasticsearchBook> viewBookByIsbn(String isbn);

    void toggleBookStatus(Long bookId);

    Page<ElasticsearchBook> findAll(String text, int start, int limit);

}
