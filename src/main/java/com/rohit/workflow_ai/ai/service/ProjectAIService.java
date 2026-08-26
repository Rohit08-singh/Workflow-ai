package com.rohit.workflow_ai.ai.service;

import com.rohit.workflow_ai.ai.dto.ProjectAIResponse;
import com.rohit.workflow_ai.project.entity.Project;
import com.rohit.workflow_ai.project.repository.ProjectRepository;
import com.rohit.workflow_ai.task.entity.Task;
import com.rohit.workflow_ai.task.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectAIService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final GeminiService geminiService;

    public ProjectAIService(
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            GeminiService geminiService) {

        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.geminiService = geminiService;
    }

    public ProjectAIResponse askProjectAssistant(
            ObjectId companyId,
            String userPrompt) {

        List<Project> projects =
                projectRepository.findByCompanyId(companyId);

        List<Task> tasks =
                taskRepository.findByCompanyId(companyId);

        String finalPrompt = """
                You are an AI project management assistant
                inside a project management SaaS.

                Answer the user's question using ONLY the
                company data provided below.

                Do not invent projects, tasks, employees,
                dates or statistics.

                If the available data is insufficient,
                clearly say that.

                COMPANY PROJECT DATA:
                %s

                COMPANY TASK DATA:
                %s

                USER QUESTION:
                %s

                Give a concise and useful business-oriented answer.
                """.formatted(
                buildProjectContext(projects),
                buildTaskContext(tasks),
                userPrompt
        );

        String response =
                geminiService.generateContent(finalPrompt);

        return ProjectAIResponse.builder()
                .response(response)
                .build();
    }

    private String buildProjectContext(
            List<Project> projects) {

        if (projects.isEmpty()) {
            return "No projects found.";
        }

        StringBuilder context =
                new StringBuilder();

        for (Project project : projects) {

            context.append("""
                    
                    Project:
                    ID: %s
                    Name: %s
                    Description: %s
                    Status: %s
                    Start Date: %s
                    End Date: %s
                    Client ID: %s
                    """.formatted(
                    project.getId(),
                    project.getName(),
                    project.getDescription(),
                    project.getStatus(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getClientId()
            ));
        }

        return context.toString();
    }

    private String buildTaskContext(
            List<Task> tasks) {

        if (tasks.isEmpty()) {
            return "No tasks found.";
        }

        StringBuilder context =
                new StringBuilder();

        for (Task task : tasks) {

            context.append("""
                    
                    Task:
                    ID: %s
                    Project ID: %s
                    Title: %s
                    Description: %s
                    Status: %s
                    Assigned User ID: %s
                    Due Date: %s
                    """.formatted(
                    task.getId(),
                    task.getProjectId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getAssignedUserId(),
                    task.getDueDate()
            ));
        }

        return context.toString();
    }
}