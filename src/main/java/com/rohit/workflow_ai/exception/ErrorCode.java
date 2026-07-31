package com.rohit.workflow_ai.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),

    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "Company not found"),

    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Client not found"),

    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not found"),

    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "Task not found"),

    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists"),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid email or password"),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),

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