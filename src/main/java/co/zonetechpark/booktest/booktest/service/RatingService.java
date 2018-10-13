package co.zonetechpark.booktest.booktest.service;

import co.zonetechpark.booktest.booktest.jpa.entity.Rating;
import co.zonetechpark.booktest.booktest.resources.model.request.RatingResource;

public interface RatingService {

    Rating rating(RatingResource resource);

    Double ratingAverage(Long bookId);
}
