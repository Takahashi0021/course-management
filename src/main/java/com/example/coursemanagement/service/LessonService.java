package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.LessonRequest;
import com.example.coursemanagement.dto.response.LessonResponse;

import java.util.List;

public interface LessonService {

    LessonResponse createLesson(LessonRequest request);

    LessonResponse updateLesson(Long id, LessonRequest request);

    void deleteLesson(Long id);

    LessonResponse getLessonById(Long id);

    List<LessonResponse> getLessonsByCourse(Long courseId);

    void reorderLessons(Long courseId, List<Long> lessonIds);
}