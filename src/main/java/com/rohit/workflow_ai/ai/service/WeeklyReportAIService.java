package com.rohit.workflow_ai.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohit.workflow_ai.ai.dto.WeeklyReportResponse;
import com.rohit.workflow_ai.common.enums.TaskStatus;
import com.rohit.workflow_ai.project.entity.Project;
import com.rohit.workflow_ai.project.repository.ProjectRepository;
import com.rohit.workflow_ai.task.entity.Task;
import com.rohit.workflow_ai.task.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeeklyReportAIService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public WeeklyReportAIService(
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            GeminiService geminiService,
            ObjectMapper objectMapper) {

        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
    }

    public WeeklyReportResponse generateWeeklyReport(
            ObjectId companyId,
            String projectId) {

        ObjectId projectObjectId;

        try {
            projectObjectId = new ObjectId(projectId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid project ID");
        }

        /*
         * IMPORTANT:
         * Always verify the project belongs to
         * the authenticated company.
         */
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

        LocalDate today = LocalDate.now();

        LocalDate weekStart =
                today.with(
                        TemporalAdjusters.previousOrSame(
                                java.time.DayOfWeek.MONDAY
                        )
                );

        LocalDate weekEnd =
                today.with(
                        TemporalAdjusters.nextOrSame(
                                java.time.DayOfWeek.SUNDAY
                        )
                );

        String taskContext =
                buildTaskContext(tasks);

        String prompt = """
                You are an AI project manager inside
                a project management SaaS.

                Generate a weekly project report using ONLY
                the provided project and task information.

                Do NOT invent:
                - tasks
                - employees
                - dates
                - statistics
                - completed work
                - project information

                Current report period:
                %s to %s

                PROJECT:
                Name: %s
                Description: %s
                Status: %s
                Start Date: %s
                End Date: %s

                TASKS:
                %s

                Return EXACTLY this JSON structure:

                {
                  "projectName": "project name",
                  "period": "period",
                  "progressSummary": "short summary",
                  "completedWork": [],
                  "pendingWork": [],
                  "overdueWork": [],
                  "risks": [],
                  "nextWeekFocus": []
                }

                Keep the report concise,
                factual and business-oriented.

                If there is insufficient activity,
                clearly state that instead of inventing activity.
                """.formatted(
                weekStart,
                weekEnd,
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getStartDate(),
                project.getEndDate(),
                taskContext
        );

        String aiResponse =
                geminiService.generateContent(prompt);

        return parseResponse(aiResponse);
    }

    private String buildTaskContext(List<Task> tasks) {

        if (tasks.isEmpty()) {
            return "No tasks exist for this project.";
        }

        StringBuilder context =
                new StringBuilder();

        LocalDate today = LocalDate.now();

        for (Task task : tasks) {

            boolean overdue =
                    task.getDueDate() != null
                            && task.getDueDate().isBefore(today)
                            && task.getStatus()
                            != TaskStatus.DONE;

            context.append("""
                    
                    Task ID: %s
                    Title: %s
                    Description: %s
                    Status: %s
                    Assigned User ID: %s
                    Due Date: %s
                    Overdue: %s
                    """.formatted(
                    task.getId(),
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

    private WeeklyReportResponse parseResponse(
            String response) {

        try {

            String json = response.trim();

            /*
             * Gemini sometimes returns:
             *
             * ```json
             * {...}
             * ```
             *
             * Remove the markdown wrapper.
             */
            if (json.startsWith("```")) {

                json = json
                        .replaceFirst(
                                "^```json\\s*",
                                ""
                        )
                        .replaceFirst(
                                "^```\\s*",
                                ""
                        )
                        .replaceFirst(
                                "\\s*```$",
                                ""
                        );
            }

            return objectMapper.readValue(
                    json,
                    WeeklyReportResponse.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse AI weekly report",
                    e
            );
        }
    }
}