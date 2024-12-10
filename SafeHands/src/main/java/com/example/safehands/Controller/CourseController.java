package com.example.safehands.Controller;

import com.example.safehands.ApiResponse.ApiResponse;
import com.example.safehands.Model.Course;
import com.example.safehands.Service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/get")
    public ResponseEntity getAllTraining(){
        return ResponseEntity.status(200).body(courseService.getAllCourses());
    }

    @PostMapping("/add/{adminId}")
    public ResponseEntity addTraining(@PathVariable Integer adminId,@RequestBody @Valid Course course){
        courseService.addCourse(adminId,course);
        return ResponseEntity.status(200).body(new ApiResponse("Course added"));
    }

    @PutMapping("/update/{courseId}/{adminId}")
    public ResponseEntity updateTraining(@PathVariable Integer courseId,@PathVariable Integer adminId, @RequestBody @Valid Course course){
        courseService.updateCourse(courseId,adminId,course);
        return ResponseEntity.status(200).body(new ApiResponse("Course updated"));

    }

    @DeleteMapping("/delete/{courseId}/{adminId}")
    public ResponseEntity deleteTraining(@PathVariable Integer courseId,@PathVariable Integer adminId){
        courseService.deleteCourse(courseId,adminId);
        return ResponseEntity.status(200).body(new ApiResponse("Course deleted"));
    }




}
