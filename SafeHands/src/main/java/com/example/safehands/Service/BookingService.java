package com.example.safehands.Service;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.safehands.ApiResponse.ApiException;
import com.example.safehands.Model.*;
import com.example.safehands.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
 public class BookingService {
    private final BookingRepository bookingRepository;
    private final ParentRepository parentRepository;
    private final BabySitterRepository babySitterRepository;
    private final ChildRepository childRepository;
    private final DiscountRepository discountRepository;


    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }


    // 3 Create a new booking
    public Double createBooking(Booking booking) {
        // Validate Parent
        if (parentRepository.findParentByid(booking.getParentId()) == null) {
            throw new RuntimeException("Parent with ID " + booking.getParentId() + " does not exist.");
        }

        // Validate BabySitter
        BabySitter babySitter = babySitterRepository.findBabySitterById(booking.getBabySitterId());
        if (babySitter == null) {
            throw new ApiException("BabySitter with ID " + booking.getBabySitterId() + " does not exist.");
        }
        if (!babySitter.getActivation()) {
            throw new ApiException("BabySitter not active");
        }

        // Validate Child IDs
        String[] childIds = booking.getChildIds().split(",");
        for (String childId : childIds) {
            Child child = childRepository.findChildById(Integer.parseInt(childId));
            if (child == null || !child.getParentId().equals(booking.getParentId())) {
                throw new ApiException("Child with ID " + childId + " does not belong to the parent with ID " + booking.getParentId());
            }
        }

        // Check for special needs children and babysitter certification
        for (String childId : childIds) {
            Child child = childRepository.findChildById(Integer.parseInt(childId));
            if (child != null && child.getSpecialNeeds() &&
                    !babySitter.getCertifications().contains("Special Needs Care")) {
                throw new ApiException("Babysitter does not have the required certification to care for children with special needs.");
            }
        }

        // Validate BabySitter availability
        if (!isBabySitterAvailable(booking.getBabySitterId(), booking.getStartTime(), booking.getRequestEndTime())) {
            throw new ApiException("Babysitter is not available");
        }

        // Calculate total price before saving
        Double totalPrice = calculateTotalPrice(booking, childIds);
        booking.setTotalPrice(totalPrice);

        // Save the booking to generate an ID
        booking.setStatus("Pending"); // Initial status
        bookingRepository.save(booking);

        // Apply discount if a discount code is provided
        if (booking.getDiscountCode() != null && !booking.getDiscountCode().isEmpty()) {
            try {
                applyDiscountToBooking(booking.getId(), booking.getDiscountCode());
                booking = bookingRepository.findBookingById(booking.getId()); // Reload the updated booking
                totalPrice = booking.getTotalPrice(); // Update total price after discount
            } catch (ApiException e) {
                System.out.println("Discount application failed: " + e.getMessage());
            }
        }

        // Log the final price
        System.out.println("Total Price After Discount (if applied): " + totalPrice);
        return totalPrice;
    }


    //4 Update an existing booking
    public void updateBooking(Integer id, Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findBookingById(id);
        if(existingBooking==null){
            throw new ApiException("Booking with ID " + bookingRepository.findBookingById(id) + " does not exist.");
        }
        // Validate Parent
        if (parentRepository.findParentByid(updatedBooking.getParentId()) ==null) {
            throw new ApiException("Parent with ID " + updatedBooking.getParentId() + " does not exist.");
        }

        // Validate BabySitter
        BabySitter babySitter=babySitterRepository.findBabySitterById(updatedBooking.getBabySitterId());
        if (babySitter==null) {
            throw new ApiException("BabySitter with ID " + updatedBooking.getBabySitterId() + " does not exist.");
        }
        if(!babySitter.getActivation()){
            throw new ApiException("BabySitter not active");
        }


        // Fetch all children for the parent
        List<Child> children = childRepository.findChildByParentId(updatedBooking.getParentId());
        if (children.isEmpty()) {
            throw new ApiException("No children found for parent with ID " + updatedBooking.getParentId());
        }

        // Validate Child IDs
        String[] childIds = updatedBooking.getChildIds().split(",");
        for (String childId : childIds) {
            Child child = childRepository.findChildById(Integer.parseInt(childId));
            if (child == null || !child.getParentId().equals(updatedBooking.getParentId())) {
                throw new ApiException("Child with ID " + childId + " does not belong to the parent with ID " + updatedBooking.getParentId());
            }
        }

        // Check for special needs children and babysitter certification
        for(Child child:children){
            if(child.getSpecialNeeds()){
                if(!babySitter.getCertifications().contains("Special Needs Care")){
                    throw new ApiException("Babysitter does not have the required certification to care for children with special needs.");
                }
            }
        }
        // Validate BabySitter availability
        Boolean babySitterAvailability = isBabySitterAvailable(updatedBooking.getBabySitterId(),updatedBooking.getStartTime(),updatedBooking.getRequestEndTime());
        if(!babySitterAvailability){
            throw new ApiException("Babysitter is not available");
        }


        existingBooking.setParentId(updatedBooking.getParentId());
        existingBooking.setBabySitterId(updatedBooking.getBabySitterId());
        existingBooking.setHours(updatedBooking.getHours());
        existingBooking.setTotalPrice(calculateTotalPrice(updatedBooking,childIds));
        existingBooking.setStatus(updatedBooking.getStatus());
        existingBooking.setStartTime(updatedBooking.getStartTime());
        existingBooking.setRequestEndTime(updatedBooking.getRequestEndTime());
        existingBooking.setRecurring(updatedBooking.isRecurring());
        existingBooking.setDiscountPercentage(updatedBooking.getDiscountPercentage());
        existingBooking.setPaymentStatus(updatedBooking.getPaymentStatus());
        existingBooking.setRecurrenceEndDate(updatedBooking.getRecurrenceEndDate());
        existingBooking.setRecurrenceFrequency(updatedBooking.getRecurrenceFrequency());

         bookingRepository.save(existingBooking);
    }

    //5 delete booking and calculate fee
    public void deleteBooking(Integer id) {
        Booking booking = bookingRepository.findBookingById(id);

        if (booking == null) {
            throw new ApiException("Booking not found.");
        }

        Parent parent = parentRepository.findParentByid(booking.getParentId());
        if (parent == null) {
            throw new ApiException("Parent not found.");
        }

        BabySitter babySitter = babySitterRepository.findBabySitterById(booking.getBabySitterId());
        if (babySitter == null) {
            throw new ApiException("Babysitter not found.");
        }

        // Calculate time difference between cancellation and the start time
        long hoursUntilStart = Duration.between(LocalDateTime.now(), booking.getStartTime()).toHours();

        Double totalRefund = 0.0;
        Double cancellationFee = 0.0;

        // If it's a recurring booking, handle all associated bookings
        if (Boolean.TRUE.equals(booking.isRecurring())) {
            List<Booking> recurringBookings = bookingRepository.findAllByParentIdAndRecurrenceGroup(
                    booking.getParentId(), booking.getRecurrenceGroup()
            );

            for (Booking recurringBooking : recurringBookings) {
                // Apply cancellation policy if within the cancellation period
                if (hoursUntilStart < babySitter.getFreeCancellationPeriod()) {
                    cancellationFee += recurringBooking.getTotalPrice() * (babySitter.getCancellationFeePercentage() / 100);
                } else {
                    totalRefund += recurringBooking.getTotalPrice();
                }

                // Delete the booking
                bookingRepository.delete(recurringBooking);
            }
        } else {
            // Handle single booking cancellation
            if (hoursUntilStart < babySitter.getFreeCancellationPeriod()) {
                cancellationFee += booking.getTotalPrice() * (babySitter.getCancellationFeePercentage() / 100);
            } else {
                totalRefund += booking.getTotalPrice();
            }

            // Delete the single booking
            bookingRepository.delete(booking);
        }

        // Process refund and fee adjustment
        Double netRefund = totalRefund - cancellationFee;
        if (netRefund > 0) {
            parent.setBalance(parent.getBalance() + netRefund);
        } else {
            parent.setBalance(parent.getBalance() - cancellationFee);
        }

        parentRepository.save(parent);

        // Log the refund and cancellation fee
        System.out.println("Total refund issued: " + totalRefund);
        System.out.println("Total cancellation fee applied: " + cancellationFee);
    }



    // Calculate total price
    private Double calculateTotalPrice(Booking booking, String[] childIds) {
        Double baseRate = 50.0; // base rate per hour
        Double childRate = 10.0; // additional rate per child
        Double specialNeedsRate = 1.05; // 5% surcharge for special needs children

        // Fetch the specified children based on the provided child IDs
        List<Child> children = new ArrayList<>();
        for (String childId : childIds) {
            Child child = childRepository.findChildById(Integer.parseInt(childId));
            if (child != null && child.getParentId().equals(booking.getParentId())) {
                children.add(child);
            }
        }

        // Check if any child has special needs
        Boolean hasSpecialNeeds = false;
        for (Child child : children) {
            if (child.getSpecialNeeds()) {
                hasSpecialNeeds = true;
                break; // No need to check further if one child with special needs is found
            }
        }

        // Calculate total price
        Double totalPrice = baseRate * booking.getHours() + childRate * children.size();

        // Apply 5% surcharge if any child has special needs
        if (hasSpecialNeeds) {
            totalPrice *= specialNeedsRate;
        }

        return totalPrice;
    }

    //6 method to check Availability of babySitter
    public Boolean isBabySitterAvailable(Integer babySitterId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> existingBookings = bookingRepository.findAllByBabySitterIdAndStatus(babySitterId, "Confirmed");

        for (Booking existingBooking : existingBookings) {
            if ((startTime.isBefore(existingBooking.getRequestEndTime()) && endTime.isAfter(existingBooking.getStartTime()))) {
                return false; // Babysitter is not available
            }

            // If the booking is recurring, check conflicts for the recurrence period
            if (Boolean.TRUE.equals(existingBooking.isRecurring())) {
                LocalDateTime recurringStart = existingBooking.getStartTime();
                LocalDateTime recurringEnd = existingBooking.getRequestEndTime();
                LocalDateTime recurrenceEndDate = existingBooking.getRecurrenceEndDate();

                while (recurringStart.isBefore(recurrenceEndDate)) {
                    if (startTime.isBefore(recurringEnd) && endTime.isAfter(recurringStart)) {
                        return false; // Babysitter is not available
                    }

                    // Adjust dates for the next recurrence
                    switch (existingBooking.getRecurrenceFrequency()) {
                        case "DAILY":
                            recurringStart = recurringStart.plusDays(1);
                            recurringEnd = recurringEnd.plusDays(1);
                            break;
                        case "WEEKLY":
                            recurringStart = recurringStart.plusWeeks(1);
                            recurringEnd = recurringEnd.plusWeeks(1);
                            break;
                        case "MONTHLY":
                            recurringStart = recurringStart.plusMonths(1);
                            recurringEnd = recurringEnd.plusMonths(1);
                            break;
                    }
                }
            }
        }
        return true;// Babysitter is available
    }

