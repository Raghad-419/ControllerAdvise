package com.example.safehands.Service;

import com.example.safehands.ApiResponse.ApiException;
import com.example.safehands.Model.Admin;
import com.example.safehands.Model.BabySitter;
import com.example.safehands.Repository.AdminRepository;
import com.example.safehands.Repository.BabySitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final BabySitterRepository babySitterRepository;



    // Get all admins
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // Create a new admin
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    // Update an admin
    public Admin updateAdmin(Integer id, Admin updatedAdmin) {
        Admin existingAdmin = adminRepository.findAdminById(id);
        existingAdmin.setName(updatedAdmin.getName());
        existingAdmin.setEmail(updatedAdmin.getEmail());
        existingAdmin.setPassword(updatedAdmin.getPassword());
        return adminRepository.save(existingAdmin);
    }

    // Delete an admin
    public void deleteAdmin(Integer id) {
        Admin admin = adminRepository.findAdminById(id);
        adminRepository.delete(admin);
    }


    public void activateBabySitter(Integer adminId, Integer babySitterId){
        BabySitter babySitter= babySitterRepository.findBabySitterById(babySitterId);
        if(babySitter==null){
            throw new ApiException("BabySitter not found");
        }

        Admin admin =adminRepository.findAdminById(adminId);
        if(admin==null){
            throw new ApiException("admin not found");
        }

        if(!babySitter.getActivation()){
            babySitter.setActivation(true);
            babySitterRepository.save(babySitter);
        }else throw new ApiException("babySitter is already activate");
    }

}
