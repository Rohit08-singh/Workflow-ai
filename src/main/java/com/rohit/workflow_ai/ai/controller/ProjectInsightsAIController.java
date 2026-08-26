package com.rohit.workflow_ai.ai.controller;

import com.rohit.workflow_ai.ai.dto.ProjectInsightsRequest;
import com.rohit.workflow_ai.ai.dto.ProjectInsightsResponse;
import com.rohit.workflow_ai.ai.service.ProjectInsightsAIService;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai/project-insights")
public class ProjectInsightsAIController {

    private final ProjectInsightsAIService projectInsightsAIService;

    public ProjectInsightsAIController(
            ProjectInsightsAIService projectInsightsAIService) {

        this.projectInsightsAIService =
                projectInsightsAIService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectInsightsResponse>>
    generateInsights(

            @AuthenticationPrincipal
            CustomUserDetails currentUser,

            @Valid
            @RequestBody
            ProjectInsightsRequest request) {

        ObjectId companyId =
                currentUser
                        .getUser()
                        .getCompanyId();

        ProjectInsightsResponse response =
                projectInsightsAIService.generateInsights(
                        companyId,
                        request.getProjectId()
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Project insights generated successfully"
                )
        );
    }
}