package com.example.safehands.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Check(constraints = "(status = 'Pending' AND payment_status = 'Pending') OR " +
        "((status = 'Confirmed' OR status = 'Completed') AND payment_status = 'Completed')")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty parent id")
    @Positive(message = "Parent Id should be positive")
    private Integer parentId;
    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty baby sitter id")
    @Positive(message = "Baby sitter Id should be positive")
    private Integer babySitterId;
    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty number of hours")
    @Positive(message = "number of hours should be at least 1")
    private Integer hours;
    @Column(columnDefinition = "DOUBLE default 0.0")
    private Double totalPrice=0.0;
    @Column(columnDefinition = "varchar(10) default 'Pending'")
    @Pattern(regexp = "Pending|Confirmed|Completed",message = "Booking status must be Pending, Confirmed, or Completed ")
    private String status="Pending";
    @Column(nullable = false)
    @NotNull(message = "empty start time")
    @FutureOrPresent(message = "Start time must be Future Or Present")
    private LocalDateTime startTime;
    @Column(nullable = false)
    @NotNull(message = "empty end time time")
    @FutureOrPresent(message = "end time must be Future Or Present")
    private LocalDateTime requestEndTime;
    @Column(columnDefinition = "varchar(20) default 'Pending'")
    @Pattern(regexp = "Pending|Completed",message = "payment Status must be Pending, or Completed ")
    private String paymentStatus="Pending";
    @Column(columnDefinition = "boolean default false")
    private boolean isRecurring= false;
    @Column(columnDefinition = "varchar(10)")
    @Pattern(regexp = "DAILY|WEEKLY|MONTHLY", message = "Frequency must be DAILY, WEEKLY, or MONTHLY")
    private String recurrenceFrequency;
    @Column(nullable = true)
    private LocalDateTime recurrencfeEndDate;
    @Column(columnDefinition = "varchar(20)")
    private String discountCode ;
    @Column(columnDefinition = "DOUBLE default 0.0")
    private Double discountPercentage = 0.0;
    @Column(columnDefinition = "varchar(500) not null")
    @NotEmpty(message = "empty child Ids ")
    private String childIds;
    @Column(nullable = true)
    private String recurrenceGroup;

}
