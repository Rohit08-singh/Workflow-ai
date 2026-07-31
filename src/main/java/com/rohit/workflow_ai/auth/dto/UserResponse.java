package com.rohit.workflow_ai.auth.dto;

import com.rohit.workflow_ai.common.enums.Status;
import com.rohit.workflow_ai.common.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;

    private Status status;

}