//7 get Parent Booking History
    public List<Booking> getParentBookingHistory(Integer parentId){
        return bookingRepository.findBookingByParentIdAndStatus(parentId,"Completed");
    }
//8 Babysitter Booking History
    public List<Booking> getBabysitterBookingHistory(Integer babysitterId) {
        return bookingRepository.findBookingByBabySitterIdAndStatus(babysitterId,"Completed");
    }


  //9 method to complete payment method
    public void paymentProcess(Integer bookingId) {
        // Fetch the booking
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null) {
            throw new ApiException("Booking not found.");
        }

        // Check if the booking has already been paid
        if ("Completed".equalsIgnoreCase(booking.getPaymentStatus())) {
            throw new ApiException("This booking has already been paid.");
        }

        Double totalPrice = 0.0;

        // Calculate total price for recurring or single bookings
        if (Boolean.TRUE.equals(booking.isRecurring())) {
            LocalDateTime currentStartTime = booking.getStartTime();
            LocalDateTime currentEndTime = booking.getRecurrenceEndDate();
            long totalSessions = calculateNumberOfSessions(currentStartTime, currentEndTime, booking.getRecurrenceFrequency());

            List<Booking> recurringBookings = bookingRepository.findAllByParentIdAndBabySitterIdAndRecurrenceFrequency(
                    booking.getParentId(),
                    booking.getBabySitterId(),
                    booking.getRecurrenceFrequency()
            );

            if (recurringBookings == null || recurringBookings.isEmpty()) {
                throw new ApiException("No recurring bookings found to process payment.");
            }

            // Calculate total price for all sessions
            for (Booking session : recurringBookings) {
                totalPrice += session.getTotalPrice();
            }
        } else {
            // For single bookings, use the booking's total price
            totalPrice = booking.getTotalPrice();
        }

        // Check if a discount code is provided
        String discountCode = booking.getDiscountCode();
        if (discountCode != null && !discountCode.isEmpty()) {
            Discount discount = discountRepository.findDiscountByCode(discountCode);

            if (discount == null) {
                throw new ApiException("Invalid discount code.");
            }

            if (discount.getExpirationDate().isBefore(LocalDateTime.now())) {
                throw new ApiException("The discount code has expired.");
            }

            // Apply discount (percentage or flat rate)
            if (discount.getAmount() <= 1) {
                // Percentage discount
                totalPrice -= totalPrice * discount.getAmount();
            } else {
                // Flat rate discount
                totalPrice -= discount.getAmount();
            }

            // Ensure the total price does not drop below zero
            totalPrice = Math.max(totalPrice, 0.0);
        }

        // Fetch the parent and validate balance
        Parent parent = parentRepository.findParentByid(booking.getParentId());
        if (parent == null) {
            throw new ApiException("Parent not found for booking ID: " + bookingId);
        }

        // Log the parent balance before deduction for debugging
        System.out.println("Parent balance before payment: " + parent.getBalance());
        System.out.println("Total price to be deducted: " + totalPrice);

        // Check if the parent has sufficient balance
        if (parent.getBalance() < totalPrice) {
            throw new ApiException("Insufficient balance. Payment cannot be processed.");
        }

        // Deduct the amount from the parent's balance
        parent.setBalance(parent.getBalance() - totalPrice);
        parentRepository.save(parent);

        // Update the status and payment status of all relevant bookings
        if (Boolean.TRUE.equals(booking.isRecurring())) {
            List<Booking> recurringBookings = bookingRepository.findAllByParentIdAndBabySitterIdAndRecurrenceFrequency(
                    booking.getParentId(),
                    booking.getBabySitterId(),
                    booking.getRecurrenceFrequency()
            );

            for (Booking session : recurringBookings) {
                session.setStatus("Confirmed");
                session.setPaymentStatus("Completed");
                bookingRepository.save(session);
            }
        } else {
            // For single booking, update its status and payment status
            booking.setStatus("Confirmed");
            booking.setPaymentStatus("Completed");
            bookingRepository.save(booking);
        }

        // Log the parent balance after deduction for debugging
        Parent updatedParent = parentRepository.findParentByid(booking.getParentId());
        System.out.println("Parent balance after payment: " + updatedParent.getBalance());

        System.out.println("Payment processed successfully. Total Amount: " + totalPrice);
    }


    // 10 Scheduled task to update booking status
    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void updateBookingStatuses() {
        // Fetch bookings with a status of "Confirmed" and endTime in the past
        List<Booking> bookings = bookingRepository.findBookingByStatusAndRequestEndTimeBefore("Confirmed", LocalDateTime.now());

        for (Booking booking : bookings) {
            booking.setStatus("Completed");
            bookingRepository.save(booking);
            System.out.println("Booking ID " + booking.getId() + " marked as Completed.");
        }
    }
