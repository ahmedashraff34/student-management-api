package com.spectrosystems.student_management_api.dto;
import com.spectrosystems.student_management_api.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 15, message = "Name must be 3-15 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only letters")
    private String name;

    @NotBlank (message = "Username is required")
    @Size(min = 3, max = 20, message = "Name must be 3-15 characters")
    private String username;

    @NotBlank   (message = "Email is required")
    @Email      (message = "Invalid Email address")
    private String email;

    @NotBlank (message = "Password is required")
    @Size(min = 8, max = 25, message = "Name must be 8-25 characters")
    private String password;

    @NotNull (message = "Role should not be empty")
    private Role role;
}