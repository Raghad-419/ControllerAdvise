package com.example.safehands.Service;

import com.example.safehands.ApiResponse.ApiException;
import com.example.safehands.Model.Review;
import com.example.safehands.Repository.AdminRepository;
import com.example.safehands.Repository.BookingRepository;
import com.example.safehands.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final AdminRepository adminRepository;




    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }
    // Create a new review
    public void createReview(Review review) {
        // Validate booking
        boolean bookingExists = bookingRepository.existsByParentIdAndBabySitterIdAndStatus(review.getParentId(), review.getBabySitterId(), "Completed");

        if (!bookingExists) {
            throw new RuntimeException("Review not allowed: No completed booking exists between the parent and babysitter.");
        }

        // Save the review
         reviewRepository.save(review);
    }

    public void updateReview(Integer id,Review review ){
        Review oldReview =reviewRepository.findReviewById(id);
        if(oldReview==null){
            throw new ApiException("Review not found");
        }
        if(!oldReview.getParentId().equals(review.getParentId())){
            throw new ApiException("Permission denied: You are not the owner of this review.");
        }
        oldReview.setComment(review.getComment());
        oldReview.setRating(review.getRating());

        reviewRepository.save(oldReview);

    }

    public void deleteReview(Integer reviewId,Integer userId){
        Review review=reviewRepository.findReviewById(reviewId);
        if(review==null){
            throw new ApiException("Review not found");
        }
        Boolean isOwner = review.getParentId().equals(userId);
        Boolean isAdmin=adminRepository.findAdminById(userId)!=null;
        if(isAdmin||isOwner){
            reviewRepository.delete(review);
        }else throw new ApiException("Permission denied: Only the review owner or an admin can delete this review.");
    }


    // 15 method to get all reviews of babySitter
    public List<Review> getReviewsForBabySitter(Integer babySitterId){
        List<Review> reviews = reviewRepository.findReviewsByBabySitterId(babySitterId);
        if(reviews.isEmpty()){
            throw new ApiException("No reviews found");
        }
        return reviews;
    }






}


