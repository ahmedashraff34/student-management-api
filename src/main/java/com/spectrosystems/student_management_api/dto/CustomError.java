package com.spectrosystems.student_management_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CustomError {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}