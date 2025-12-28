package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.request.CourseRequest;
import com.example.coursemanagement.dto.response.ApiResponse;
import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.service.CourseService;
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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Create a new course")
    public ResponseEntity<ApiResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        CourseResponse courseResponse = courseService.createCourse(request);
        return ResponseEntity.ok(ApiResponse.success("Course created successfully", courseResponse));
    }

    @GetMapping
    @Operation(summary = "Get all courses")
    public ResponseEntity<ApiResponse> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success("Courses retrieved successfully", courses));
    }

    @GetMapping("/published")
    @Operation(summary = "Get published courses")
    public ResponseEntity<ApiResponse> getPublishedCourses() {
        List<CourseResponse> courses = courseService.getPublishedCourses();
        return ResponseEntity.ok(ApiResponse.success("Published courses retrieved successfully", courses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<ApiResponse> getCourseById(@PathVariable Long id) {
        CourseResponse courseResponse = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success("Course retrieved successfully", courseResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Update course")
    public ResponseEntity<ApiResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {
        CourseResponse courseResponse = courseService.updateCourse(id, request);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", courseResponse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Delete course")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully"));
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "Get courses by instructor")
    public ResponseEntity<ApiResponse> getCoursesByInstructor(@PathVariable Long instructorId) {
        List<CourseResponse> courses = courseService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(ApiResponse.success("Courses retrieved successfully", courses));
    }

    @GetMapping("/my-courses")
    @Operation(summary = "Get my courses")
    public ResponseEntity<ApiResponse> getMyCourses() {
        List<CourseResponse> courses = courseService.getMyCourses();
        return ResponseEntity.ok(ApiResponse.success("My courses retrieved successfully", courses));
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Publish course")
    public ResponseEntity<ApiResponse> publishCourse(@PathVariable Long id) {
        CourseResponse courseResponse = courseService.publishCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course published successfully", courseResponse));
    }

    @PostMapping("/{id}/unpublish")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @Operation(summary = "Unpublish course")
    public ResponseEntity<ApiResponse> unpublishCourse(@PathVariable Long id) {
        CourseResponse courseResponse = courseService.unpublishCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course unpublished successfully", courseResponse));
    }

    @GetMapping("/search")
    @Operation(summary = "Search courses")
    public ResponseEntity<ApiResponse> searchCourses(@RequestParam String keyword) {
        List<CourseResponse> courses = courseService.searchCourses(keyword);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", courses));
    }
}