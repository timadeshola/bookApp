package co.zonetechpark.booktest.booktest.resources.model.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class BookResponse {

    private String title;
    private String author;
    private Timestamp dateCreated;
}
