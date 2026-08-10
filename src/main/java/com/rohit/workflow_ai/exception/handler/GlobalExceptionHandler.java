package com.rohit.workflow_ai.exception.handler;

import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import com.rohit.workflow_ai.exception.custom.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {

        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(
                        ApiResponseUtil.error(
                                ex.getErrorCode().getStatus(),
                                ex.getErrorCode().getMessage()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {

        ex.printStackTrace();   // <-- Add this

        return ResponseEntity
                .internalServerError()
                .body(
                        ApiResponseUtil.error(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                ex.getMessage()
                        )
                );
    }
}