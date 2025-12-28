package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.EnrollmentRequest;
import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.dto.response.EnrollmentResponse;
import com.example.coursemanagement.dto.response.UserResponse;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.Enrollment;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.mapper.EnrollmentMapper;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.EnrollmentRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.impl.EnrollmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private User student;
    private User instructor;
    private Course course;
    private Enrollment enrollment;
    private EnrollmentRequest enrollmentRequest;
    private EnrollmentResponse enrollmentResponse;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setEmail("student@example.com");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setRole("ROLE_STUDENT");

        instructor = new User();
        instructor.setId(2L);
        instructor.setEmail("instructor@example.com");
        instructor.setRole("ROLE_INSTRUCTOR");

        course = new Course();
        course.setId(1L);
        course.setTitle("Introduction to Java");
        course.setInstructor(instructor);
        course.setPublished(true);

        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setProgress(0);
        enrollment.setStatus("ACTIVE");

        enrollmentRequest = new EnrollmentRequest(1L, 1L);

        UserResponse studentResponse = new UserResponse(
                1L,
                "student@example.com",
                "John",
                "Doe",
                "ROLE_STUDENT",
                true,
                null,
                null
        );

        CourseResponse courseResponse = new CourseResponse(
                1L,
                "Introduction to Java",
                "Description",
                null,
                BigDecimal.ZERO,
                true,
                null,
                null,
                0,
                0
        );

        enrollmentResponse = new EnrollmentResponse(
                1L,
                studentResponse,
                courseResponse,
                LocalDateTime.now(),
                0,
                "ACTIVE"
        );
    }

    @Test
    void enroll_ShouldCreateEnrollmentSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        when(enrollmentMapper.toResponse(any(Enrollment.class))).thenReturn(enrollmentResponse);

        EnrollmentResponse result = enrollmentService.enroll(enrollmentRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ACTIVE", result.getStatus());

        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(enrollmentRepository, times(1)).existsByStudentIdAndCourseId(1L, 1L);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void enroll_WithNonExistingStudent_ShouldThrowException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        EnrollmentRequest invalidRequest = new EnrollmentRequest(999L, 1L);

        assertThrows(ResourceNotFoundException.class, () -> {
            enrollmentService.enroll(invalidRequest);
        });

        verify(userRepository, times(1)).findById(999L);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void enroll_WithNonStudentRole_ShouldThrowException() {
        student.setRole("ROLE_INSTRUCTOR");
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));

        assertThrows(IllegalArgumentException.class, () -> {
            enrollmentService.enroll(enrollmentRequest);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void enroll_WithUnpublishedCourse_ShouldThrowException() {
        course.setPublished(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        assertThrows(IllegalArgumentException.class, () -> {
            enrollmentService.enroll(enrollmentRequest);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void enroll_WhenAlreadyEnrolled_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            enrollmentService.enroll(enrollmentRequest);
        });

        verify(enrollmentRepository, times(1)).existsByStudentIdAndCourseId(1L, 1L);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void getEnrollmentById_ShouldReturnEnrollment() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(enrollmentResponse);

        EnrollmentResponse result = enrollmentService.getEnrollmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void getEnrollmentsByStudent_ShouldReturnEnrollments() {
        List<Enrollment> enrollments = Arrays.asList(enrollment);
        List<EnrollmentResponse> enrollmentResponses = Arrays.asList(enrollmentResponse);

        when(enrollmentRepository.findByStudentId(1L)).thenReturn(enrollments);
        when(enrollmentMapper.toResponseList(enrollments)).thenReturn(enrollmentResponses);

        List<EnrollmentResponse> result = enrollmentService.getEnrollmentsByStudent(1L);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(enrollmentRepository, times(1)).findByStudentId(1L);
    }

    @Test
    void unenroll_ShouldDeleteEnrollment() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        enrollmentService.unenroll(1L);

        verify(enrollmentRepository, times(1)).delete(enrollment);
    }

    @Test
    void updateProgress_ShouldUpdateProgress() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        when(enrollmentMapper.toResponse(any(Enrollment.class))).thenReturn(enrollmentResponse);

        EnrollmentResponse result = enrollmentService.updateProgress(1L, 50);

        assertNotNull(result);
        assertEquals(50, enrollment.getProgress());

        verify(enrollmentRepository, times(1)).findById(1L);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void updateProgress_WithInvalidValue_ShouldThrowException() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        assertThrows(IllegalArgumentException.class, () -> {
            enrollmentService.updateProgress(1L, 150);
        });

        verify(enrollmentRepository, times(1)).findById(1L);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void completeEnrollment_ShouldCompleteEnrollment() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        when(enrollmentMapper.toResponse(any(Enrollment.class))).thenReturn(enrollmentResponse);

        EnrollmentResponse result = enrollmentService.completeEnrollment(1L);

        assertNotNull(result);
        assertEquals(100, enrollment.getProgress());
        assertEquals("COMPLETED", enrollment.getStatus());

        verify(enrollmentRepository, times(1)).findById(1L);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void isEnrolled_ShouldReturnTrueWhenEnrolled() {
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

        boolean result = enrollmentService.isEnrolled(1L, 1L);

        assertTrue(result);
        verify(enrollmentRepository, times(1)).existsByStudentIdAndCourseId(1L, 1L);
    }

    @Test
    void isEnrolled_ShouldReturnFalseWhenNotEnrolled() {
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);

        boolean result = enrollmentService.isEnrolled(1L, 1L);

        assertFalse(result);
        verify(enrollmentRepository, times(1)).existsByStudentIdAndCourseId(1L, 1L);
    }
}