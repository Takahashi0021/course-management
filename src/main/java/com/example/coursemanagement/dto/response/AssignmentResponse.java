package com.example.coursemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponse {

    private Long id;
    private String title;
    private String description;
    private Long courseId;
    private Integer maxPoints;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private Integer submissionCount;
}