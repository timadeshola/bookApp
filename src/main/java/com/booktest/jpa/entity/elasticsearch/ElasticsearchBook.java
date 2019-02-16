package com.booktest.jpa.entity.elasticsearch;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "elasticsearch", type = "default", shards = 2, replicas = 0, refreshInterval = "-1")
public class ElasticsearchBook implements Serializable {

    @Id
    private Long id;

    private String isbn;

    private String title;

    private String author;

    private Boolean status;

    private Timestamp dateCreated;

    private Timestamp dateUpdated;

    private Timestamp dateDeleted;

}
