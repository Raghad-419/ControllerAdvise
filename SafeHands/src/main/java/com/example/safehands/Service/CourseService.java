package com.example.safehands.Service;

import com.example.safehands.ApiResponse.ApiException;
import com.example.safehands.Model.Admin;
import com.example.safehands.Model.BabySitter;
import com.example.safehands.Model.Course;
import com.example.safehands.Model.TrainingParticipation;
import com.example.safehands.Repository.AdminRepository;
import com.example.safehands.Repository.BabySitterRepository;
import com.example.safehands.Repository.ParticipationRepository;
import com.example.safehands.Repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final BabySitterRepository babySitterRepository;
    private final ParticipationRepository participationRepository;
    private final AdminRepository adminRepository;

    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    public void addCourse(Integer adminId, Course course){
        Admin admin =adminRepository.findAdminById(adminId);
        if(admin==null){
            throw new ApiException("Admin not found");
        }
        courseRepository.save(course);

    }

    public void updateCourse(Integer courseId,Integer adminId ,Course course){
        Course oldCourse = courseRepository.findCourseById(courseId);
        if(adminRepository.findAdminById(adminId)==null){
            throw new ApiException("Admin not found");
        }
        if(oldCourse==null){
            throw new ApiException("Course not found");
        }
        oldCourse.setContent(course.getContent());
        oldCourse.setTitle(course.getTitle());
        oldCourse.setStartDate(course.getStartDate());
        oldCourse.setEndDate(course.getEndDate());

        courseRepository.save(oldCourse);
    }

    public void deleteCourse(Integer courseId, Integer adminId) {
        // Check if the admin exists
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("Admin not found.");
        }

        // Fetch the course by ID
        Course course = courseRepository.findCourseById(courseId);
        if (course == null) {
            throw new ApiException("Course not found.");
        }

        // Find all training participations with status 'registered' for the course
        List<TrainingParticipation> trainingParticipationList = participationRepository.findTrainingParticipationByCourseIdAndStatus(courseId, "registered");
        if (trainingParticipationList != null && !trainingParticipationList.isEmpty()) {
            // Delete all the registered participations
            for (TrainingParticipation trainingParticipation : trainingParticipationList) {
                participationRepository.delete(trainingParticipation);
            }
        }

        // Delete the course after removing the participations
        courseRepository.delete(course);
    }






}
