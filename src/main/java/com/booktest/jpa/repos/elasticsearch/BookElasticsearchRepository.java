package com.booktest.jpa.repos.elasticsearch;

import com.booktest.jpa.entity.elasticsearch.ElasticsearchBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookElasticsearchRepository extends ElasticsearchRepository<ElasticsearchBook, Long> {

    Optional<ElasticsearchBook> findByTitle(String title);

    Optional<ElasticsearchBook> findByAuthor(String author);

    Optional<ElasticsearchBook> findBookByIsbn(String isbn);

}
