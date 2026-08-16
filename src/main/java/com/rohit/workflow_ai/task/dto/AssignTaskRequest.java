package com.rohit.workflow_ai.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignTaskRequest {

    @NotBlank(message = "Assigned User ID is required")
    private String assignedUserId;
}