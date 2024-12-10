package com.example.safehands.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
//@Check(constraints = "password ~ '^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$' AND LENGTH(name) >= 4 AND LENGTH(password) >= 8")
public class Admin {
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

}
