package co.zonetechpark.booktest.booktest.resources.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
public class RatingResource {

    private Long id;

    private Long bookId;

    @NotNull
    private Double rating;

    @Size(min = 4, max = 500, message = "comment cannot be less than 4 and greater than 500 characters")
    private String comment;
}
