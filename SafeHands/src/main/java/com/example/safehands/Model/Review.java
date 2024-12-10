package com.example.safehands.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Check(constraints = "rating >= 1 AND rating <= 5")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty rating")
    @Positive(message = "Rating must be more than 0")
    @Min(1)
    @Max(5)
    private Integer rating;
    @Column(columnDefinition = "varchar(100) not null")
    @NotEmpty(message = "Empty comment")
    private String comment;
    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty parent id")
    private Integer parentId;
    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty babySitter  id")
    private Integer babySitterId;



}
