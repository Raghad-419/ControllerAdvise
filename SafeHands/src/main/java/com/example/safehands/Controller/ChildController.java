package com.example.safehands.Controller;

import com.example.safehands.ApiResponse.ApiResponse;
import com.example.safehands.Model.Child;
import com.example.safehands.Service.ChildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/child")
public class ChildController {

        private final ChildService childService;

        // Get all children
        @GetMapping("/get")
        public ResponseEntity getAllChildren() {
            return ResponseEntity.status(200).body(childService.getAllChildren());
        }


        // Create a new child
        @PostMapping("/add")
        public ResponseEntity createChild(@Valid @RequestBody Child child) {

            childService.createChild(child);
            return ResponseEntity.status(200).body(new ApiResponse("Child added"));
        }


        // Update an existing child
        @PutMapping("/update/{id}")
        public ResponseEntity updateChild(@PathVariable Integer id, @Valid @RequestBody Child updatedChild) {
            childService.updateChild(id, updatedChild);
            return ResponseEntity.status(200).body(new ApiResponse("child updated"));
        }

        // Delete a child
        @DeleteMapping("/delete/{id}")
        public ResponseEntity deleteChild(@PathVariable Integer id) {
            childService.deleteChild(id);
            return ResponseEntity.status(200).body("Child deleted successfully");
        }
    }


