package com.example.coursemanagement.mapper;

import com.example.coursemanagement.config.CentralMapperConfig;
import com.example.coursemanagement.dto.request.EnrollmentRequest;
import com.example.coursemanagement.dto.response.EnrollmentResponse;
import com.example.coursemanagement.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class, uses = {UserMapper.class, CourseMapper.class})
public interface EnrollmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    @Mapping(target = "progress", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    Enrollment toEntity(EnrollmentRequest request);

    EnrollmentResponse toResponse(Enrollment enrollment);

    List<EnrollmentResponse> toResponseList(List<Enrollment> enrollments);
}