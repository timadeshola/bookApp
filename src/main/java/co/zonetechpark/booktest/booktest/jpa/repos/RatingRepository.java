package co.zonetechpark.booktest.booktest.jpa.repos;

import co.zonetechpark.booktest.booktest.jpa.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>, QuerydslPredicateExecutor<Rating> {

    List<Rating> findRatingByBook_Id(Long bookId);
}
