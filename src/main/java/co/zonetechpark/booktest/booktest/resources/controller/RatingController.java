package co.zonetechpark.booktest.booktest.resources.controller;

import co.zonetechpark.booktest.booktest.jpa.entity.Rating;
import co.zonetechpark.booktest.booktest.resources.model.request.RatingResource;
import co.zonetechpark.booktest.booktest.resources.model.response.RatingResponse;
import co.zonetechpark.booktest.booktest.service.RatingService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/rating")
@Slf4j
@Api(value = "api/v1/rating", description = "Endpoint for rating management", tags = "Rating Management")
public class RatingController {

    private RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("rating")
    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_REVIEWER', 'ROLE_PUBLISHER', 'ROLE_EDITOR')")
    @ApiOperation(httpMethod = "POST", value = "Resource to rate a book", response = RatingResponse.class, nickname = "rating")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Book has been rated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<RatingResponse> rating(@Valid @RequestBody RatingResource resource) {
        Rating rating = ratingService.rating(resource);
        rating.setUser(null);
        RatingResponse response = new RatingResponse();
        response.setComment(rating.getComment());
        response.setRating(rating.getRating());
        response.setAuthor(rating.getBook().getAuthor());
        response.setComment(rating.getComment());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_EDITOR')")
    @GetMapping("average")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a book rating average", response = Double.class, nickname = "averageRating")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a book rating average"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Double> averageRating(
            @ApiParam(name = "bookId", value = "Provide Book ID", required = true)
            @RequestParam(value = "bookId") Long bookId) {
        Double average = ratingService.ratingAverage(bookId);
        return new ResponseEntity<>(average, HttpStatus.OK);
    }
}
