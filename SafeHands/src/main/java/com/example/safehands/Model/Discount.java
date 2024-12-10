package com.example.safehands.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(20) not null unique")
    @NotNull(message = "Empty code")
    private String code; // The discount code
    @Column(columnDefinition = "double not null")
    @NotNull(message = "Empty amount")
    @Positive(message = "Amount must be positive")
    private Double amount;// The discount amount (percentage or flat value)
    @Column(nullable = false)
    @NotNull(message = "Empty expiration date")
    @FutureOrPresent(message = "Expiration must be present or future")
    private LocalDateTime expirationDate;// Expiration date of the discount code
}
