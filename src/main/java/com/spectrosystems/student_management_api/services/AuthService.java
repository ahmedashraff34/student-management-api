package com.spectrosystems.student_management_api.services;

import com.spectrosystems.student_management_api.configs.JwtService;
import com.spectrosystems.student_management_api.dtos.AuthenticationResponse;
import com.spectrosystems.student_management_api.dtos.RegisterRequest;
import com.spectrosystems.student_management_api.exceptions.InvalidCredentialsException;
import com.spectrosystems.student_management_api.exceptions.UserNotFoundException;
import com.spectrosystems.student_management_api.models.User;
import com.spectrosystems.student_management_api.exceptions.DuplicateEmailException;
import com.spectrosystems.student_management_api.exceptions.DuplicateUsernameException;
import com.spectrosystems.student_management_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication and registration operations.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user using username or email and password.
     *
     * @param usernameOrEmail the username or email of the user
     * @param password        the password of the user
     * @return AuthenticationResponse containing a JWT token if authentication succeeds
     * @throws UserNotFoundException       if no user exists with the given username or email
     * @throws InvalidCredentialsException if the password is incorrect
     */
    public AuthenticationResponse login(String usernameOrEmail, String password) {
        var user = userRepository.findByUsername(usernameOrEmail).or(() -> userRepository.findByEmail(usernameOrEmail)).orElseThrow(() -> new UserNotFoundException("User not found"));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), password));
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid Credentials");
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    /**
     * Registers a new user and returns a JWT token.
     *
     * @param request the registration request containing user details
     * @return AuthenticationResponse containing a JWT token
     * @throws DuplicateUsernameException if the username already exists
     * @throws DuplicateEmailException    if the email already exists
     */
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName()).username(request.getUsername()).email(request.getEmail()).role(request.getRole()).password(passwordEncoder.encode(request.getPassword())).build();

        try {
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("uk_username")) {
                throw new DuplicateUsernameException("Username already exists");
            }
            if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("uk_email")) {
                throw new DuplicateEmailException("Email already exists");
            }
            throw ex;
        }
    }
}
