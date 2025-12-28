package com.example.coursemanagement.mapper;

import com.example.coursemanagement.config.CentralMapperConfig;
import com.example.coursemanagement.dto.request.CourseRequest;
import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = CentralMapperConfig.class, uses = {UserMapper.class})
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    Course toEntity(CourseRequest request);

    @Mapping(target = "studentCount", source = "enrollments", qualifiedByName = "countStudents")
    @Mapping(target = "lessonCount", source = "lessons", qualifiedByName = "countLessons")
    CourseResponse toResponse(Course course);

    List<CourseResponse> toResponseList(List<Course> courses);

    @Named("countStudents")
    default Integer countStudents(List<?> enrollments) {
        return enrollments != null ? enrollments.size() : 0;
    }

    @Named("countLessons")
    default Integer countLessons(List<?> lessons) {
        return lessons != null ? lessons.size() : 0;
    }
}