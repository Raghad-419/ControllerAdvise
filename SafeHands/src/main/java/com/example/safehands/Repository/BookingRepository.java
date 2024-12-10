package com.example.safehands.Repository;

import com.example.safehands.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    Booking findBookingById(Integer id);

   Boolean existsByParentIdAndBabySitterIdAndStatus(Integer parentId,Integer babySitterId,String status);

   List<Booking> findAllByBabySitterIdAndStatus(Integer babySitterId, String status);

   List<Booking> findBookingByParentIdAndStatus(Integer parentIId , String status);

   List<Booking> findBookingByBabySitterIdAndStatus(Integer parentIId , String status);

   List<Booking> findBookingByStatusAndRequestEndTimeBefore(String status,LocalDateTime dateTime);

   List<Booking> findAllByBabySitterIdAndStartTimeAfterAndStatusNot(Integer babySitterId,LocalDateTime now ,String status);

   List<Booking> findAllByParentIdAndStartTimeAfterAndStatusNot(Integer parentId,LocalDateTime now ,String status);

   List<Booking> findAllByParentIdAndBabySitterIdAndRecurrenceFrequency(Integer parentId, Integer babySitterId, String recurrenceFrequency);

   List<Booking> findAllByParentIdAndRecurrenceGroup(Integer parentId, String recurrenceGroup);


}
