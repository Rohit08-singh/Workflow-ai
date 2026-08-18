package com.rohit.workflow_ai.user.controller;

import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.user.dto.*;
import com.rohit.workflow_ai.user.service.UserService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
<<<<<<< Updated upstream

<<<<<<< HEAD
    // ==========================
    // Logged In User Profile       
    // ==========================
=======
=======
>>>>>>> Stashed changes
>>>>>>> 4b7cca23b8715849d7cd5ee14bb476d2d91fef54
    @GetMapping("/me")
    // ==========================
    // Logged In User Profile
    // ==========================

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
    // Update Profile
    // ==========================
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
<<<<<<< HEAD
=======
<<<<<<< Updated upstream
}
=======
>>>>>>> 4b7cca23b8715849d7cd5ee14bb476d2d91fef54

    // ==========================
    // Create Employee
    // ==========================
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        ObjectId companyId =
                new ObjectId("68a0d12f0d3a4a8dbe2b1234");

        UserResponse response =
                userService.createUser(request, companyId);

        return ResponseEntity.status(HttpStatus.CREATED)
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
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        ObjectId companyId =
                new ObjectId("68a0d12f0d3a4a8dbe2b1234");

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        userService.getAllUsers(companyId),
                        "Employees fetched successfully"
                )
        );
    }

    // ==========================
// Get Employee By Id
// ==========================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable String id) {

        ObjectId companyId =
                new ObjectId("68a0d12f0d3a4a8dbe2b1234");

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
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request) {

        ObjectId companyId =
                new ObjectId("68a0d12f0d3a4a8dbe2b1234");

        UserResponse response =
                userService.updateUser(
                        id,
                        request,
                        companyId);

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        response,
                        "Employee updated successfully"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable String id) {

        ObjectId companyId =
                new ObjectId("68a0d12f0d3a4a8dbe2b1234");

        userService.deleteUser(id, companyId);

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        null,
                        "Employee deleted successfully"
                )
        );
    }
<<<<<<< HEAD
}
=======
}


>>>>>>> Stashed changes
>>>>>>> 4b7cca23b8715849d7cd5ee14bb476d2d91fef54
