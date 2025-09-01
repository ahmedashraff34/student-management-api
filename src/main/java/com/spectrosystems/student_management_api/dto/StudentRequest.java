package com.spectrosystems.student_management_api.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {

    @NotBlank   (message = "First name is required")
    @Size       (min = 3, max = 15, message = "First name must be 3-15 characters")
    @Pattern    (regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
    String firstName;

    @NotBlank   (message = "Last name is required")
    @Size       (min = 3, max = 15, message = "Last name must be 3-15 characters")
    @Pattern    (regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
    String lastName;

    @NotBlank   (message = "Email is required")
    @Email      (message = "Invalid Email address")
    String email;

    @NotNull   (message = "Date of birth is required")
    @Past       (message = "The date of birth must be in the past")
    LocalDate dateOfBirth;
}
