package com.rohit.workflow_ai.auth.controller;

import com.rohit.workflow_ai.auth.dto.*;
import com.rohit.workflow_ai.auth.service.AuthService;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerCompany(
            @Valid @RequestBody RegisterCompanyRequest request) {

        UserResponse response = authService.registerCompany(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseUtil.created(
                        response,
                        "Company registered successfully"
                ));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Login Successful"
                )
        );
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        authService.refreshToken(request),
                        "Token refreshed successfully"
                )
        );
    }

}