package com.rohit.workflow_ai.task.controller;

import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import com.rohit.workflow_ai.task.dto.*;
import com.rohit.workflow_ai.task.service.TaskService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByProject(

            @PathVariable String projectId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        List<TaskResponse> response =
                taskService.getTasksByProject(
                        projectId,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Tasks fetched successfully"
                )
        );
    }


    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskStatus(
            @PathVariable String taskId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {

        ObjectId companyId = currentUser.getUser().getCompanyId();

        TaskResponse response =
                taskService.updateTaskStatus(
                        taskId,
                        request,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Task status updated successfully"
                )
        );
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(

            @PathVariable String taskId,

            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        TaskResponse response =
                taskService.getTaskById(
                        taskId,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Task fetched successfully"
                )
        );
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(

            @PathVariable String taskId,

            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        taskService.deleteTask(
                taskId,
                companyId
        );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        null,
                        "Task deleted successfully"
                )
        );
    }

    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<ApiResponse<TaskResponse>> assignTask(
            @PathVariable String taskId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody AssignTaskRequest request
    ) {

        ObjectId companyId = currentUser.getUser().getCompanyId();

        TaskResponse response =
                taskService.assignTask(
                        taskId,
                        request,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Task assigned successfully"
                )
        );
    }
}