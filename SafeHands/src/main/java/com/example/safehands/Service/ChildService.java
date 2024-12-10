package com.example.safehands.Service;

import com.example.safehands.ApiResponse.ApiException;
import com.example.safehands.Model.Child;
import com.example.safehands.Repository.ChildRepository;
import com.example.safehands.Repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;
        private final ParentRepository parentRepository;


        // Get all children
        public List<Child> getAllChildren() {
            return childRepository.findAll();
        }


        // Create a new child
        public Child createChild(Child child) {
            // Validate Parent ID
            if (!parentRepository.existsById(child.getParentId())) {
                throw new RuntimeException("Parent with ID " + child.getParentId() + " does not exist.");
            }
            return childRepository.save(child);
        }

        // Update an existing child
        public Child updateChild(Integer id, Child updatedChild) {

            Child existingChild = childRepository.findChildById(id);
            if(existingChild==null){
                throw new ApiException("Child not found");
            }
            // Validate Parent ID
            if (!parentRepository.existsById(updatedChild.getParentId())) {
                throw new RuntimeException("Parent with ID " + updatedChild.getParentId() + " does not exist.");
            }

            existingChild.setParentId(updatedChild.getParentId());
            existingChild.setName(updatedChild.getName());
            existingChild.setAge(updatedChild.getAge());
            existingChild.setSpecialNeeds(updatedChild.getSpecialNeeds());
            existingChild.setHealthCondition(updatedChild.getHealthCondition());

            return childRepository.save(existingChild);
        }

        // Delete a child
        public void deleteChild(Integer id) {
            Child child = childRepository.findChildById(id);
            childRepository.delete(child);
        }
    }

