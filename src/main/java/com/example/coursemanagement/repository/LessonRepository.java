package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByCourseIdOrderByOrderNumberAsc(Long courseId);

    Optional<Lesson> findByCourseIdAndOrderNumber(Long courseId, Integer orderNumber);

    @Query("SELECT MAX(l.orderNumber) FROM Lesson l WHERE l.course.id = :courseId")
    Optional<Integer> findMaxOrderNumberByCourseId(@Param("courseId") Long courseId);

    long countByCourseId(Long courseId);
}