package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.AssignmentRequest;
import com.example.coursemanagement.dto.response.AssignmentResponse;

import java.util.List;

public interface AssignmentService {

    AssignmentResponse createAssignment(AssignmentRequest request);

    AssignmentResponse updateAssignment(Long id, AssignmentRequest request);

    void deleteAssignment(Long id);

    AssignmentResponse getAssignmentById(Long id);

    List<AssignmentResponse> getAssignmentsByCourse(Long courseId);

    List<AssignmentResponse> getMyAssignments();

    List<AssignmentResponse> getOverdueAssignments();
}