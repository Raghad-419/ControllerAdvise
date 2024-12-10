package com.example.safehands.Service;

import com.example.safehands.ApiResponse.ApiException;
import com.example.safehands.Model.BabySitter;
import com.example.safehands.Model.Review;
import com.example.safehands.Repository.AdminRepository;
import com.example.safehands.Repository.BabySitterRepository;
import com.example.safehands.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class BabySitterService {

    private final BabySitterRepository babySitterRepository;
    private final ReviewRepository reviewRepository;
    private final AdminRepository adminRepository;


    // Get all babysitters
    public List<BabySitter> getAllBabySitters() {
        return babySitterRepository.findAll();
    }


    // Add a new babysitter
    public BabySitter addBabySitter(BabySitter babySitter) {
        return babySitterRepository.save(babySitter);
    }

    // Update an existing babysitter
    public BabySitter updateBabySitter(Integer id, BabySitter updatedBabySitter) {

        BabySitter existingBabySitter = babySitterRepository.findBabySitterById(id);
        if(existingBabySitter ==null){
            throw new ApiException("BabySitter not found");
        }
        existingBabySitter.setName(updatedBabySitter.getName());
        existingBabySitter.setEmail(updatedBabySitter.getEmail());
        existingBabySitter.setPassword(updatedBabySitter.getPassword());
        existingBabySitter.setBio(updatedBabySitter.getBio());
        existingBabySitter.setHourlyRate(updatedBabySitter.getHourlyRate());
        existingBabySitter.setExtraChildRate(updatedBabySitter.getExtraChildRate());
        existingBabySitter.setCertifications(updatedBabySitter.getCertifications());
        existingBabySitter.setSkills(updatedBabySitter.getSkills());
        existingBabySitter.setAverageRating(updatedBabySitter.getAverageRating());
        existingBabySitter.setTotalRatings(updatedBabySitter.getTotalRatings());

        return babySitterRepository.save(existingBabySitter);
    }

    // Delete a babysitter
    public void deleteBabySitter(Integer id) {
        BabySitter existingBabySitter = babySitterRepository.findBabySitterById(id);
        if(existingBabySitter ==null){
            throw new ApiException("BabySitter not found");
        }
        BabySitter babySitter = babySitterRepository.findBabySitterById(id);
        babySitterRepository.delete(babySitter);
    }


//1 method to calculate Average Rating of babySitter
    public Double calculateAverageRating(Integer babySitterId){
        BabySitter babySitter= babySitterRepository.findBabySitterById(babySitterId);
        if (babySitter == null) {
            throw new RuntimeException("BabySitter with ID " + babySitterId + " does not exist.");
        }

        List<Review> reviews =reviewRepository.findReviewsByBabySitterId(babySitterId);
        if(reviews.isEmpty()){
            return 0.0;
        }

        // Calculate the sum of ratings
        double sum = 0.0;
        for (Review review : reviews) {
            sum += review.getRating();
        }

        // Calculate the average rating
        double averageRating = sum / reviews.size();

        // Update Babysitter's ratings
        babySitter.setTotalRatings(reviews.size());
        babySitter.setAverageRating(averageRating);

        // Save updated Babysitter (if needed)
        babySitterRepository.save(babySitter);

        return averageRating;
    }

//2 method to find TopBabySitters
    public List<BabySitter> findTopBabySitters(Integer numberOfBabySitters) {
        List<BabySitter> babySitterList = babySitterRepository.findAll();

        // Sort the babysitters by rating in descending order
        babySitterList.sort((b1, b2) -> Double.compare(b2.getAverageRating(), b1.getAverageRating()));

        // Create a list to hold the top N babysitters
        List<BabySitter> bestBabySitters = new ArrayList<>();

        // Add the top N babysitters to the list
        for (int i = 0; i < numberOfBabySitters && i < babySitterList.size(); i++) {
            bestBabySitters.add(babySitterList.get(i));
        }

        return bestBabySitters;
    }


}


