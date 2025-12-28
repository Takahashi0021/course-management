package com.example.coursemanagement.mapper;

import com.example.coursemanagement.config.CentralMapperConfig;
import com.example.coursemanagement.dto.request.AssignmentRequest;
import com.example.coursemanagement.dto.response.AssignmentResponse;
import com.example.coursemanagement.entity.Assignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface AssignmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "submissions", ignore = true)
    Assignment toEntity(AssignmentRequest request);

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "submissionCount", source = "submissions", qualifiedByName = "countSubmissions")
    AssignmentResponse toResponse(Assignment assignment);

    List<AssignmentResponse> toResponseList(List<Assignment> assignments);

    @Named("countSubmissions")
    default Integer countSubmissions(List<?> submissions) {
        return submissions != null ? submissions.size() : 0;
    }
}