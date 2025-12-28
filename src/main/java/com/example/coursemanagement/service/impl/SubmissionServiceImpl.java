package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.entity.Assignment;
import com.example.coursemanagement.entity.AssignmentSubmission;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.entity.UserRole; // Добавить импорт
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.repository.AssignmentRepository;
import com.example.coursemanagement.repository.SubmissionRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AssignmentSubmission submitAssignment(Long assignmentId, String submissionText, String fileUrl, Long studentId) { // Добавить studentId параметр
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + assignmentId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (student.getRole() != UserRole.ROLE_STUDENT) {
            throw new IllegalArgumentException("Only students can submit assignments");
        }

        if (assignment.getDueDate() != null && LocalDateTime.now().isAfter(assignment.getDueDate())) {
            throw new IllegalArgumentException("Assignment deadline has passed");
        }

        submissionRepository.findByAssignmentIdAndStudentId(assignmentId, student.getId())
                .ifPresent(submission -> {
                    throw new IllegalArgumentException("Assignment already submitted");
                });

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmissionText(submissionText);
        submission.setFileUrl(fileUrl);
        submission.setStatus("SUBMITTED");

        return submissionRepository.save(submission);
    }

    @Override
    @Transactional
    public AssignmentSubmission gradeSubmission(Long submissionId, Integer points, String feedback, Long instructorId) { // Добавить instructorId
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with id: " + submissionId));

        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + instructorId));

        if (instructor.getRole() != UserRole.ROLE_INSTRUCTOR && instructor.getRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("Only instructors or admins can grade assignments");
        }

        if (points < 0 || points > submission.getAssignment().getMaxPoints()) {
            throw new IllegalArgumentException("Points must be between 0 and " + submission.getAssignment().getMaxPoints());
        }

        submission.setPoints(points);
        submission.setFeedback(feedback);
        submission.setStatus("GRADED");

        return submissionRepository.save(submission);
    }

    @Override
    public AssignmentSubmission getSubmissionById(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with id: " + id));
    }

    @Override
    public List<AssignmentSubmission> getSubmissionsByAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    @Override
    public List<AssignmentSubmission> getMySubmissions(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (student.getRole() != UserRole.ROLE_STUDENT) {
            throw new IllegalArgumentException("Only students can view their submissions");
        }

        return submissionRepository.findByStudentId(studentId);
    }

    @Override
    public AssignmentSubmission getSubmissionByAssignmentAndStudent(Long assignmentId, Long studentId) {
        return submissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found for assignment " + assignmentId +
                        " and student " + studentId));
    }

    public List<User> getAllStudents() {
        return userRepository.findByRole(UserRole.ROLE_STUDENT);
    }
}