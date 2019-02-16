package com.booktest.resources.model.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RatingResponse {

    private Double rating;
    private String comment;
    private String title;
    private String author;
}
