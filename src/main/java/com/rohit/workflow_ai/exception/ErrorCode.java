package com.rohit.workflow_ai.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // =========================
    // User Errors
    // =========================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid email or password"),
    USER_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "User is already verified"),

    // =========================
    // Company Errors
    // =========================
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "Company not found"),
    COMPANY_ALREADY_EXISTS(HttpStatus.CONFLICT, "Company already exists"),

    // =========================
    // Client Errors
    // =========================
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Client not found"),

    // =========================
    // Project Errors
    // =========================
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not found"),

    // =========================
    // Task Errors
    // =========================
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "Task not found"),

    // Authentication & Security

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token has expired"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token has expired"),


    // OTP & Verification

    INVALID_OTP(HttpStatus.BAD_REQUEST, "Invalid OTP"),
    OTP_EXPIRED(HttpStatus.BAD_REQUEST, "OTP has expired"),
    INVALID_REFRESH_TOKEN(
            HttpStatus.UNAUTHORIZED,
            "Invalid or Expired Refresh Token"
    ),

    // General Errors

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}