package com.rohit.workflow_ai.ai.controller;

import com.rohit.workflow_ai.ai.dto.AIRequest;
import com.rohit.workflow_ai.ai.service.AIService;
import com.rohit.workflow_ai.common.response.ApiResponse;
import com.rohit.workflow_ai.common.response.ApiResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    // ==========================
    // Generic AI
    // ==========================
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<?>> generateResponse(
            @Valid @RequestBody AIRequest request) {

        return ResponseEntity.ok(
                ApiResponseUtil.success(
                        aiService.generate(request),
                        "AI response generated successfully"
                )
        );
    }
}