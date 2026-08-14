package com.rohit.workflow_ai.project.mapper;

import com.rohit.workflow_ai.common.enums.ProjectStatus;
import com.rohit.workflow_ai.project.dto.CreateProjectRequest;
import com.rohit.workflow_ai.project.dto.ProjectResponse;
import com.rohit.workflow_ai.project.entity.Project;
import org.bson.types.ObjectId;

public class ProjectMapper {

    private ProjectMapper() {
    }

    public static Project toEntity(CreateProjectRequest request,
                                   ObjectId companyId,
                                   ObjectId clientId) {

        return Project.builder()
                .companyId(companyId)
                .clientId(clientId)
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(ProjectStatus.PLANNED)
                .build();
    }

    public static ProjectResponse toResponse(Project project) {

        return ProjectResponse.builder()
                .id(project.getId().toHexString())
                .clientId(project.getClientId().toHexString())
                .name(project.getName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .status(project.getStatus())
                .build();
    }
}