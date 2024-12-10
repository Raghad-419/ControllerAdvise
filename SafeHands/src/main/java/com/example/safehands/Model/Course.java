package com.example.safehands.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(60) not null")
    @NotEmpty(message = "Empty title")
    private String title;
    @Column(columnDefinition = "varchar(500) not null")
    @NotEmpty(message = "Empty content")
    private String content;
    @Column(nullable = false)
    @NotNull(message = "Empty start date")
    private LocalDateTime startDate;
    @Column(nullable = false)
    @NotNull(message = "Empty end date")
    private LocalDateTime endDate;


}