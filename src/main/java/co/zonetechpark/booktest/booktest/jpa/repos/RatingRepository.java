package co.zonetechpark.booktest.booktest.jpa.repos;

import co.zonetechpark.booktest.booktest.jpa.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long>, QuerydslPredicateExecutor<Rating> {

    List<Rating> findRatingByBooks_Id(Long bookId);
}
