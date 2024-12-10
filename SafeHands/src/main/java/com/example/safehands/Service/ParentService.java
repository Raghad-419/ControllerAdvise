package com.example.safehands.Service;

import com.example.safehands.ApiResponse.ApiException;
import com.example.safehands.Model.Booking;
import com.example.safehands.Model.Parent;
import com.example.safehands.Repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentService {

        private final ParentRepository parentRepository;


        // Get all parents
        public List<Parent> getAllParents() {
            return parentRepository.findAll();
        }

        // Add a new parent
        public void addParent(Parent parent) {
            parentRepository.save(parent);
        }

        // Update an existing parent
        public void updateParent(Integer id, Parent updatedParent) {
            Parent existingParent = parentRepository.findParentByid(id);
            if(existingParent==null){
                throw new ApiException("Parent not found");
            }
            existingParent.setName(updatedParent.getName());
            existingParent.setEmail(updatedParent.getEmail());
            existingParent.setPassword(updatedParent.getPassword());
            existingParent.setLocation(updatedParent.getLocation());
            existingParent.setBalance(updatedParent.getBalance());
            parentRepository.save(existingParent);
        }

        // Delete a parent
        public void deleteParent(Integer id) {
            Parent parent = parentRepository.findParentByid(id);
            if(parent==null){
                throw new ApiException("Parent not found");

            }
            parentRepository.delete(parent);
        }



}
