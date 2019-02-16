package com.booktest.resources.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatingResource {

    private Long id;

    private Long bookId;

    @NotNull
    private Double rating;

    @Size(min = 4, max = 500, message = "comment cannot be less than 4 and greater than 500 characters")
    private String comment;
}
