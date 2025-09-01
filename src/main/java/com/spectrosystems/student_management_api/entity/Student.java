package com.spectrosystems.student_management_api.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank   (message = "First name is required")
    @Size       (min = 3, max = 15, message = "First name must be 3-15 characters")
    String firstName;

    @NotBlank   (message = "Last name is required")
    @Size       (min = 3, max = 15, message = "Last name must be 3-15 characters")
    String lastName;

    @NotBlank   (message = "Email is required")
    @Email      (message = "Invalid Email address")
    @Column(unique = true)
    String email;

    @NotNull   (message = "Date of birth is required")
    @Past       (message = "The date of birth must be in the past")
    LocalDate dateOfBirth;
}
