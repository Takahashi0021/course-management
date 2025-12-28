package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.AssignmentRequest;
import com.example.coursemanagement.dto.response.AssignmentResponse;
import com.example.coursemanagement.entity.Assignment;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.mapper.AssignmentMapper;
import com.example.coursemanagement.repository.AssignmentRepository;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.service.impl.AssignmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AssignmentMapper assignmentMapper;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    private Assignment assignment;
    private AssignmentRequest assignmentRequest;
    private AssignmentResponse assignmentResponse;
    private Course course;

    @BeforeEach
    void setUp() {
        User instructor = new User();
        instructor.setId(1L);
        instructor.setRole("ROLE_INSTRUCTOR");

        course = new Course();
        course.setId(1L);
        course.setTitle("Java Course");
        course.setInstructor(instructor);

        assignment = new Assignment();
        assignment.setId(1L);
        assignment.setTitle("Java Basics Assignment");
        assignment.setDescription("Complete the exercises");
        assignment.setCourse(course);
        assignment.setMaxPoints(100);
        assignment.setDueDate(LocalDateTime.now().plusDays(7));
        assignment.setCreatedAt(LocalDateTime.now());

        assignmentRequest = new AssignmentRequest(
                "Java Basics Assignment",
                "Complete the exercises",
                1L,
                100,
                LocalDateTime.now().plusDays(7)
        );

        assignmentResponse = new AssignmentResponse(
                1L,
                "Java Basics Assignment",
                "Complete the exercises",
                1L,
                100,
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now(),
                0
        );
    }

    @Test
    void createAssignment_ShouldCreateAssignmentSuccessfully() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(assignmentMapper.toEntity(assignmentRequest)).thenReturn(assignment);
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);
        when(assignmentMapper.toResponse(any(Assignment.class))).thenReturn(assignmentResponse);

        AssignmentResponse result = assignmentService.createAssignment(assignmentRequest);

        assertNotNull(result);
        assertEquals("Java Basics Assignment", result.getTitle());
        assertEquals(100, result.getMaxPoints());

        verify(courseRepository, times(1)).findById(1L);
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }

    @Test
    void createAssignment_WithNonExistingCourse_ShouldThrowException() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        AssignmentRequest invalidRequest = new AssignmentRequest(
                "Test Assignment",
                "Description",
                999L,
                100,
                LocalDateTime.now().plusDays(7)
        );

        assertThrows(ResourceNotFoundException.class, () -> {
            assignmentService.createAssignment(invalidRequest);
        });

        verify(courseRepository, times(1)).findById(999L);
        verify(assignmentRepository, never()).save(any(Assignment.class));
    }

    @Test
    void createAssignment_WithPastDueDate_ShouldThrowException() {
        AssignmentRequest invalidRequest = new AssignmentRequest(
                "Test Assignment",
                "Description",
                1L,
                100,
                LocalDateTime.now().minusDays(1)
        );

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        assertThrows(IllegalArgumentException.class, () -> {
            assignmentService.createAssignment(invalidRequest);
        });

        verify(courseRepository, times(1)).findById(1L);
        verify(assignmentRepository, never()).save(any(Assignment.class));
    }

    @Test
    void getAssignmentById_ShouldReturnAssignment() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
        when(assignmentMapper.toResponse(assignment)).thenReturn(assignmentResponse);

        AssignmentResponse result = assignmentService.getAssignmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java Basics Assignment", result.getTitle());

        verify(assignmentRepository, times(1)).findById(1L);
    }

    @Test
    void getAssignmentById_WithNonExistingId_ShouldThrowException() {
        when(assignmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            assignmentService.getAssignmentById(999L);
        });

        verify(assignmentRepository, times(1)).findById(999L);
    }

    @Test
    void getAssignmentsByCourse_ShouldReturnAssignments() {
        List<Assignment> assignments = Arrays.asList(assignment);
        List<AssignmentResponse> assignmentResponses = Arrays.asList(assignmentResponse);

        when(assignmentRepository.findByCourseId(1L)).thenReturn(assignments);
        when(assignmentMapper.toResponseList(assignments)).thenReturn(assignmentResponses);

        List<AssignmentResponse> result = assignmentService.getAssignmentsByCourse(1L);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(assignmentRepository, times(1)).findByCourseId(1L);
    }

    @Test
    void updateAssignment_ShouldUpdateAssignmentSuccessfully() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);
        when(assignmentMapper.toResponse(any(Assignment.class))).thenReturn(assignmentResponse);

        AssignmentResponse result = assignmentService.updateAssignment(1L, assignmentRequest);

        assertNotNull(result);

        verify(assignmentRepository, times(1)).findById(1L);
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }

    @Test
    void deleteAssignment_ShouldDeleteAssignment() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        assignmentService.deleteAssignment(1L);

        verify(assignmentRepository, times(1)).delete(assignment);
    }

    @Test
    void getMyAssignments_ShouldReturnAssignments() {
        List<Assignment> assignments = Arrays.asList(assignment);
        List<AssignmentResponse> assignmentResponses = Arrays.asList(assignmentResponse);

        when(assignmentRepository.findAll()).thenReturn(assignments);
        when(assignmentMapper.toResponseList(assignments)).thenReturn(assignmentResponses);

        List<AssignmentResponse> result = assignmentService.getMyAssignments();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(assignmentRepository, times(1)).findAll();
    }
}