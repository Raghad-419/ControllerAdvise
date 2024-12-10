package com.example.safehands.Controller;

import com.example.safehands.ApiResponse.ApiResponse;
import com.example.safehands.Model.Parent;
import com.example.safehands.Service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parent")
public class ParentController {
    private final ParentService parentService;



    // Get all parents
    @GetMapping("/get")
    public ResponseEntity getAllParents() {
        List<Parent> parents = parentService.getAllParents();
        return ResponseEntity.status(200).body(parents);
    }


    // Create a new parent
    @PostMapping("/add")
    public ResponseEntity addParent(@RequestBody @Valid Parent parent) {
        parentService.addParent(parent);
        return ResponseEntity.status(200).body(new ApiResponse("Parent added"));
    }

    // Update an existing parent
    @PutMapping("/update/{id}")
    public ResponseEntity updateParent(@PathVariable Integer id, @Valid @RequestBody Parent updatedParent) {
         parentService.updateParent(id, updatedParent);
        return ResponseEntity.status(200).body(new ApiResponse("Parent updated"));
    }

    // Delete a parent
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteParent(@PathVariable Integer id) {
        parentService.deleteParent(id);
        return ResponseEntity.status(200).body("Parent deleted successfully");
    }



}
