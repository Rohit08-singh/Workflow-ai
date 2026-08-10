package com.rohit.workflow_ai.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordResponse {

    private String resetToken;

    private String message;
}