package com.example.safehands.Controller;

import com.example.safehands.ApiResponse.ApiResponse;
import com.example.safehands.Model.TrainingParticipation;
import com.example.safehands.Service.TrainingParticipationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participation")
public class TrainingParticipationController {
    private final TrainingParticipationService trainingParticipationService;


    @GetMapping("/get")
    public ResponseEntity getParticipation(){
        return ResponseEntity.status(200).body(trainingParticipationService.getTrainingParticipation());
    }
    @PostMapping("/add")
    public ResponseEntity addParticipation(@RequestBody @Valid TrainingParticipation trainingParticipation, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        trainingParticipationService.addTrainingParticipation(trainingParticipation);
        return ResponseEntity.status(200).body(new ApiResponse("Participation added"));

        }

        @PutMapping("/update/{id}")
        public ResponseEntity updateParticipation(@PathVariable Integer id,@RequestBody @Valid TrainingParticipation trainingParticipation,Errors errors){
            if(errors.hasErrors()){
                return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
            }
            trainingParticipationService.updateTrainingParticipation(id,trainingParticipation);
            return ResponseEntity.status(200).body(new ApiResponse("Participation updated"));
        }
        @DeleteMapping("/delete/{id}")
        public ResponseEntity deleteParticipation(@PathVariable Integer id){
        trainingParticipationService.deleteTrainingParticipation(id);
        return ResponseEntity.status(200).body(new ApiResponse("Participation deleted"));
        }


        @PutMapping("/certifyBabysitter/{participantId}")
        public ResponseEntity certifyBabysitter(@PathVariable Integer participantId ){
            trainingParticipationService.certifyBabysitter(participantId);
        return ResponseEntity.status(200).body(new ApiResponse("Certified successfully"));
        }



}
