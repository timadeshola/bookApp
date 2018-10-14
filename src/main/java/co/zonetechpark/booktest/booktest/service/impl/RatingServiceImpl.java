package co.zonetechpark.booktest.booktest.service.impl;

import co.zonetechpark.booktest.booktest.core.exceptions.CustomException;
import co.zonetechpark.booktest.booktest.jpa.entity.Book;
import co.zonetechpark.booktest.booktest.jpa.entity.Rating;
import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.jpa.repos.BookRepository;
import co.zonetechpark.booktest.booktest.jpa.repos.RatingRepository;
import co.zonetechpark.booktest.booktest.jpa.repos.UserRepository;
import co.zonetechpark.booktest.booktest.resources.model.request.RatingResource;
import co.zonetechpark.booktest.booktest.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RatingServiceImpl implements RatingService {

    private RatingRepository ratingRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;

    public RatingServiceImpl(RatingRepository ratingRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Rating rating(RatingResource resource) {
        Rating rating = new Rating();

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String username = currentUser.getName();
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if(!optionalUser.isPresent()) {
            throw new CustomException("User not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        User user = optionalUser.get();

        Optional<Book> booksOptional = bookRepository.findById(resource.getBookId());
        if(!booksOptional.isPresent()) {
            throw new CustomException("Book not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Book book = booksOptional.get();

        rating.setUser(user);
        rating.setBook(book);
        rating.setComment(resource.getComment());
        rating.setRating(resource.getRating());
        return ratingRepository.save(rating);
    }

    @Override
    public Double ratingAverage(Long bookId) {
        List<Rating> ratings = ratingRepository.findRatingByBook_Id(bookId);

        Double average = ratings.stream().collect(Collectors.averagingDouble(Rating::getRating));
        log.info("AVERAGE RATING ===========> {}", average);
        return average;
    }
}
