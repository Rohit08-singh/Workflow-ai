package com.rohit.workflow_ai.ai.controller;

import com.rohit.workflow_ai.ai.dto.WeeklyReportRequest;
import com.rohit.workflow_ai.ai.dto.WeeklyReportResponse;
import com.rohit.workflow_ai.ai.service.WeeklyReportAIService;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai/weekly-report")
public class WeeklyReportAIController {

    private final WeeklyReportAIService weeklyReportAIService;

    public WeeklyReportAIController(
            WeeklyReportAIService weeklyReportAIService) {

        this.weeklyReportAIService =
                weeklyReportAIService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WeeklyReportResponse>>
    generateWeeklyReport(

            @AuthenticationPrincipal
            CustomUserDetails currentUser,

            @Valid
            @RequestBody
            WeeklyReportRequest request) {

        ObjectId companyId =
                currentUser
                        .getUser()
                        .getCompanyId();

        WeeklyReportResponse response =
                weeklyReportAIService.generateWeeklyReport(
                        companyId,
                        request.getProjectId()
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Weekly project report generated successfully"
                )
        );
    }
}