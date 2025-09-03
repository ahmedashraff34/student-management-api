package com.spectrosystems.student_management_api.exceptions;

import com.spectrosystems.student_management_api.models.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Student Management API.
 * Catches and handles all exceptions thrown by controllers, returning
 * structured error responses with appropriate HTTP status codes.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles StudentNotFoundException.
     *
     * @param ex the exception thrown when a student is not found
     * @return ResponseEntity containing an Error object with 404 NOT FOUND
     */
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Error> handleStudentNotFound(StudentNotFoundException ex) {
        Error error = new Error(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles UserNotFoundException.
     *
     * @param ex the exception thrown when a user is not found
     * @return ResponseEntity containing an Error object with 404 NOT FOUND
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> handleUserNotFound(UserNotFoundException ex) {
        Error error = new Error(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InvalidCredentialsException.
     *
     * @param ex the exception thrown when user credentials are invalid
     * @return ResponseEntity containing an Error object with 401 UNAUTHORIZED
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Error> handleInvalidCredentials(InvalidCredentialsException ex) {
        Error error = new Error(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles DuplicateEmailException.
     *
     * @param ex the exception thrown when an email already exists
     * @return ResponseEntity containing an Error object with 400 BAD REQUEST
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Error> handleDuplicateEmail(DuplicateEmailException ex) {
        Error error = new Error(HttpStatus.CONFLICT.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles DuplicateUsernameException.
     *
     * @param ex the exception thrown when a username already exists
     * @return ResponseEntity containing an Error object with 400 BAD REQUEST
     */
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Error> handleDuplicateUsername(DuplicateUsernameException ex) {
        Error error = new Error(HttpStatus.CONFLICT.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles validation errors for method arguments annotated with @Valid.
     *
     * @param ex the exception thrown when validation fails
     * @return ResponseEntity containing a map of field names to error messages with 400 BAD REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (Object errorObj : ex.getBindingResult().getAllErrors()) {
            FieldError error = (FieldError) errorObj;
            String fieldName = error.getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles database integrity violations.
     *
     * @param ex the exception thrown when a database constraint is violated
     * @return ResponseEntity containing an Error object with 400 BAD REQUEST
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Error> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.error("Database integrity violation", ex);
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), "Database error. Please check your input.", LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other uncaught exceptions.
     *
     * @param ex the generic exception
     * @return ResponseEntity containing an Error object with 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
