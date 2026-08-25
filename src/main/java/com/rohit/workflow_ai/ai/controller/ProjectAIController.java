package com.rohit.workflow_ai.ai.controller;

import com.rohit.workflow_ai.ai.dto.ProjectAIRequest;
import com.rohit.workflow_ai.ai.dto.ProjectAIResponse;
import com.rohit.workflow_ai.ai.service.ProjectAIService;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai/projects")
public class ProjectAIController {

    private final ProjectAIService projectAIService;

    public ProjectAIController(ProjectAIService projectAIService) {
        this.projectAIService = projectAIService;
    }

    @PostMapping("/assistant")
    public ResponseEntity<ApiResponse<ProjectAIResponse>> askProjectAssistant(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody ProjectAIRequest request) {

        System.out.println("====================================");
        System.out.println("PROJECT AI CONTROLLER DEBUG");

        System.out.println("User ID: "
                + currentUser.getUser().getId());

        System.out.println("Email: "
                + currentUser.getUser().getEmail());

        System.out.println("Role: "
                + currentUser.getUser().getRole());

        System.out.println("Company ID from User: "
                + currentUser.getUser().getCompanyId());

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        System.out.println("Company ID passed to Service: "
                + companyId);

        System.out.println("====================================");

        ProjectAIResponse response =
                projectAIService.askProjectAssistant(
                        companyId,
                        request.getPrompt()
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Project AI response generated successfully"
                )
        );
    }
}