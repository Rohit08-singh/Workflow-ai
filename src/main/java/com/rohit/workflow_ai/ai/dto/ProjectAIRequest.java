package com.rohit.workflow_ai.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectAIRequest {

    @NotBlank(message = "Prompt is required")
    private String prompt;
}