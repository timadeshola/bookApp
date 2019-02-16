package com.booktest.jpa.repos;

import com.booktest.jpa.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {

    Optional<Book> findByTitle(String title);

    Optional<Book> findByAuthor(String author);

    Optional<Book> findBookByIsbn(String isbn);

}
