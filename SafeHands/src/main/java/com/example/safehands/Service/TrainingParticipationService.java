package com.example.safehands.Service;

import com.example.safehands.ApiResponse.ApiException;
import com.example.safehands.Model.BabySitter;
import com.example.safehands.Model.Course;
import com.example.safehands.Model.TrainingParticipation;
import com.example.safehands.Repository.BabySitterRepository;
import com.example.safehands.Repository.ParticipationRepository;
import com.example.safehands.Repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class TrainingParticipationService {
    private final ParticipationRepository trainingParticipationRepository;
    private final BabySitterRepository babySitterRepository;
    private final CourseRepository courseRepository;

    public List<TrainingParticipation> getTrainingParticipation(){
        return trainingParticipationRepository.findAll();
    }


    public void addTrainingParticipation(TrainingParticipation trainingParticipation){
        BabySitter babySitter= babySitterRepository.findBabySitterById(trainingParticipation.getBabySitterId());
        if(babySitter==null){
            throw new ApiException("BabySitter not found");
        }
        if(!babySitter.getActivation()){
            throw new ApiException("BabySitter is not active");
        }
        Course course= courseRepository.findCourseById(trainingParticipation.getCourseId());
        if(course==null){
            throw new ApiException("Course not found");
        }

        trainingParticipationRepository.save(trainingParticipation);

    }

    public void updateTrainingParticipation(Integer id, TrainingParticipation trainingParticipation){
        TrainingParticipation oldTrainingParticipation=trainingParticipationRepository.findTrainingParticipationById(id);
        if(oldTrainingParticipation==null){
            throw new ApiException("Course Participation not found");
        }
        BabySitter babySitter= babySitterRepository.findBabySitterById(trainingParticipation.getBabySitterId());
        if(babySitter==null){
            throw new ApiException("BabySitter not found");
        }
        if(!babySitter.getActivation()){
            throw new ApiException("BabySitter is not active");
        }
        Course course= courseRepository.findCourseById(trainingParticipation.getCourseId());
        if(course==null){
            throw new ApiException("Course not found");
        }

        oldTrainingParticipation.setCourseId(trainingParticipation.getCourseId());
        oldTrainingParticipation.setStatus(trainingParticipation.getStatus());
        oldTrainingParticipation.setBabySitterId(trainingParticipation.getBabySitterId());

        trainingParticipationRepository.save(oldTrainingParticipation);


    }



    public void deleteTrainingParticipation(Integer id){
        TrainingParticipation trainingParticipation=trainingParticipationRepository.findTrainingParticipationById(id);
        if(trainingParticipation==null){
            throw new ApiException("Course Participation not found");
        }
        trainingParticipationRepository.delete(trainingParticipation);

        }


//16 method to UpdateTrainingStatus
    public void checkAndUpdateTrainingStatus() {
        List<TrainingParticipation> participations = trainingParticipationRepository.findTrainingParticipationByStatus("registered");

        for (TrainingParticipation participation : participations) {
            Course course = courseRepository.findById(participation.getCourseId()).orElse(null);

            if (course != null && LocalDateTime.now().isAfter(course.getEndDate())) {
                participation.setStatus("completed");
                trainingParticipationRepository.save(participation); // Save the updated status
            }
        }
    }


    @Scheduled(fixedRate = 60000) // This will run every minute
    public void updateStatuses() {
        checkAndUpdateTrainingStatus();
    }



    // 17 Certify babysitter after completing training
    public void certifyBabysitter(Integer participationId) {
        // Fetch participation record
        TrainingParticipation participation = trainingParticipationRepository.findTrainingParticipationById(participationId);
        if(participation==null){
            throw new ApiException("Participation record not found");}

        // Ensure training is marked as completed
        if (!participation.getStatus().equals("completed")) {
            throw new RuntimeException("Course must be completed before certification");
        }

        // Fetch babysitter
        BabySitter babysitter = babySitterRepository.findBabySitterById(participation.getBabySitterId());
        if(babysitter==null){
            throw  new ApiException("Babysitter not found");}
        if(!babysitter.getActivation()){
            throw new ApiException("BabySitter is not active");
        }

        // Fetch training details
        Course course = courseRepository.findCourseById(participation.getCourseId());
        if(course==null){
            throw new RuntimeException("Course not found");}

        // Add certificate to babysitter
        String currentCertificates = babysitter.getCertifications();
        if (currentCertificates == null || currentCertificates.isEmpty()) {
            babysitter.setCertifications(course.getTitle());
        } else if (!currentCertificates.contains(course.getTitle())) {
            babysitter.setCertifications(currentCertificates + "," + course.getTitle());
        }

        // Increase hourly rate by $10
        babysitter.setHourlyRate(babysitter.getHourlyRate() * 1.10);
        // Save babysitter updates
        babySitterRepository.save(babysitter);

        // Mark participation as certified
        participation.setStatus("certified");
        trainingParticipationRepository.save(participation);
    }


}


