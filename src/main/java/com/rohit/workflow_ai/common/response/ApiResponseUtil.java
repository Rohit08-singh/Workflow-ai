package com.rohit.workflow_ai.common.response;

import org.springframework.http.HttpStatus;

public class ApiResponseUtil {
            
    private ApiResponseUtil() {
    }

    public static <T> ApiResponse<T> success(T data, String message) {

        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data, String message) {

        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {

        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message) {

        return ApiResponse.<T>builder()
                .success(false)
                .status(status.value())
                .message(message)
                .build();
    }
}