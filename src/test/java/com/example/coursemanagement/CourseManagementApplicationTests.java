package com.example.coursemanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class CourseManagementApplicationTest {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> {});
    }

    @Test
    void main_ShouldStartApplication() {
        assertDoesNotThrow(() -> CourseManagementApplication.main(new String[]{}));
    }
}