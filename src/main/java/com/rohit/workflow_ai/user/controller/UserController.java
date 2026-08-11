package com.rohit.workflow_ai.user.controller;

import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.user.dto.UpdateProfileRequest;
import com.rohit.workflow_ai.user.dto.UserProfileResponse;
import com.rohit.workflow_ai.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(
            Authentication authentication) {

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        userService.getMyProfile(authentication),
                        "Profile fetched successfully"
                )
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        userService.updateProfile(authentication, request),
                        "Profile updated successfully"
                )
        );
    }
}