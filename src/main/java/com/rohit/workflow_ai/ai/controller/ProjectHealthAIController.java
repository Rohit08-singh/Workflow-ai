package com.rohit.workflow_ai.ai.controller;

import com.rohit.workflow_ai.ai.dto.ProjectHealthResponse;
import com.rohit.workflow_ai.ai.service.ProjectHealthAIService;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai/projects")
public class ProjectHealthAIController {

    private final ProjectHealthAIService projectHealthAIService;

    public ProjectHealthAIController(
            ProjectHealthAIService projectHealthAIService) {

        this.projectHealthAIService = projectHealthAIService;
    }

    @PostMapping("/{projectId}/health")
    public ResponseEntity<ApiResponse<ProjectHealthResponse>> analyzeProjectHealth(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String projectId) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        ObjectId objectId;

        try {
            objectId = new ObjectId(projectId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid project ID");
        }

        ProjectHealthResponse response =
                projectHealthAIService.analyzeProject(
                        objectId,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Project health analysis generated successfully"
                )
        );
    }
}