// method to get Available BabySitters
    public List<BabySitter> getAvailableBabySitters( LocalDateTime startTime, LocalDateTime endTime){
        List<BabySitter> babySitterList = babySitterRepository.findAllByActivationTrue();
        List<BabySitter> availableSitters =new ArrayList<>();
        for(BabySitter babySitter: babySitterList){
            if(isBabySitterAvailable(babySitter.getId(),startTime,endTime)){
                availableSitters.add(babySitter);
            }
        }
        return availableSitters;
    }

//11 method to get Upcoming Bookings Of BabySitter
    public List<Booking> getUpcomingBookingOfBabySitter(Integer babySitterId){
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findAllByBabySitterIdAndStartTimeAfterAndStatusNot(babySitterId,now ,"Completed");
    }
//12 method to get Upcoming Bookings Of parent
    public List<Booking> getUpcomingBookingOfParent(Integer parentId){
        LocalDateTime now =LocalDateTime.now();
        return bookingRepository.findAllByParentIdAndStartTimeAfterAndStatusNot(parentId,now,"Completed");
    }

//
////13 create Recurring Booking
    public Double createRecurringBooking(Booking booking) {
        // If the booking is not recurring, treat it as a single booking
        if (!booking.isRecurring()) {
            return createBooking(booking);
        }

        // Validate recurrence details
        validateRecurringBooking(booking);

        // Initialize time and total price accumulator
        LocalDateTime currentStartTime = booking.getStartTime();
        LocalDateTime currentEndTime = booking.getRequestEndTime();
        long totalSessions = calculateNumberOfSessions(
                currentStartTime,
                booking.getRecurrenceEndDate(),
                booking.getRecurrenceFrequency()
        );

        Double totalPrice = 0.0;

        // Process each session in the recurrence
        for (long i = 0; i < totalSessions; i++) {
            // Check babysitter availability for each session
            if (!isBabySitterAvailable(booking.getBabySitterId(), currentStartTime, currentEndTime)) {
                throw new ApiException("Babysitter is unavailable for one or more recurring sessions.");
            }

            // Create a single session booking
            Booking recurringBooking = createSingleSessionBooking(booking, currentStartTime, currentEndTime);
            bookingRepository.save(recurringBooking);

            // Apply discount if a discount code is provided
            String discountCode = booking.getDiscountCode();
            if (discountCode != null && !discountCode.isEmpty()) {
                applyDiscountToBooking(recurringBooking.getId(), discountCode);
                recurringBooking = bookingRepository.findBookingById(recurringBooking.getId()); // Refresh to get updated price
            }

            // Accumulate the total price
            totalPrice += recurringBooking.getTotalPrice();

            // Update start and end times for the next session
            LocalDateTime[] nextTimes = getNextSessionTime(
                    currentStartTime,
                    currentEndTime,
                    booking.getRecurrenceFrequency()
            );
            currentStartTime = nextTimes[0];
            currentEndTime = nextTimes[1];
        }

        return totalPrice;
    }


    private LocalDateTime[] getNextSessionTime(LocalDateTime currentStartTime, LocalDateTime currentEndTime, String frequency) {
        switch (frequency) {
            case "DAILY":
                return new LocalDateTime[]{currentStartTime.plusDays(1), currentEndTime.plusDays(1)};
            case "WEEKLY":
                return new LocalDateTime[]{currentStartTime.plusWeeks(1), currentEndTime.plusWeeks(1)};
            case "MONTHLY":
                return new LocalDateTime[]{currentStartTime.plusMonths(1), currentEndTime.plusMonths(1)};
            default:
                throw new ApiException("Invalid recurrence frequency. Allowed values: DAILY, WEEKLY, MONTHLY.");
        }
    }


    private Booking createSingleSessionBooking(Booking original, LocalDateTime start, LocalDateTime end) {
        Booking session = new Booking();
        session.setParentId(original.getParentId());
        session.setBabySitterId(original.getBabySitterId());
        session.setHours(original.getHours());
        session.setStartTime(start);
        session.setRequestEndTime(end);
        session.setRecurring(true);
        session.setRecurrenceFrequency(original.getRecurrenceFrequency());
        session.setRecurrenceEndDate(original.getRecurrenceEndDate());
        session.setChildIds(original.getChildIds());
        session.setTotalPrice(calculateTotalPrice(session, original.getChildIds().split(",")));
        session.setStatus("Pending");
        return session;
    }


    private void validateRecurringBooking(Booking booking) {
        // Check if the booking is marked as recurring
        if (!booking.isRecurring()) {
            throw new ApiException("The booking is not marked as recurring.");
        }

        // Validate recurrence frequency
        if (booking.getRecurrenceFrequency() == null ||
                !booking.getRecurrenceFrequency().matches("DAILY|WEEKLY|MONTHLY")) {
            throw new ApiException("Invalid recurrence frequency. Allowed values are DAILY, WEEKLY, or MONTHLY.");
        }

        // Validate recurrence end date
        if (booking.getRecurrenceEndDate() == null || booking.getRecurrenceEndDate().isBefore(booking.getStartTime())) {
            throw new ApiException("Recurring bookings require a valid end date that is after the start date.");
        }

        // Validate parent
        if (parentRepository.findParentByid(booking.getParentId()) == null) {
            throw new ApiException("Parent with ID " + booking.getParentId() + " does not exist.");
        }

        // Validate babysitter
        BabySitter babySitter = babySitterRepository.findBabySitterById(booking.getBabySitterId());
        if (babySitter == null) {
            throw new ApiException("Babysitter with ID " + booking.getBabySitterId() + " does not exist.");
        }
        if(!babySitter.getActivation()){
            throw new ApiException("BabySitter not active");
        }

        // Validate child IDs
        String[] childIds = booking.getChildIds().split(",");
        for (String childId : childIds) {
            Child child = childRepository.findChildById(Integer.parseInt(childId));
            if (child == null || !child.getParentId().equals(booking.getParentId())) {
                throw new ApiException("Child with ID " + childId + " does not belong to parent with ID " + booking.getParentId());
            }
        }

        // Validate babysitter certifications for special needs children
        for (String childId : childIds) {
            Child child = childRepository.findChildById(Integer.parseInt(childId));
            if (child != null && child.getSpecialNeeds() &&
                    !babySitter.getCertifications().contains("Special Needs Care")) {
                throw new ApiException("Babysitter does not have the required certification to care for children with special needs.");
            }
        }

        // Validate babysitter availability for the first session
        if (!isBabySitterAvailable(booking.getBabySitterId(), booking.getStartTime(), booking.getRequestEndTime())) {
            throw new ApiException("Babysitter is unavailable for the initial session.");
        }
    }


    // Calculate the number of sessions
    private long calculateNumberOfSessions(LocalDateTime startTime, LocalDateTime endDate, String frequency) {
        long sessions = 0;
        switch (frequency) {
            case "DAILY":
                sessions = ChronoUnit.DAYS.between(startTime, endDate);
                break;
            case "WEEKLY":
                sessions = ChronoUnit.WEEKS.between(startTime, endDate);
                break;
            case "MONTHLY":
                sessions = ChronoUnit.MONTHS.between(startTime, endDate);
                break;
            default:
                throw new ApiException("Invalid recurrence frequency. Allowed values: DAILY, WEEKLY, MONTHLY.");
        }

        // Ensure at least one session occurs
        return sessions > 0 ? sessions : 1;
    }

    // 14 method to apply discount
    public void applyDiscountToBooking(Integer bookingId, String discountCode) {
        // Fetch the booking
        Booking booking = bookingRepository.findBookingById(bookingId);

        if (booking == null) {
            throw new ApiException("Booking with ID " + bookingId + " does not exist.");
        }

        // Fetch the discount based on the provided discount code
        Discount discount = discountRepository.findDiscountByCode(discountCode);

        if (discount == null) {
            throw new ApiException("Invalid discount code. Code not found.");
        }

        // Validate discount expiration
        if (discount.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ApiException("The discount code has expired.");
        }

        // Extract childIds from the booking
        String[] childIds = booking.getChildIds().split(",");

        // Calculate the original total price
        Double originalPrice = calculateTotalPrice(booking, childIds);

        // Calculate the discount amount
        Double discountAmount;
        if (discount.getAmount() <= 1) {
            // If the discount amount is a percentage (e.g., 0.1 for 10%)
            discountAmount = originalPrice * discount.getAmount();
        } else {
            // If the discount amount is a flat rate (e.g., $10)
            discountAmount = discount.getAmount();
        }

        // Ensure the discount does not exceed the total price
        if (discountAmount > originalPrice) {
            throw new ApiException("Discount amount cannot exceed the total price.");
        }

        // Calculate the new total price
        Double newTotalPrice = originalPrice - discountAmount;

        // Apply the discount and update the booking
        booking.setDiscountPercentage(discount.getAmount() * 100); // Save percentage
        booking.setTotalPrice(newTotalPrice);

        // Save the updated booking
        bookingRepository.save(booking);
    }




}


