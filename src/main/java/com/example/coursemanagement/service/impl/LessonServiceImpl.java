package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.dto.request.LessonRequest;
import com.example.coursemanagement.dto.response.LessonResponse;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.Lesson;
import com.example.coursemanagement.exception.ResourceNotFoundException;
import com.example.coursemanagement.mapper.LessonMapper;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.LessonRepository;
import com.example.coursemanagement.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    @Override
    @Transactional
    public LessonResponse createLesson(LessonRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        lessonRepository.findByCourseIdAndOrderNumber(request.getCourseId(), request.getOrderNumber())
                .ifPresent(lesson -> {
                    throw new IllegalArgumentException("Lesson with order number " + request.getOrderNumber() +
                            " already exists in this course");
                });

        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);

        Lesson savedLesson = lessonRepository.save(lesson);
        return lessonMapper.toResponse(savedLesson);
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(Long id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        if (!lesson.getOrderNumber().equals(request.getOrderNumber())) {
            lessonRepository.findByCourseIdAndOrderNumber(request.getCourseId(), request.getOrderNumber())
                    .ifPresent(existingLesson -> {
                        if (!existingLesson.getId().equals(id)) {
                            throw new IllegalArgumentException("Lesson with order number " + request.getOrderNumber() +
                                    " already exists in this course");
                        }
                    });
        }

        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setCourse(course);
        lesson.setOrderNumber(request.getOrderNumber());

        Lesson updatedLesson = lessonRepository.save(lesson);
        return lessonMapper.toResponse(updatedLesson);
    }

    @Override
    @Transactional
    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
        lessonRepository.delete(lesson);
    }

    @Override
    public LessonResponse getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
        return lessonMapper.toResponse(lesson);
    }

    @Override
    public List<LessonResponse> getLessonsByCourse(Long courseId) {
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByOrderNumberAsc(courseId);
        return lessonMapper.toResponseList(lessons);
    }

    @Override
    @Transactional
    public void reorderLessons(Long courseId, List<Long> lessonIds) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        for (int i = 0; i < lessonIds.size(); i++) {
            Long lessonId = lessonIds.get(i);
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));

            if (!lesson.getCourse().getId().equals(courseId)) {
                throw new IllegalArgumentException("Lesson " + lessonId + " does not belong to course " + courseId);
            }

            lesson.setOrderNumber(i + 1);
            lessonRepository.save(lesson);
        }
    }
}