Course Management System
A comprehensive educational platform built with Spring Boot for managing courses, assignments, and student enrollments with secure role-based access control.

Project Overview
Goal: Create a learning management system where students can enroll in courses, instructors can create educational content, and administrators can manage the entire platform.

Database Structure (Entities)
User: Stores user information including email, encrypted password, first name, last name, and role.
UserRole: Enum defining user permissions (STUDENT, INSTRUCTOR, ADMIN).
Course: Educational courses created by instructors with title, description, and instructor relationship.
Enrollment: Records student enrollment in specific courses.
Lesson: Individual lessons within a course, containing educational content.
Assignment: Tasks created by instructors for students to complete.
AssignmentSubmission: Records student submissions for assignments, including grades and feedback.

Security and Roles
ROLE_STUDENT: Can browse courses, enroll in available courses, submit assignments, and view personal grades. Cannot create or modify course content.

ROLE_INSTRUCTOR: Can create and manage courses, create lessons and assignments, grade student submissions, and view enrolled students. Cannot change user roles or access system administration.

ROLE_ADMIN: Has all instructor permissions plus additional administrative rights. Can manage all users, change roles, deactivate/activate accounts, and access system-wide management functions.

Security Features: Passwords are encrypted using BCrypt. JWT tokens are used for authentication with claims containing user information and role. Access control is enforced at both endpoint and method levels.

DTO and Mappers
DTO (Data Transfer Object): We use DTOs to separate API layer from database entities, preventing exposure of sensitive data and controlling data flow.

Request DTO: Objects received from client requests, containing validated input data for operations like registration, course creation, and assignment submission.

Response DTO: Objects returned to clients, containing only necessary information formatted for API responses.

MapStruct: Implementation of object mapping that automatically converts between entity and DTO objects, reducing boilerplate code and improving maintainability.

Practical Part: Business Logic
Course Enrollment Logic: When a student enrolls in a course, the system verifies course availability and prevents duplicate enrollments.

Assignment Submission Logic: When a student submits an assignment, the system checks if the student is enrolled in the course, validates the submission deadline, and prevents multiple submissions for the same assignment.

Grading Logic: When an instructor grades a submission, the system validates that the grader has appropriate permissions, ensures points are within valid range, and updates the submission status.

Automatic Status Management: Systems automatically manage entity states, such as marking assignments as late if submitted after deadlines.

Transactional Operations: Critical operations like assignment submission and grading are transactional. If any part fails, the entire operation rolls back to maintain data consistency and prevent partial updates.

Role-Based Service Methods: Business logic methods include role validation, ensuring users can only perform actions permitted by their role, with clear exception messages for unauthorized attempts.

Data Integrity: Relationships between entities are maintained with proper cascading and validation, ensuring referential integrity throughout the system.
