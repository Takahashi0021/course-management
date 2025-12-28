package com.example.coursemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponse {

    private Long id;
    private String title;
    private String content;
    private String videoUrl;
    private Long courseId;
    private Integer orderNumber;
    private LocalDateTime createdAt;
}