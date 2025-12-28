package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByInstructorId(Long instructorId);

    List<Course> findByIsPublishedTrue();

    List<Course> findByInstructorIdAndIsPublishedTrue(Long instructorId);

    @Query("SELECT c FROM Course c WHERE c.title ILIKE %:keyword% OR c.description ILIKE %:keyword%")
    List<Course> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT c FROM Course c WHERE c.price = 0 AND c.isPublished = true")
    List<Course> findFreePublishedCourses();

    boolean existsByTitleAndInstructorId(String title, Long instructorId);
}