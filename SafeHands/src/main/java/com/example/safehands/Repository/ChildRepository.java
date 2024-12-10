package com.example.safehands.Repository;

import com.example.safehands.Model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<Child,Integer> {
    Child findChildById(Integer id);
    List<Child> findChildByParentId(Integer parentId);
}
