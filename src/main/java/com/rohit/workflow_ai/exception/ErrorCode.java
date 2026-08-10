package com.rohit.workflow_ai.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // =========================
    // User Errors
    // =========================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists"),
    USER_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "User is already verified"),

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Current password is incorrect"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "Passwords do not match"),

    // =========================
    // Company Errors
    // =========================
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "Company not found"),
    COMPANY_ALREADY_EXISTS(HttpStatus.CONFLICT, "Company already exists"),

    // =========================
    // Client Errors
    // =========================
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Client not found"),
    CLIENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "Client already exists"),

    // =========================
    // Project Errors
    // =========================
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not found"),
    PROJECT_ALREADY_EXISTS(HttpStatus.CONFLICT, "Project already exists"),

    // =========================
    // Task Errors
    // =========================
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "Task not found"),
    TASK_ALREADY_EXISTS(HttpStatus.CONFLICT, "Task already exists"),

    // =========================
    // Authentication & Security
    // =========================
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid email or password"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token has expired"),

    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token has expired"),

    INVALID_RESET_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid or expired reset token"),

    // =========================
    // OTP & Verification
    // =========================
    INVALID_OTP(HttpStatus.BAD_REQUEST, "Invalid OTP"),
    OTP_EXPIRED(HttpStatus.BAD_REQUEST, "OTP has expired"),

    // =========================
    // Validation Errors
    // =========================
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation failed"),

    // =========================
    // General Errors
    // =========================
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