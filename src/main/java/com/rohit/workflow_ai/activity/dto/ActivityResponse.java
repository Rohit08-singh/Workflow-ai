package com.rohit.workflow_ai.activity.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ActivityResponse {

    private String id;

    private String type;

    private String message;

    private String performedBy;

    private String projectId;

    private String taskId;

    private String clientId;

    private String targetUserId;

    private LocalDateTime createdAt;
}