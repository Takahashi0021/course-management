package com.example.coursemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {

    private Long id;
    private UserResponse student;
    private CourseResponse course;
    private LocalDateTime enrolledAt;
    private Integer progress;
    private String status;
}