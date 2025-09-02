package com.spectrosystems.student_management_api.service;


import com.spectrosystems.student_management_api.config.JwtService;
import com.spectrosystems.student_management_api.dto.AuthenticationResponse;
import com.spectrosystems.student_management_api.dto.LoginRequest;
import com.spectrosystems.student_management_api.dto.RegisterRequest;
import com.spectrosystems.student_management_api.entity.User;
import com.spectrosystems.student_management_api.exception.DuplicateEmailException;
import com.spectrosystems.student_management_api.exception.DuplicateUsernameException;
import com.spectrosystems.student_management_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken= jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        try {
            userRepository.save(user);
            var jwtToken= jwtService.generateToken(user);
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
