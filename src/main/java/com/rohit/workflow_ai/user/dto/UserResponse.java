package com.rohit.workflow_ai.user.dto;

import com.rohit.workflow_ai.common.enums.Status;
import com.rohit.workflow_ai.common.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private String id;

    private String companyId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private UserRole role;

    private Status status;

    private String profileImage;
}