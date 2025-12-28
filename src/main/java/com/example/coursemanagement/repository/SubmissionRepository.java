package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    Optional<AssignmentSubmission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    List<AssignmentSubmission> findByAssignmentId(Long assignmentId);

    List<AssignmentSubmission> findByStudentId(Long studentId);

    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.course.instructor.id = :instructorId")
    List<AssignmentSubmission> findByInstructorId(@Param("instructorId") Long instructorId);

    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.course.id = :courseId")
    List<AssignmentSubmission> findByCourseId(@Param("courseId") Long courseId);

    boolean existsByAssignmentIdAndStudentId(Long assignmentId, Long studentId);
}