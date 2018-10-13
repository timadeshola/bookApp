package co.zonetechpark.booktest.booktest.resources.controller;

import co.zonetechpark.booktest.booktest.jpa.entity.Rating;
import co.zonetechpark.booktest.booktest.resources.model.request.RatingResource;
import co.zonetechpark.booktest.booktest.resources.model.response.RatingResponse;
import co.zonetechpark.booktest.booktest.service.RatingService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/rating")
@Slf4j
@Api(value = "api/v1/rating", description = "Endpoint for managing book rating", tags = "Rating")
public class RatingController {

    private RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER')")
    @PostMapping("rating")
    public ResponseEntity<RatingResponse> rating(@RequestBody RatingResource resource) {
        Rating rating = ratingService.rating(resource);
        rating.setUser(null);
        RatingResponse response = new RatingResponse();
        response.setComment(rating.getComment());
        response.setRating(rating.getRating());
        response.setAuthor(rating.getBooks().getAuthor());
        response.setComment(rating.getComment());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @GetMapping("average")
    public ResponseEntity<Double> averageRating(@RequestParam(value = "bookId") Long bookId) {
        Double average = ratingService.ratingAverage(bookId);
        return new ResponseEntity<>(average, HttpStatus.OK);
    }
}
