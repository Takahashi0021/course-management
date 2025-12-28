package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.LessonRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.dto.response.LessonResponse;
import com.example.coursemanagement.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Tag(name = "Lessons", description = "Lesson management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Create a new lesson")
    public ResponseEntity<ApiResponse> createLesson(@Valid @RequestBody LessonRequest request) {
        LessonResponse lessonResponse = lessonService.createLesson(request);
        return ResponseEntity.ok(ApiResponse.success("Lesson created successfully", lessonResponse));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get lessons by course ID")
    public ResponseEntity<ApiResponse> getLessonsByCourse(@PathVariable Long courseId) {
        List<LessonResponse> lessons = lessonService.getLessonsByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success("Lessons retrieved successfully", lessons));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get lesson by ID")
    public ResponseEntity<ApiResponse> getLessonById(@PathVariable Long id) {
        LessonResponse lessonResponse = lessonService.getLessonById(id);
        return ResponseEntity.ok(ApiResponse.success("Lesson retrieved successfully", lessonResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Update lesson")
    public ResponseEntity<ApiResponse> updateLesson(
            @PathVariable Long id,
            @Valid @RequestBody LessonRequest request) {
        LessonResponse lessonResponse = lessonService.updateLesson(id, request);
        return ResponseEntity.ok(ApiResponse.success("Lesson updated successfully", lessonResponse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Delete lesson")
    public ResponseEntity<ApiResponse> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok(ApiResponse.success("Lesson deleted successfully"));
    }

    @PutMapping("/course/{courseId}/reorder")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Reorder lessons in a course")
    public ResponseEntity<ApiResponse> reorderLessons(
            @PathVariable Long courseId,
            @RequestBody List<Long> lessonIds) {
        lessonService.reorderLessons(courseId, lessonIds);
        return ResponseEntity.ok(ApiResponse.success("Lessons reordered successfully"));
    }
}