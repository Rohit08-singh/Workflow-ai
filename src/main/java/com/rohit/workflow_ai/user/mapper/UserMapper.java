package com.rohit.workflow_ai.user.mapper;




import com.rohit.workflow_ai.common.enums.UserRole;
import com.rohit.workflow_ai.user.dto.CreateUserRequest;
import com.rohit.workflow_ai.user.dto.UserProfileResponse;
import com.rohit.workflow_ai.user.dto.UserResponse;
import com.rohit.workflow_ai.user.entity.User;
import org.bson.types.ObjectId;

public class UserMapper {

    private UserMapper() {
    }

    // ==========================
    // Create Employee
    // ==========================
    public static User toEntity(CreateUserRequest request,
                                ObjectId companyId) {

        return User.builder()
                .companyId(companyId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phone(request.getPhone())
                .role(
                        request.getRole() == null
                                ? UserRole.EMPLOYEE
                                : request.getRole()
                )
                .build();
    }

    // ==========================
    // Employee Response
    // ==========================
    public static UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId().toHexString())
                .companyId(user.getCompanyId().toHexString())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .profileImage(user.getProfileImage())
                .build();
    }

    // ==========================
    // Logged-in Profile Response
    // ==========================
    public static UserProfileResponse toUserProfile(User user) {

        return UserProfileResponse.builder()
                .id(user.getId().toHexString())
                .companyId(user.getCompanyId() != null
                        ? user.getCompanyId().toHexString()
                        : null)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .profileImage(user.getProfileImage())
                .build();
    }
}