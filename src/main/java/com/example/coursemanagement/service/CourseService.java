package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.CourseRequest;
import com.example.coursemanagement.dto.response.CourseResponse;

import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CourseRequest request);

    CourseResponse updateCourse(Long id, CourseRequest request);

    void deleteCourse(Long id);

    CourseResponse getCourseById(Long id);

    List<CourseResponse> getAllCourses();

    List<CourseResponse> getPublishedCourses();

    List<CourseResponse> getCoursesByInstructor(Long instructorId);

    List<CourseResponse> getMyCourses();

    CourseResponse publishCourse(Long id);

    CourseResponse unpublishCourse(Long id);

    List<CourseResponse> searchCourses(String keyword);

    Long countCourses();
}