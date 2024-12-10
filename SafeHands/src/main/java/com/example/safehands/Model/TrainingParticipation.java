package com.example.safehands.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Check(constraints = "status = 'registered' OR status = 'completed' OR status = 'certified'")
public class TrainingParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty babysitter ID")
    private Integer babySitterId; // Babysitter participating in the training

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty training ID")
    private Integer courseId; // Training course ID

    @Column(columnDefinition = "varchar(10) default 'registered'")
    @Pattern(regexp = "registered|completed|certified", message = "Status must be registered or completed or certified")
    private String status="registered";

}
