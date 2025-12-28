package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.CourseRequest;
import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.dto.response.UserResponse;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.mapper.CourseMapper;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.impl.CourseServiceImpl;
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
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private CourseRequest courseRequest;
    private CourseResponse courseResponse;
    private User instructor;
    private UserResponse instructorResponse;

    @BeforeEach
    void setUp() {
        instructor = new User();
        instructor.setId(1L);
        instructor.setEmail("instructor@example.com");
        instructor.setFirstName("Professor");
        instructor.setLastName("Smith");
        instructor.setRole("ROLE_INSTRUCTOR");

        instructorResponse = new UserResponse(
                1L,
                "instructor@example.com",
                "Professor",
                "Smith",
                "ROLE_INSTRUCTOR",
                true,
                null,
                null
        );

        course = new Course();
        course.setId(1L);
        course.setTitle("Introduction to Java");
        course.setDescription("Learn Java programming basics");
        course.setInstructor(instructor);
        course.setPrice(BigDecimal.valueOf(99.99));
        course.setPublished(true);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        courseRequest = new CourseRequest(
                "Introduction to Java",
                "Learn Java programming basics",
                1L,
                BigDecimal.valueOf(99.99),
                true
        );

        courseResponse = new CourseResponse(
                1L,
                "Introduction to Java",
                "Learn Java programming basics",
                instructorResponse,
                BigDecimal.valueOf(99.99),
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                10,
                5
        );
    }

    @Test
    void createCourse_ShouldCreateCourseSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.existsByTitleAndInstructorId("Introduction to Java", 1L)).thenReturn(false);
        when(courseMapper.toEntity(courseRequest)).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toResponse(any(Course.class))).thenReturn(courseResponse);

        CourseResponse result = courseService.createCourse(courseRequest);

        assertNotNull(result);
        assertEquals("Introduction to Java", result.getTitle());
        assertEquals(1L, result.getInstructor().getId());

        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).existsByTitleAndInstructorId("Introduction to Java", 1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void createCourse_WithNonExistingInstructor_ShouldThrowException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        CourseRequest invalidRequest = new CourseRequest(
                "Test Course",
                "Description",
                999L,
                BigDecimal.ZERO,
                true
        );

        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.createCourse(invalidRequest);
        });

        verify(userRepository, times(1)).findById(999L);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void getCourseById_ShouldReturnCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toResponse(course)).thenReturn(courseResponse);

        CourseResponse result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Introduction to Java", result.getTitle());

        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getCourseById_WithNonExistingId_ShouldThrowException() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.getCourseById(999L);
        });

        verify(courseRepository, times(1)).findById(999L);
    }

    @Test
    void getAllCourses_ShouldReturnListOfCourses() {
        List<Course> courses = Arrays.asList(course, course);
        List<CourseResponse> courseResponses = Arrays.asList(courseResponse, courseResponse);

        when(courseRepository.findAll()).thenReturn(courses);
        when(courseMapper.toResponseList(courses)).thenReturn(courseResponses);

        List<CourseResponse> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getPublishedCourses_ShouldReturnPublishedCourses() {
        List<Course> publishedCourses = Arrays.asList(course);
        List<CourseResponse> courseResponses = Arrays.asList(courseResponse);

        when(courseRepository.findByIsPublishedTrue()).thenReturn(publishedCourses);
        when(courseMapper.toResponseList(publishedCourses)).thenReturn(courseResponses);

        List<CourseResponse> result = courseService.getPublishedCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsPublished());

        verify(courseRepository, times(1)).findByIsPublishedTrue();
    }

    @Test
    void updateCourse_ShouldUpdateCourseSuccessfully() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toResponse(any(Course.class))).thenReturn(courseResponse);

        CourseResponse result = courseService.updateCourse(1L, courseRequest);

        assertNotNull(result);

        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void deleteCourse_ShouldDeleteCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void publishCourse_ShouldPublishCourse() {
        course.setPublished(false);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toResponse(any(Course.class))).thenReturn(courseResponse);

        CourseResponse result = courseService.publishCourse(1L);

        assertNotNull(result);
        assertTrue(result.getIsPublished());

        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void unpublishCourse_ShouldUnpublishCourse() {
        course.setPublished(true);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toResponse(any(Course.class))).thenReturn(courseResponse);

        CourseResponse result = courseService.unpublishCourse(1L);

        assertNotNull(result);

        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void searchCourses_ShouldReturnMatchingCourses() {
        List<Course> courses = Arrays.asList(course);
        List<CourseResponse> courseResponses = Arrays.asList(courseResponse);

        when(courseRepository.searchByKeyword("Java")).thenReturn(courses);
        when(courseMapper.toResponseList(courses)).thenReturn(courseResponses);

        List<CourseResponse> result = courseService.searchCourses("Java");

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(courseRepository, times(1)).searchByKeyword("Java");
    }
}