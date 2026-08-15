package com.rohit.workflow_ai.task.controller;

import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import com.rohit.workflow_ai.task.dto.CreateTaskRequest;
import com.rohit.workflow_ai.task.dto.TaskResponse;
import com.rohit.workflow_ai.task.dto.UpdateTaskRequest;
import com.rohit.workflow_ai.task.service.TaskService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CreateTaskRequest request) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        TaskResponse response =
                taskService.createTask(
                        request,
                        companyId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseUtil.created(
                                response,
                                "Task created successfully"
                        )
                );
    }
    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable String taskId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody UpdateTaskRequest request) {

        System.out.println("Update API Called");

        ObjectId companyId = currentUser.getUser().getCompanyId();

        TaskResponse response =
                taskService.updateTask(taskId, request, companyId);

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Task updated successfully"
                )
        );
    }
}