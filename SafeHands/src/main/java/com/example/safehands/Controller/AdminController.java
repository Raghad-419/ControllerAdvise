package com.example.safehands.Controller;

import com.example.safehands.ApiResponse.ApiResponse;
import com.example.safehands.Model.Admin;
import com.example.safehands.Service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;


    // Get all admins
    @GetMapping("/get")
    public ResponseEntity getAllAdmins() {
        return ResponseEntity.status(200).body(adminService.getAllAdmins());
    }


    // Create a new admin
    @PostMapping("/add")
    public ResponseEntity createAdmin(@RequestBody @Valid Admin admin) {

         adminService.createAdmin(admin);
        return ResponseEntity.status(200).body(new ApiResponse("Admin added"));
    }

    // Update an admin
    @PutMapping("/update/{id}")
    public ResponseEntity updateAdmin(@PathVariable Integer id, @Valid @RequestBody Admin updatedAdmin) {
       adminService.updateAdmin(id, updatedAdmin);
        return ResponseEntity.status(200).body(new ApiResponse("Admin updated"));
    }

    // Delete an admin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteAdmin(@PathVariable Integer id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.status(200).body(new ApiResponse("Admin deleted successfully"));
    }
@PutMapping("/activateBabySitter/{adminId}/{babySitterId}")
    public ResponseEntity activateBabySitter(@PathVariable Integer adminId,@PathVariable Integer babySitterId){
        adminService.activateBabySitter(adminId,babySitterId);
        return ResponseEntity.status(200).body(new ApiResponse("babySitter activate"));
    }
}
