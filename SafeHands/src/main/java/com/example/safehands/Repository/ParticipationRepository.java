package com.example.safehands.Repository;

import com.example.safehands.Model.TrainingParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<TrainingParticipation,Integer>{
    TrainingParticipation findTrainingParticipationById(Integer id);
    List<TrainingParticipation> findTrainingParticipationByStatus(String status);

    List<TrainingParticipation> findTrainingParticipationByCourseIdAndStatus(Integer courseId,String status);
}
