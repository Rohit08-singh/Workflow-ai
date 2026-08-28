package com.rohit.workflow_ai.dashboard.controller;

import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.dashboard.dto.DashboardResponse;
import com.rohit.workflow_ai.dashboard.service.DashboardService;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        dashboardService.getDashboard(companyId),
                        "Dashboard fetched successfully"
                )
        );
    }
}