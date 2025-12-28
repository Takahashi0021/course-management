package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.AssignmentRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.dto.response.AssignmentResponse;
import com.example.coursemanagement.service.AssignmentService;
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
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@Tag(name = "Assignments", description = "Assignment management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Create a new assignment")
    public ResponseEntity<ApiResponse> createAssignment(@Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse assignmentResponse = assignmentService.createAssignment(request);
        return ResponseEntity.ok(ApiResponse.success("Assignment created successfully", assignmentResponse));
    }

    @GetMapping
    @Operation(summary = "Get all assignments")
    public ResponseEntity<ApiResponse> getAllAssignments() {
        List<AssignmentResponse> assignments = assignmentService.getMyAssignments();
        return ResponseEntity.ok(ApiResponse.success("Assignments retrieved successfully", assignments));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get assignment by ID")
    public ResponseEntity<ApiResponse> getAssignmentById(@PathVariable Long id) {
        AssignmentResponse assignmentResponse = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Assignment retrieved successfully", assignmentResponse));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get assignments by course")
    public ResponseEntity<ApiResponse> getAssignmentsByCourse(@PathVariable Long courseId) {
        List<AssignmentResponse> assignments = assignmentService.getAssignmentsByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success("Assignments retrieved successfully", assignments));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Update assignment")
    public ResponseEntity<ApiResponse> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse assignmentResponse = assignmentService.updateAssignment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Assignment updated successfully", assignmentResponse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Delete assignment")
    public ResponseEntity<ApiResponse> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok(ApiResponse.success("Assignment deleted successfully"));
    }

    @GetMapping("/my-assignments")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get my assignments")
    public ResponseEntity<ApiResponse> getMyAssignments() {
        List<AssignmentResponse> assignments = assignmentService.getMyAssignments();
        return ResponseEntity.ok(ApiResponse.success("My assignments retrieved successfully", assignments));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get overdue assignments")
    public ResponseEntity<ApiResponse> getOverdueAssignments() {
        List<AssignmentResponse> assignments = assignmentService.getOverdueAssignments();
        return ResponseEntity.ok(ApiResponse.success("Overdue assignments retrieved successfully", assignments));
    }
}