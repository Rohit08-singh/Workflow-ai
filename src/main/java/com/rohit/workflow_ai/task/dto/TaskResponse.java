package com.rohit.workflow_ai.task.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskResponse {

    private String id;

    private String projectId;

    private String title;

    private String description;

    private String assignedUserId;

    private LocalDate dueDate;

    private String status;
}