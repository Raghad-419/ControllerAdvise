package com.example.safehands.Repository;

import com.example.safehands.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {
    Review findReviewById(Integer id);
    List<Review> findReviewsByBabySitterId(Integer babySitterId);
}
