package com.rohit.workflow_ai.project.controller;

import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.project.dto.CreateProjectRequest;
import com.rohit.workflow_ai.project.dto.ProjectResponse;
import com.rohit.workflow_ai.project.dto.UpdateProjectRequest;
import com.rohit.workflow_ai.project.service.ProjectService;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // ==========================
    // Create Project
    // ==========================

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CreateProjectRequest request) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        ProjectResponse response =
                projectService.createProject(
                        request,
                        companyId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseUtil.created(
                                response,
                                "Project created successfully"
                        )
                );
    }

    // ==========================
    // Get All Projects
    // ==========================

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        List<ProjectResponse> response =
                projectService.getAllProjects(companyId);

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Projects fetched successfully"
                )
        );
    }

    // ==========================
    // Get Project By ID
    // ==========================

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN', 'EMPLOYEE')")
    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String projectId) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        ProjectResponse response =
                projectService.getProjectById(
                        projectId,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Project fetched successfully"
                )
        );
    }

    // ==========================
    // Update Project
    // ==========================

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String projectId,
            @Valid @RequestBody UpdateProjectRequest request) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        ProjectResponse response =
                projectService.updateProject(
                        projectId,
                        request,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Project updated successfully"
                )
        );
    }

    // ==========================
    // Delete Project
    // ==========================

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String projectId) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        projectService.deleteProject(
                projectId,
                companyId
        );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        null,
                        "Project deleted successfully"
                )
        );
    }
}