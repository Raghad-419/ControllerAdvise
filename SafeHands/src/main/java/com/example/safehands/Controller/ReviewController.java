package com.example.safehands.Controller;

import com.example.safehands.ApiResponse.ApiResponse;
import com.example.safehands.Model.Review;
import com.example.safehands.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;



    // Get all reviews
    @GetMapping("/get")
    public ResponseEntity getAllReviews() {
        return ResponseEntity.status(200).body(reviewService.getAllReviews());
    }


    // Create a new review
    @PostMapping("/add")
    public ResponseEntity createReview(@Valid @RequestBody Review review ) {

        reviewService.createReview(review);
        return ResponseEntity.status(200).body(new ApiResponse("Review added"));
    }

    // Update a review
    @PutMapping("/update/{id}")
    public ResponseEntity updateReview(@PathVariable Integer id, @Valid @RequestBody Review updatedReview) {
        reviewService.updateReview(id, updatedReview);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated"));
    }

    // Delete a review
    @DeleteMapping("/delete/{reviewId}/{userId}")
    public ResponseEntity deleteReview(@PathVariable Integer reviewId, @PathVariable Integer userId) {
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));
    }

    @GetMapping("/getReviewsForBabySitter/{babySitterId}")
    public ResponseEntity getReviewsForBabySitter(@PathVariable Integer babySitterId){
        return ResponseEntity.status(200).body(reviewService.getReviewsForBabySitter(babySitterId));
    }



}
