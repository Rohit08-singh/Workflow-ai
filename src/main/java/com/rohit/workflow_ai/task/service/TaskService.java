package com.rohit.workflow_ai.task.service;

import com.rohit.workflow_ai.activity.service.ActivityService;
import com.rohit.workflow_ai.common.enums.Status;
import com.rohit.workflow_ai.exception.ErrorCode;
import com.rohit.workflow_ai.exception.custom.AppException;
import com.rohit.workflow_ai.project.entity.Project;
import com.rohit.workflow_ai.project.repository.ProjectRepository;
import com.rohit.workflow_ai.task.dto.AssignTaskRequest;
import com.rohit.workflow_ai.task.dto.CreateTaskRequest;
import com.rohit.workflow_ai.task.dto.TaskResponse;
import com.rohit.workflow_ai.task.dto.UpdateTaskRequest;
import com.rohit.workflow_ai.task.dto.UpdateTaskStatusRequest;
import com.rohit.workflow_ai.task.entity.Task;
import com.rohit.workflow_ai.task.mapper.TaskMapper;
import com.rohit.workflow_ai.task.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ActivityService activityService;

    public TaskService(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            ActivityService activityService) {

        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.activityService = activityService;
    }

    // =========================
    // CREATE TASK
    // =========================

    public TaskResponse createTask(
            CreateTaskRequest request,
            ObjectId companyId,
            ObjectId performedBy) {

        ObjectId projectId =
                new ObjectId(request.getProjectId());

        Project project = projectRepository
                .findByIdAndCompanyId(
                        projectId,
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(
                                ErrorCode.PROJECT_NOT_FOUND
                        ));

        Task task = TaskMapper.toEntity(
                request,
                companyId,
                projectId
        );

        Task savedTask =
                taskRepository.save(task);

        // Activity log
        activityService.createActivity(
                companyId,
                performedBy,
                "TASK_CREATED",
                "Task '" + savedTask.getTitle() + "' was created",
                projectId,
                savedTask.getId(),
                null,
                savedTask.getAssignedUserId()
        );

        return TaskMapper.toResponse(savedTask);
    }

    // =========================
    // UPDATE TASK
    // =========================

    public TaskResponse updateTask(
            String taskId,
            UpdateTaskRequest request,
            ObjectId companyId,
            ObjectId performedBy
    ) {

        Task task = taskRepository
                .findByIdAndCompanyId(
                        new ObjectId(taskId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(
                                ErrorCode.TASK_NOT_FOUND
                        ));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        task.setAssignedUserId(
                request.getAssignedUserId() == null
                        || request.getAssignedUserId().isBlank()
                        ? null
                        : new ObjectId(
                        request.getAssignedUserId()
                )
        );

        task.setDueDate(request.getDueDate());
        task.setStatus(request.getStatus());

        Task updatedTask =
                taskRepository.save(task);

        // Activity log
        activityService.createActivity(
                companyId,
                performedBy,
                "TASK_UPDATED",
                "Task '" + updatedTask.getTitle() + "' was updated",
                updatedTask.getProjectId(),
                updatedTask.getId(),
                null,
                updatedTask.getAssignedUserId()
        );

        return TaskMapper.toResponse(updatedTask);
    }

    // =========================
    // GET TASKS BY PROJECT
    // =========================

    public List<TaskResponse> getTasksByProject(
            String projectId,
            ObjectId companyId
    ) {

        Project project = projectRepository
                .findByIdAndCompanyId(
                        new ObjectId(projectId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(
                                ErrorCode.PROJECT_NOT_FOUND
                        ));

        return taskRepository
                .findByProjectIdAndCompanyId(
                        project.getId(),
                        companyId
                )
                .stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    // =========================
    // UPDATE TASK STATUS
    // =========================

    public TaskResponse updateTaskStatus(
            String taskId,
            UpdateTaskStatusRequest request,
            ObjectId companyId,
            ObjectId performedBy
    ) {

        Task task = taskRepository
                .findByIdAndCompanyId(
                        new ObjectId(taskId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(
                                ErrorCode.TASK_NOT_FOUND
                        ));

        task.setStatus(request.getStatus());

        Task updatedTask =
                taskRepository.save(task);

        // Activity log
        activityService.createActivity(
                companyId,
                performedBy,
                "TASK_STATUS_UPDATED",
                "Task '" + updatedTask.getTitle()
                        + "' status changed to "
                        + updatedTask.getStatus(),
                updatedTask.getProjectId(),
                updatedTask.getId(),
                null,
                updatedTask.getAssignedUserId()
        );

        return TaskMapper.toResponse(updatedTask);
    }

    // =========================
    // GET TASK BY ID
    // =========================

    public TaskResponse getTaskById(
            String taskId,
            ObjectId companyId
    ) {

        Task task = taskRepository
                .findByIdAndCompanyId(
                        new ObjectId(taskId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(
                                ErrorCode.TASK_NOT_FOUND
                        ));

        return TaskMapper.toResponse(task);
    }

    // =========================
    // DELETE TASK
    // =========================

    public void deleteTask(
            String taskId,
            ObjectId companyId,
            ObjectId performedBy
    ) {

        Task task = taskRepository
                .findByIdAndCompanyId(
                        new ObjectId(taskId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(
                                ErrorCode.TASK_NOT_FOUND
                        ));

        task.setRecordStatus(Status.DELETED);

        Task deletedTask =
                taskRepository.save(task);

        // Activity log
        activityService.createActivity(
                companyId,
                performedBy,
                "TASK_DELETED",
                "Task '" + deletedTask.getTitle() + "' was deleted",
                deletedTask.getProjectId(),
                deletedTask.getId(),
                null,
                deletedTask.getAssignedUserId()
        );
    }

    // =========================
    // ASSIGN TASK
    // =========================

    public TaskResponse assignTask(
            String taskId,
            AssignTaskRequest request,
            ObjectId companyId,
            ObjectId performedBy
    ) {

        Task task = taskRepository
                .findByIdAndCompanyId(
                        new ObjectId(taskId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(
                                ErrorCode.TASK_NOT_FOUND
                        ));

        ObjectId assignedUserId =
                new ObjectId(request.getAssignedUserId());

        task.setAssignedUserId(assignedUserId);

        Task updatedTask =
                taskRepository.save(task);

        // Activity log
        activityService.createActivity(
                companyId,
                performedBy,
                "TASK_ASSIGNED",
                "Task '" + updatedTask.getTitle()
                        + "' was assigned to a user",
                updatedTask.getProjectId(),
                updatedTask.getId(),
                null,
                assignedUserId
        );

        return TaskMapper.toResponse(updatedTask);
    }
}