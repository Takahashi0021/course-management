package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.EnrollmentRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.dto.response.EnrollmentResponse;
import com.example.coursemanagement.service.EnrollmentService;
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
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollments", description = "Enrollment management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Enroll in a course")
    public ResponseEntity<ApiResponse> enroll(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentResponse enrollmentResponse = enrollmentService.enroll(request);
        return ResponseEntity.ok(ApiResponse.success("Enrolled successfully", enrollmentResponse));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all enrollments (Admin only)")
    public ResponseEntity<ApiResponse> getAllEnrollments() {
        List<EnrollmentResponse> enrollments = enrollmentService.getMyEnrollments();
        return ResponseEntity.ok(ApiResponse.success("Enrollments retrieved successfully", enrollments));
    }

    @GetMapping("/my-enrollments")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get my enrollments")
    public ResponseEntity<ApiResponse> getMyEnrollments() {
        List<EnrollmentResponse> enrollments = enrollmentService.getMyEnrollments();
        return ResponseEntity.ok(ApiResponse.success("My enrollments retrieved successfully", enrollments));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Get enrollments by student")
    public ResponseEntity<ApiResponse> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success("Enrollments retrieved successfully", enrollments));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Get enrollments by course")
    public ResponseEntity<ApiResponse> getEnrollmentsByCourse(@PathVariable Long courseId) {
        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success("Enrollments retrieved successfully", enrollments));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID")
    public ResponseEntity<ApiResponse> getEnrollmentById(@PathVariable Long id) {
        EnrollmentResponse enrollmentResponse = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Enrollment retrieved successfully", enrollmentResponse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @Operation(summary = "Unenroll from course")
    public ResponseEntity<ApiResponse> unenroll(@PathVariable Long id) {
        enrollmentService.unenroll(id);
        return ResponseEntity.ok(ApiResponse.success("Unenrolled successfully"));
    }

    @PutMapping("/{id}/progress")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Update course progress")
    public ResponseEntity<ApiResponse> updateProgress(
            @PathVariable Long id,
            @RequestParam Integer progress) {
        EnrollmentResponse enrollmentResponse = enrollmentService.updateProgress(id, progress);
        return ResponseEntity.ok(ApiResponse.success("Progress updated successfully", enrollmentResponse));
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Complete enrollment")
    public ResponseEntity<ApiResponse> completeEnrollment(@PathVariable Long id) {
        EnrollmentResponse enrollmentResponse = enrollmentService.completeEnrollment(id);
        return ResponseEntity.ok(ApiResponse.success("Enrollment completed successfully", enrollmentResponse));
    }
}