package com.rohit.workflow_ai.task.service;

import com.rohit.workflow_ai.exception.ErrorCode;
import com.rohit.workflow_ai.exception.custom.AppException;
import com.rohit.workflow_ai.project.entity.Project;
import com.rohit.workflow_ai.project.repository.ProjectRepository;
import com.rohit.workflow_ai.task.dto.CreateTaskRequest;
import com.rohit.workflow_ai.task.dto.TaskResponse;
import com.rohit.workflow_ai.task.dto.UpdateTaskRequest;
import com.rohit.workflow_ai.task.entity.Task;
import com.rohit.workflow_ai.task.mapper.TaskMapper;
import com.rohit.workflow_ai.task.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(
            TaskRepository taskRepository,
            ProjectRepository projectRepository) {

        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public TaskResponse createTask(
            CreateTaskRequest request,
            ObjectId companyId) {

        // 1. Convert project ID
        ObjectId projectId = new ObjectId(request.getProjectId());

        // 2. Check whether project exists
        Project project = projectRepository
                .findByIdAndCompanyId(projectId, companyId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.PROJECT_NOT_FOUND));

        // 3. Create task
        Task task = TaskMapper.toEntity(
                request,
                companyId,
                projectId
        );

        // 4. Save task
        Task savedTask = taskRepository.save(task);

        // 5. Return response
        return TaskMapper.toResponse(savedTask);
    }

    public TaskResponse updateTask(
            String taskId,
            UpdateTaskRequest request,
            ObjectId companyId
    ) {

        Task task = taskRepository
                .findByIdAndCompanyId(
                        new ObjectId(taskId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(ErrorCode.TASK_NOT_FOUND));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAssignedTo(request.getAssignedTo());
        task.setDueDate(request.getDueDate());
        task.setStatus(request.getStatus());

        Task updatedTask = taskRepository.save(task);

        return TaskMapper.toResponse(updatedTask);
    }
}