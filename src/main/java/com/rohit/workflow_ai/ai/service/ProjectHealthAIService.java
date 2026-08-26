package com.rohit.workflow_ai.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohit.workflow_ai.ai.dto.ProjectHealthResponse;
import com.rohit.workflow_ai.common.enums.ProjectStatus;
import com.rohit.workflow_ai.common.enums.TaskStatus;
import com.rohit.workflow_ai.project.entity.Project;
import com.rohit.workflow_ai.project.repository.ProjectRepository;
import com.rohit.workflow_ai.task.entity.Task;
import com.rohit.workflow_ai.task.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ProjectHealthAIService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public ProjectHealthAIService(
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            GeminiService geminiService,
            ObjectMapper objectMapper) {

        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.geminiService = geminiService;
        this.objectMapper = objectMapper;
    }

    public ProjectHealthResponse analyzeProject(
            ObjectId projectId,
            ObjectId companyId) {

        // ==========================================
        // 1. Find project inside the company
        // ==========================================

        Project project = projectRepository
                .findByIdAndCompanyId(projectId, companyId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found")
                );

        // ==========================================
        // 2. Find project tasks
        // ==========================================

        List<Task> tasks =
                taskRepository.findByProjectIdAndCompanyId(
                        projectId,
                        companyId
                );

        // ==========================================
        // 3. Calculate objective metrics
        // ==========================================

        ProjectMetrics metrics =
                calculateMetrics(project, tasks);

        // ==========================================
        // 4. Build AI prompt
        // ==========================================

        String prompt =
                buildPrompt(project, metrics);

        // ==========================================
        // 5. Ask Gemini for interpretation
        // ==========================================

        String aiResponse =
                geminiService.generateContent(prompt);

        // ==========================================
        // 6. Parse Gemini response
        // ==========================================

        ProjectHealthResponse response =
                parseResponse(aiResponse);

        // ==========================================
        // 7. IMPORTANT:
        // Java remains the source of truth
        // for health score and risk level.
        // ==========================================

        response.setHealthScore(metrics.healthScore());
        response.setRiskLevel(metrics.riskLevel());

        return response;
    }

    // =====================================================
    // OBJECTIVE PROJECT METRICS
    // =====================================================

    private ProjectMetrics calculateMetrics(
            Project project,
            List<Task> tasks) {

        int totalTasks = tasks.size();

        int todoTasks = 0;
        int inProgressTasks = 0;
        int reviewTasks = 0;
        int completedTasks = 0;
        int overdueTasks = 0;

        LocalDate today = LocalDate.now();

        for (Task task : tasks) {

            if (task.getStatus() == TaskStatus.TODO) {
                todoTasks++;
            }

            if (task.getStatus() == TaskStatus.IN_PROGRESS) {
                inProgressTasks++;
            }

            if (task.getStatus() == TaskStatus.REVIEW) {
                reviewTasks++;
            }

            if (task.getStatus() == TaskStatus.DONE) {
                completedTasks++;
            }

            if (task.getDueDate() != null
                    && task.getDueDate().isBefore(today)
                    && task.getStatus() != TaskStatus.DONE) {

                overdueTasks++;
            }
        }

        double completionRate = totalTasks == 0
                ? 0
                : ((double) completedTasks / totalTasks) * 100;

        // ==========================================
        // Project timeline
        // ==========================================

        long daysRemaining = -1;

        if (project.getEndDate() != null) {
            daysRemaining =
                    ChronoUnit.DAYS.between(
                            today,
                            project.getEndDate()
                    );
        }

        // ==========================================
        // Calculate health score
        // ==========================================

        int healthScore = calculateHealthScore(
                project,
                totalTasks,
                completedTasks,
                overdueTasks,
                completionRate,
                daysRemaining
        );

        String riskLevel =
                calculateRiskLevel(healthScore);

        return new ProjectMetrics(
                totalTasks,
                todoTasks,
                inProgressTasks,
                reviewTasks,
                completedTasks,
                overdueTasks,
                completionRate,
                daysRemaining,
                healthScore,
                riskLevel
        );
    }

    // =====================================================
    // HEALTH SCORE
    // =====================================================

    private int calculateHealthScore(
            Project project,
            int totalTasks,
            int completedTasks,
            int overdueTasks,
            double completionRate,
            long daysRemaining) {

        // ------------------------------------------
        // No tasks
        // ------------------------------------------

        if (totalTasks == 0) {

            if (project.getStatus() == ProjectStatus.PLANNED) {
                return 60;
            }

            return 40;
        }

        // ------------------------------------------
        // Start with completion rate
        // ------------------------------------------

        double score = completionRate;

        // ------------------------------------------
        // Penalize overdue tasks
        // ------------------------------------------

        double overduePercentage =
                ((double) overdueTasks / totalTasks) * 100;

        score -= overduePercentage * 1.5;

        // ------------------------------------------
        // Deadline pressure
        // ------------------------------------------

        if (daysRemaining >= 0 && daysRemaining <= 7) {

            if (completionRate < 80) {
                score -= 15;
            }
        }

        if (daysRemaining > 7 && daysRemaining <= 30) {

            if (completionRate < 50) {
                score -= 10;
            }
        }

        // ------------------------------------------
        // Project status
        // ------------------------------------------

        if (project.getStatus() == ProjectStatus.ON_HOLD) {
            score -= 20;
        }

        if (project.getStatus() == ProjectStatus.CANCELLED) {
            score = 0;
        }

        if (project.getStatus() == ProjectStatus.COMPLETED) {
            score = 100;
        }

        // ------------------------------------------
        // Keep score between 0 and 100
        // ------------------------------------------

        return Math.max(
                0,
                Math.min(
                        100,
                        (int) Math.round(score)
                )
        );
    }

    // =====================================================
    // RISK LEVEL
    // =====================================================

    private String calculateRiskLevel(int healthScore) {

        if (healthScore >= 80) {
            return "LOW";
        }

        if (healthScore >= 60) {
            return "MEDIUM";
        }

        if (healthScore >= 40) {
            return "HIGH";
        }

        return "CRITICAL";
    }

    // =====================================================
    // AI PROMPT
    // =====================================================

    private String buildPrompt(
            Project project,
            ProjectMetrics metrics) {

        return """
                You are an AI project health analyst
                inside a project management SaaS.

                Analyze the project using ONLY the
                objective information provided below.

                IMPORTANT:

                Do NOT calculate or change the health score.

                The backend has already calculated:

                Health Score: %d

                Risk Level: %s

                Treat these values as authoritative.

                Your job is to explain the project condition,
                identify meaningful risks, and provide
                practical recommendations.

                PROJECT:

                Name:
                %s

                Description:
                %s

                Status:
                %s

                Start Date:
                %s

                End Date:
                %s

                OBJECTIVE METRICS:

                Total Tasks:
                %d

                TODO:
                %d

                IN PROGRESS:
                %d

                REVIEW:
                %d

                COMPLETED:
                %d

                OVERDUE:
                %d

                Completion Rate:
                %.2f%%

                Days Remaining:
                %d


                Return ONLY valid JSON.

                Use exactly this structure:

                {
                  "healthScore": %d,
                  "riskLevel": "%s",
                  "summary": "short business-oriented summary",
                  "risks": [
                    "risk 1",
                    "risk 2"
                  ],
                  "recommendations": [
                    "recommendation 1",
                    "recommendation 2"
                  ]
                }

                Rules:

                - Do not invent data.
                - Do not invent employees.
                - Do not invent tasks.
                - Do not invent deadlines.
                - Keep the summary concise.
                - Risks must be supported by the metrics.
                - Recommendations must be practical.
                - If there are no meaningful risks, return an empty risks array.
                """.formatted(
                metrics.healthScore(),
                metrics.riskLevel(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getStartDate(),
                project.getEndDate(),
                metrics.totalTasks(),
                metrics.todoTasks(),
                metrics.inProgressTasks(),
                metrics.reviewTasks(),
                metrics.completedTasks(),
                metrics.overdueTasks(),
                metrics.completionRate(),
                metrics.daysRemaining(),
                metrics.healthScore(),
                metrics.riskLevel()
        );
    }

    // =====================================================
    // PARSE GEMINI RESPONSE
    // =====================================================

    private ProjectHealthResponse parseResponse(
            String aiResponse) {

        try {

            String cleanedResponse =
                    aiResponse
                            .replace("```json", "")
                            .replace("```", "")
                            .trim();

            JsonNode json =
                    objectMapper.readTree(cleanedResponse);

            return ProjectHealthResponse.builder()

                    .healthScore(
                            json.path("healthScore").asInt()
                    )

                    .riskLevel(
                            json.path("riskLevel").asText()
                    )

                    .summary(
                            json.path("summary").asText()
                    )

                    .risks(
                            objectMapper.convertValue(
                                    json.path("risks"),
                                    List.class
                            )
                    )

                    .recommendations(
                            objectMapper.convertValue(
                                    json.path("recommendations"),
                                    List.class
                            )
                    )

                    .build();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse AI project health response",
                    e
            );
        }
    }

    // =====================================================
    // INTERNAL METRICS RECORD
    // =====================================================

    private record ProjectMetrics(
            int totalTasks,
            int todoTasks,
            int inProgressTasks,
            int reviewTasks,
            int completedTasks,
            int overdueTasks,
            double completionRate,
            long daysRemaining,
            int healthScore,
            String riskLevel
    ) {
    }
}