package com.example.safehands.Controller;

import com.example.safehands.ApiResponse.ApiResponse;
import com.example.safehands.Model.Booking;
import com.example.safehands.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

    // Get all bookings
    @GetMapping("/get")
    public ResponseEntity getAllBookings() {
        return ResponseEntity.status(200).body(bookingService.getAllBookings());
    }


    @PostMapping("/add")
    public ResponseEntity createRecurringBooking(@Valid @RequestBody Booking booking) {

        return ResponseEntity.status(200).body(new ApiResponse("The booking has been added. To confirm it, please complete the payment. Total price= "+bookingService.createRecurringBooking(booking)));
    }

    // Update an existing booking
    @PutMapping("/update/{id}")
    public ResponseEntity updateBooking(@PathVariable Integer id, @Valid @RequestBody Booking updatedBooking) {
        bookingService.updateBooking(id, updatedBooking);
        return ResponseEntity.status(200).body(new ApiResponse("Booking updated"));
    }

    // Delete a booking
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.status(200).body(new ApiResponse("Booking deleted successfully"));
    }

    @PutMapping("/payment/{bookingId}")
    public ResponseEntity paymentProcess(@PathVariable Integer bookingId){

        bookingService.paymentProcess(bookingId);
        return ResponseEntity.status(200).body(new ApiResponse("Booking confirmed"));
    }

    @PutMapping("/applyDiscountToBooking/{bookingId}/{discountCode}")
    public ResponseEntity applyDiscountToBooking(@PathVariable Integer bookingId,@PathVariable String discountCode){
        bookingService.applyDiscountToBooking(bookingId,discountCode);
        return ResponseEntity.status(200).body(new ApiResponse("Discount appalled"));
    }

    @GetMapping("/getParentBookingHistory/{parentId}")
    public ResponseEntity getParentBookingHistory(@PathVariable Integer parentId){
        return ResponseEntity.status(200).body(bookingService.getParentBookingHistory(parentId));
    }
    @GetMapping("getBabySitterBookingHistory/{babySitterId}")
    public ResponseEntity getBabySitterBookingHistory(@PathVariable Integer babySitterId){
        return ResponseEntity.status(200).body(bookingService.getBabysitterBookingHistory(babySitterId));
    }

    @GetMapping("/getAvailableBabySitters/{startTime}/{endTime}")
    public ResponseEntity getAvailableBabySitters(@PathVariable LocalDateTime startTime ,@PathVariable LocalDateTime endTime){
        return ResponseEntity.status(200).body(bookingService.getAvailableBabySitters(startTime,endTime));
    }

    @GetMapping("/getUpcomingBookingOfBabySitter/{babySitterId}")
public ResponseEntity getUpcomingBookingOfBabySitter(@PathVariable Integer babySitterId){
        return ResponseEntity.status(200).body(bookingService.getUpcomingBookingOfBabySitter(babySitterId));
}

@GetMapping("/getUpcomingBookingOfParent/{parentId}")
public ResponseEntity getUpcomingBookingOfParent(@PathVariable Integer parentId){
        return ResponseEntity.status(200).body(bookingService.getUpcomingBookingOfParent(parentId));
}





}
