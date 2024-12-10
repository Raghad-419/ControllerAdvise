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
//@Check(constraints = "password ~ '^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$' AND LENGTH(name) >= 4 AND LENGTH(password) >= 8")
public class BabySitter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "Empty name")
    @Size(min = 4,message = "name must be at least 4 letters")
    private String name;
    @Column(columnDefinition = "varchar(30) not null unique")
    @NotEmpty(message = "Empty email")
    @Email(message = "Enter valid email")
    private String email;
    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "Empty password")
    @Size(min = 8 ,message = "Password length should be at least 8")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*^?&])[A-Za-z\\d@$!%*^?&]{8,}$", message = "Password must be at least 8 characters long and contain one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String password;
    @Column(columnDefinition = "varchar(200) not null")
    @NotEmpty(message = "Empty bio")
    private String bio;
    @Column(columnDefinition = "Double not null")
    @NotNull(message = "Empty hourly rate")
    @Positive(message = "Hourly rate must be positive")
    private Double hourlyRate;
    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty extra Child Rate")
    @Positive(message = "extra Child rate must be positive")
    private Integer extraChildRate;
    @Column(columnDefinition = "varchar(200) not null")
    @NotEmpty(message = "Empty Skills")
    private String skills;
    @Column(columnDefinition = "varchar(200)")
    private String certifications="";
    @Column(columnDefinition = "double default 0.0")
    private Double averageRating=0.0;
    @Column(columnDefinition = "int default 0")
    private Integer totalRatings=0;
    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty free Cancellation Period ")
    private Integer freeCancellationPeriod;// e.g., 24 hours
    @Column(columnDefinition = "DOUBLE not null")
    @NotNull(message = "Empty cancellation Fee Percentage")
    private Double cancellationFeePercentage;
    @Column(columnDefinition = "boolean default false ")
    private Boolean activation =false;
}
