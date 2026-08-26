package com.rohit.workflow_ai.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeeklyReportRequest {

    @NotBlank(message = "Project ID is required")
    private String projectId;
}