package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.dto.request.AssignmentRequest;
import com.example.coursemanagement.dto.response.AssignmentResponse;
import com.example.coursemanagement.entity.Assignment;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.mapper.AssignmentMapper;
import com.example.coursemanagement.repository.AssignmentRepository;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final AssignmentMapper assignmentMapper;

    @Override
    @Transactional
    public AssignmentResponse createAssignment(AssignmentRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        if (request.getDueDate() != null && request.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date must be in the future");
        }

        Assignment assignment = assignmentMapper.toEntity(request);
        assignment.setCourse(course);

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return assignmentMapper.toResponse(savedAssignment);
    }

    @Override
    @Transactional
    public AssignmentResponse updateAssignment(Long id, AssignmentRequest request) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        if (request.getDueDate() != null && request.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date must be in the future");
        }

        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setCourse(course);
        assignment.setMaxPoints(request.getMaxPoints());
        assignment.setDueDate(request.getDueDate());

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        return assignmentMapper.toResponse(updatedAssignment);
    }

    @Override
    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        assignmentRepository.delete(assignment);
    }

    @Override
    public AssignmentResponse getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        return assignmentMapper.toResponse(assignment);
    }

    @Override
    public List<AssignmentResponse> getAssignmentsByCourse(Long courseId) {
        List<Assignment> assignments = assignmentRepository.findByCourseId(courseId);
        return assignmentMapper.toResponseList(assignments);
    }

    @Override
    public List<AssignmentResponse> getMyAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll();
        return assignmentMapper.toResponseList(assignments);
    }

    @Override
    public List<AssignmentResponse> getOverdueAssignments() {
        return List.of();
    }
}