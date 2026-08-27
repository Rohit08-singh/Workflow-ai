package com.rohit.workflow_ai.user.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.security.service.CustomUserDetails;
import com.rohit.workflow_ai.user.dto.*;
import com.rohit.workflow_ai.user.service.UserService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ==========================
    // Logged In User Profile
    // ==========================

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

    // ==========================
    // Update Own Profile
    // ==========================

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        userService.updateProfile(
                                authentication,
                                request
                        ),
                        "Profile updated successfully"
                )
        );
    }

    // ==========================
    // Create Employee
    // ==========================
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CreateUserRequest request) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        UserResponse response =
                userService.createUser(
                        request,
                        companyId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseUtil.created(
                                response,
                                "Employee created successfully"
                        )
                );
    }
    // ==========================
    // Get All Employees
    // ==========================
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        userService.getAllUsers(companyId),
                        "Employees fetched successfully"
                )
        );
    }
    // ==========================
    // Get Employee By ID
    // ==========================
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String id) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        UserResponse response =
                userService.getUserById(
                        id,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Employee fetched successfully"
                )
        );
    }
    // ==========================
    // Update Employee
    // ==========================
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        UserResponse response =
                userService.updateUser(
                        id,
                        request,
                        companyId
                );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Employee updated successfully"
                )
        );
    }

    // ==========================
    // Delete Employee
    // ==========================
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String id) {

        ObjectId companyId =
                currentUser.getUser().getCompanyId();

        userService.deleteUser(
                id,
                companyId
        );

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        null,
                        "Employee deleted successfully"
                )
        );
    }
}