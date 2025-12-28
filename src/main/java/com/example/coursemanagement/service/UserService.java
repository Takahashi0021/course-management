package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.RegisterRequest;
import com.example.coursemanagement.dto.response.UserResponse;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.entity.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(RegisterRequest request);
    Optional<User> findByEmail(String email);
    List<UserResponse> getAllUsers();
    List<UserResponse> getUsersByRole(UserRole role);
    UserResponse updateUserRole(Long userId, UserRole role);
}