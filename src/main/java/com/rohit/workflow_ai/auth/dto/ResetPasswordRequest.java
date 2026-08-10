package com.rohit.workflow_ai.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String resetToken;

    private String newPassword;
}