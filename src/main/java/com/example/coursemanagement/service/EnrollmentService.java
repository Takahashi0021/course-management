package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.EnrollmentRequest;
import com.example.coursemanagement.dto.response.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponse enroll(EnrollmentRequest request);

    void unenroll(Long enrollmentId);

    EnrollmentResponse getEnrollmentById(Long id);

    List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId);

    List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId);

    List<EnrollmentResponse> getMyEnrollments();

    EnrollmentResponse updateProgress(Long enrollmentId, Integer progress);

    EnrollmentResponse completeEnrollment(Long enrollmentId);

    boolean isEnrolled(Long studentId, Long courseId);

    Long countEnrollmentsByCourse(Long courseId);

    Long countEnrollmentsByStudent(Long studentId);
}