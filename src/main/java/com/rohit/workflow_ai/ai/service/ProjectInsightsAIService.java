package com.rohit.workflow_ai.ai.service;

import com.rohit.workflow_ai.ai.dto.ProjectInsightsResponse;
import com.rohit.workflow_ai.project.entity.Project;
import com.rohit.workflow_ai.project.repository.ProjectRepository;
import com.rohit.workflow_ai.task.entity.Task;
import com.rohit.workflow_ai.task.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectInsightsAIService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final GeminiService geminiService;

    public ProjectInsightsAIService(
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            GeminiService geminiService) {

        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.geminiService = geminiService;
    }

    public ProjectInsightsResponse generateInsights(
            ObjectId companyId,
            String projectId) {

        ObjectId projectObjectId;

        try {
            projectObjectId = new ObjectId(projectId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid project ID");
        }

        // IMPORTANT:
        // Project is fetched using BOTH projectId and companyId.
        // This protects multi-tenancy.
        Project project =
                projectRepository.findByIdAndCompanyId(
                        projectObjectId,
                        companyId
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Project not found"
                        )
                );

        List<Task> tasks =
                taskRepository.findByProjectIdAndCompanyId(
                        projectObjectId,
                        companyId
                );

        String context = buildContext(project, tasks);

        String prompt = """
                You are an AI project manager inside
                a multi-tenant project management SaaS.

                Analyze ONLY the project information provided below.

                Do not invent:
                - tasks
                - dates
                - employees
                - statistics
                - project information

                PROJECT DATA:
                %s

                TASK DATA:
                %s

                Return the response in EXACTLY this JSON structure:

                {
                  "summary": "short project summary",
                  "keyInsights": [
                    "insight 1",
                    "insight 2"
                  ],
                  "priorityActions": [
                    "action 1",
                    "action 2"
                  ],
                  "nextStep": "single recommended next step"
                }

                Keep the response concise and business-oriented.
                """.formatted(
                buildProjectContext(project),
                buildTaskContext(tasks)
        );

        String aiResponse =
                geminiService.generateContent(prompt);

        return parseResponse(aiResponse);
    }

    private String buildProjectContext(Project project) {

        return """
                Project ID: %s
                Project Name: %s
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
        );
    }

    private String buildTaskContext(List<Task> tasks) {

        if (tasks.isEmpty()) {
            return "No tasks have been created for this project.";
        }

        StringBuilder context = new StringBuilder();

        for (Task task : tasks) {

            boolean overdue =
                    task.getDueDate() != null
                            && task.getDueDate()
                            .isBefore(LocalDate.now())
                            && task.getStatus()
                            != com.rohit.workflow_ai.common.enums.TaskStatus.DONE;

            context.append("""
                    
                    Task ID: %s
                    Project ID: %s
                    Title: %s
                    Description: %s
                    Status: %s
                    Assigned User ID: %s
                    Due Date: %s
                    Overdue: %s
                    """.formatted(
                    task.getId(),
                    task.getProjectId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getAssignedUserId(),
                    task.getDueDate(),
                    overdue
            ));
        }

        return context.toString();
    }

    private String buildContext(
            Project project,
            List<Task> tasks) {

        return "Project: "
                + project.getName()
                + ", Tasks: "
                + tasks.size();
    }

    private ProjectInsightsResponse parseResponse(
            String response) {

        try {

            String json = response.trim();

            // Remove markdown code fences if Gemini adds them.
            if (json.startsWith("```")) {
                json = json
                        .replaceFirst("^```json\\s*", "")
                        .replaceFirst("^```\\s*", "")
                        .replaceFirst("\\s*```$", "");
            }

            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();

            return mapper.readValue(
                    json,
                    ProjectInsightsResponse.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse AI project insights",
                    e
            );
        }
    }
}