package co.zonetechpark.booktest.booktest.resources.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookResource {

    private Long id;
    private String title;
    private String author;
}
