package com.example.coursemanagement.mapper;

import com.example.coursemanagement.config.CentralMapperConfig;
import com.example.coursemanagement.dto.request.LessonRequest;
import com.example.coursemanagement.dto.response.LessonResponse;
import com.example.coursemanagement.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "course", ignore = true)
    Lesson toEntity(LessonRequest request);

    @Mapping(target = "courseId", source = "course.id")
    LessonResponse toResponse(Lesson lesson);

    List<LessonResponse> toResponseList(List<Lesson> lessons);
}