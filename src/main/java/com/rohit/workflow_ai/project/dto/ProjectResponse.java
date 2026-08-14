package com.rohit.workflow_ai.project.dto;

import com.rohit.workflow_ai.common.enums.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProjectResponse {

    private String id;

    private String clientId;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private ProjectStatus status;
}