package com.spectrosystems.student_management_api.controllers;

import com.spectrosystems.student_management_api.dtos.AuthenticationResponse;
import com.spectrosystems.student_management_api.dtos.RegisterRequest;
import com.spectrosystems.student_management_api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication operations.
 * Provides endpoints for user login and registration.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user with username/email and password.
     *
     * @param usernameOrEmail the username or email of the user
     * @param password        the user's password
     * @return ResponseEntity containing the authentication response (JWT token, user info, etc.)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestParam String usernameOrEmail, @RequestParam String password) {
        return ResponseEntity.ok(authService.login(usernameOrEmail, password));
    }

    /**
     * Registers a new user with the provided registration details.
     *
     * @param request RegisterRequest DTO containing user registration data
     * @return ResponseEntity containing the authentication response (JWT token, user info, etc.)
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }
}
