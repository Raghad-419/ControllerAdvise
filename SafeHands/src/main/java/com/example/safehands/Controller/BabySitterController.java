package com.example.safehands.Controller;

import com.example.safehands.ApiResponse.ApiResponse;
import com.example.safehands.Model.BabySitter;
import com.example.safehands.Service.BabySitterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/babySitter")
public class BabySitterController {
    private final BabySitterService babySitterService;




    // Get all babysitters
    @GetMapping("/get")
    public ResponseEntity getAllBabySitters() {
        return ResponseEntity.status(200).body(babySitterService.getAllBabySitters());
    }


    // Create a new babysitter
    @PostMapping("/add")
    public ResponseEntity addBabySitter(@RequestBody @Valid BabySitter babySitter) {
        babySitterService.addBabySitter(babySitter);
        return ResponseEntity.status(200).body(new ApiResponse("BabySitter added"));
    }

    // Update an existing babysitter
    @PutMapping("/update/{id}")
    public ResponseEntity updateBabySitter(@PathVariable Integer id, @RequestBody @Valid BabySitter updatedBabySitter) {
        babySitterService.updateBabySitter(id, updatedBabySitter);
        return ResponseEntity.status(200).body(new ApiResponse("BabySitter updated"));
    }

    // Delete a babysitter
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBabySitter(@PathVariable Integer id) {
        babySitterService.deleteBabySitter(id);
        return ResponseEntity.ok("BabySitter deleted successfully");
    }

@PutMapping("/calculateAverageRating/{id}")
public ResponseEntity calculateAverageRating(@PathVariable Integer id){
        return ResponseEntity.status(200).body(babySitterService.calculateAverageRating(id));
}

@GetMapping("/findTopBabySitters/{numberOfSitters}")
public ResponseEntity findTopBabySitters(@PathVariable Integer numberOfSitters){
        return ResponseEntity.status(200).body(babySitterService.findTopBabySitters(numberOfSitters));
}

}

