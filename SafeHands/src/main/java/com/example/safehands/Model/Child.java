package com.example.safehands.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty parent id")
    @Positive(message = "Parent Id should be positive")
    private Integer parentId;
    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "Empty name")
    private String name;
    @Column(columnDefinition = "int not null")
    @NotNull(message = "Empty age")
    @Positive(message = "age must be positive")
    private Integer age;
    @Column(columnDefinition = "BOOLEAN not null")
    @NotNull(message = "Empty special Needs")
    private Boolean specialNeeds;
    @Column(columnDefinition = "varchar(200) not null")
    @NotEmpty(message = "Empty healthCondition")
    private String healthCondition;

}
