package com.example.coursemanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    private String videoUrl;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Order number is required")
    private Integer orderNumber;
}