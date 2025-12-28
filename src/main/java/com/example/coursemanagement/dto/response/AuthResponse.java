package com.example.coursemanagement.dto.response;

import com.example.coursemanagement.entity.UserRole;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
}