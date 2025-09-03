package com.spectrosystems.student_management_api.dtos;

import com.spectrosystems.student_management_api.models.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Data Transfer Object for user registration requests.
 * Contains the necessary information to create a new user account.
 */
@Data
public class RegisterRequest {

    /**
     * First name of the user.
     * Must be 3-15 characters long and contain only letters.
     */
    @NotBlank
    @Size(min = 3, max = 15, message = "First name must be 3-15 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
    private String firstName;

    /**
     * Last name of the user.
     * Must be 3-15 characters long and contain only letters.
     */
    @NotBlank
    @Size(min = 3, max = 15, message = "Last name must be 3-15 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
    private String lastName;

    /**
     * Username for the new account.
     * Must be 3-20 characters long.
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Name must be 3-20 characters")
    private String username;

    /**
     * Email address of the user.
     * Must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email address")
    private String email;

    /**
     * Password for the new account.
     * Must be 8-25 characters long.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 25, message = "Password must be 8-25 characters")
    private String password;

    /**
     * Role assigned to the user.
     * Cannot be null.
     */
    @NotNull(message = "Role should not be empty")
    private Role role;
}
