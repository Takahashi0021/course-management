package com.example.coursemanagement.service;

import com.example.coursemanagement.entity.AssignmentSubmission;

import java.util.List;

public interface SubmissionService {
    AssignmentSubmission submitAssignment(Long assignmentId, String submissionText, String fileUrl, Long studentId);
    AssignmentSubmission gradeSubmission(Long submissionId, Integer points, String feedback, Long instructorId);
    AssignmentSubmission getSubmissionById(Long id);
    List<AssignmentSubmission> getSubmissionsByAssignment(Long assignmentId);
    List<AssignmentSubmission> getMySubmissions(Long studentId);
    AssignmentSubmission getSubmissionByAssignmentAndStudent(Long assignmentId, Long studentId);
}