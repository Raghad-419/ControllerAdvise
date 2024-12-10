package com.example.safehands.Repository;

import com.example.safehands.Model.BabySitter;
import com.example.safehands.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BabySitterRepository extends JpaRepository<BabySitter,Integer> {
    BabySitter findBabySitterById(Integer id);

    List<BabySitter> findAllByActivationTrue();



}
