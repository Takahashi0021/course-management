package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByCourseId(Long courseId);

    List<Assignment> findByCourseIdAndDueDateAfter(Long courseId, LocalDateTime date);

    @Query("SELECT a FROM Assignment a WHERE a.course.instructor.id = :instructorId")
    List<Assignment> findByInstructorId(@Param("instructorId") Long instructorId);

    @Query("SELECT a FROM Assignment a WHERE a.dueDate < :now AND a.id NOT IN " +
            "(SELECT s.assignment.id FROM AssignmentSubmission s WHERE s.student.id = :studentId)")
    List<Assignment> findOverdueAssignmentsForStudent(@Param("studentId") Long studentId,
                                                      @Param("now") LocalDateTime now);
}