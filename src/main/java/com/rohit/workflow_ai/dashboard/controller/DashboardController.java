package com.rohit.workflow_ai.dashboard.controller;

import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.dashboard.dto.DashboardResponse;
import com.rohit.workflow_ai.dashboard.service.DashboardService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {

        // Temporary hardcoded companyId.
        // Later this will come from JWT.
        ObjectId companyId =
                new ObjectId("68a0d12f0d3a4a8dbe2b1234");

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        dashboardService.getDashboard(companyId),
                        "Dashboard fetched successfully"
                )
        );
    }
}