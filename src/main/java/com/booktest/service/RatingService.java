package com.booktest.service;

import com.booktest.jpa.entity.Rating;
import com.booktest.resources.model.request.RatingResource;

public interface RatingService {

    Rating rating(RatingResource resource);

    Double ratingAverage(Long bookId);
}
