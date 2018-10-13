package co.zonetechpark.booktest.booktest.resources.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RatingResource {

    private Long id;
    private Long bookId;
    private Double rating;
    private String comment;
}
