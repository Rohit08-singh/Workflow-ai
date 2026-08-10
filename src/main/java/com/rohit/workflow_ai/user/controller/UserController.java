package com.rohit.workflow_ai.user.controller;

import com.rohit.workflow_ai.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.rohit.workflow_ai.auth.dto.UserResponse;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(
            Authentication authentication) {

        UserResponse response =
                userService.getProfile(authentication.getName());

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Profile fetched successfully"
                )
        );
    }
}