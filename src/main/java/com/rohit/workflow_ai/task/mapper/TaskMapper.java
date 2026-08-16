package com.rohit.workflow_ai.task.mapper;

import com.rohit.workflow_ai.task.dto.CreateTaskRequest;
import com.rohit.workflow_ai.task.dto.TaskResponse;
import com.rohit.workflow_ai.task.entity.Task;
import org.bson.types.ObjectId;

public class TaskMapper {

    private TaskMapper() {
    }

    public static Task toEntity(
            CreateTaskRequest request,
            ObjectId companyId,
            ObjectId projectId) {

        return Task.builder()
                .companyId(companyId)
                .projectId(projectId)
                .title(request.getTitle())
                .description(request.getDescription())
                .assignedUserId(
                        request.getAssignedUserId() == null ||
                                request.getAssignedUserId().isBlank()
                                ? null
                                : new ObjectId(request.getAssignedUserId())
                )
                .dueDate(request.getDueDate())
                .build();
    }

    public static TaskResponse toResponse(Task task) {

        return TaskResponse.builder()
                .id(task.getId().toHexString())
                .projectId(task.getProjectId().toHexString())
                .title(task.getTitle())
                .description(task.getDescription())
                .assignedUserId(
                        task.getAssignedUserId() == null
                                ? null
                                : task.getAssignedUserId().toHexString()
                )
                .dueDate(task.getDueDate())
                .status(task.getStatus().name())
                .build();
    }
}