package com.rohit.workflow_ai.activity.controller;

import com.rohit.workflow_ai.activity.dto.ActivityResponse;
import com.rohit.workflow_ai.activity.service.ActivityService;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // ==========================
    // Recent Activities
    // ==========================

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<ActivityResponse>>>
    getRecentActivities(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        activityService.getRecentActivities(companyId),
                        "Recent activities fetched successfully"
                )
        );
    }

    // ==========================
    // All Activities
    // ==========================

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityResponse>>>
    getAllActivities(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        activityService.getAllActivities(companyId),
                        "Activities fetched successfully"
                )
        );
    }

    // ==========================
    // Project Activities
    // ==========================

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<ActivityResponse>>>
    getProjectActivities(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String projectId) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        activityService.getProjectActivities(
                                new ObjectId(projectId),
                                companyId
                        ),
                        "Project activities fetched successfully"
                )
        );
    }

    // ==========================
    // Task Activities
    // ==========================

    @GetMapping("/task/{taskId}")
    public ResponseEntity<ApiResponse<List<ActivityResponse>>>
    getTaskActivities(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String taskId) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        activityService.getTaskActivities(
                                new ObjectId(taskId),
                                companyId
                        ),
                        "Task activities fetched successfully"
                )
        );
    }
}