package com.example.safehands.Repository;

import com.example.safehands.Model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent,Integer> {

    Parent findParentByid(Integer id);

    Parent findParentByEmailAndPassword(String email,String password);
}
