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

        // =====================================================
        // GET COMPANY PROJECTS
        // =====================================================

        List<Project> projects =
                projectRepository.findByCompanyId(companyId);

        // =====================================================
        // GET COMPANY TASKS
        // =====================================================

        List<Task> tasks =
                taskRepository.findByCompanyId(companyId);

        // =====================================================
        // DEBUG LOGS
        // =====================================================

        System.out.println("====================================");
        System.out.println("PROJECT AI SERVICE DEBUG");
        System.out.println("Company ID: " + companyId);
        System.out.println("Projects found: " + projects.size());
        System.out.println("Tasks found: " + tasks.size());

        // Print project details
        for (Project project : projects) {

            System.out.println(
                    "Project: " +
                            project.getName() +
                            " | ID: " +
                            project.getId() +
                            " | Company ID: " +
                            project.getCompanyId() +
                            " | Status: " +
                            project.getStatus()
            );
        }

        // Print task details
        for (Task task : tasks) {

            System.out.println(
                    "Task: " +
                            task.getTitle() +
                            " | ID: " +
                            task.getId() +
                            " | Project ID: " +
                            task.getProjectId() +
                            " | Assigned User ID: " +
                            task.getAssignedUserId() +
                            " | Status: " +
                            task.getStatus()
            );
        }

        System.out.println("====================================");

        // =====================================================
        // BUILD AI PROMPT
        // =====================================================

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

        // =====================================================
        // SEND PROMPT TO GEMINI
        // =====================================================

        String response =
                geminiService.generateContent(finalPrompt);

        // =====================================================
        // RETURN RESPONSE
        // =====================================================

        return ProjectAIResponse.builder()
                .response(response)
                .build();
    }

    // =========================================================
    // BUILD PROJECT CONTEXT
    // =========================================================

    private String buildProjectContext(
            List<Project> projects) {

        if (projects.isEmpty()) {
            return "No projects found.";
        }

        StringBuilder context = new StringBuilder();

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
                    Company ID: %s
                    """.formatted(
                    project.getId(),
                    project.getName(),
                    project.getDescription(),
                    project.getStatus(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getClientId(),
                    project.getCompanyId()
            ));
        }

        return context.toString();
    }

    // =========================================================
    // BUILD TASK CONTEXT
    // =========================================================

    private String buildTaskContext(
            List<Task> tasks) {

        if (tasks.isEmpty()) {
            return "No tasks found.";
        }

        StringBuilder context = new StringBuilder();

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
                    Company ID: %s
                    """.formatted(
                    task.getId(),
                    task.getProjectId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getAssignedUserId(),
                    task.getDueDate(),
                    task.getCompanyId()
            ));
        }

        return context.toString();
    }
}