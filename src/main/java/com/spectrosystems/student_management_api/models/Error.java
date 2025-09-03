package com.spectrosystems.student_management_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents an error response returned by the API.
 * Contains HTTP status code, error message, and timestamp.
 */
@Data
@Builder
@AllArgsConstructor
public class Error {

    /**
     * HTTP status code of the error response.
     */
    private int status;

    /**
     * Detailed message describing the error.
     */
    private String message;

    /**
     * Timestamp indicating when the error occurred.
     */
    private LocalDateTime timestamp;
}
