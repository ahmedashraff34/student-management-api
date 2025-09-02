package com.spectrosystems.student_management_api.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}