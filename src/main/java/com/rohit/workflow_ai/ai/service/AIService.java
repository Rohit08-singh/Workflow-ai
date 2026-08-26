package com.rohit.workflow_ai.ai.service;

import com.rohit.workflow_ai.ai.dto.AIRequest;
import com.rohit.workflow_ai.ai.dto.AIResponse;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final GeminiService geminiService;

    public AIService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public AIResponse generate(AIRequest request) {

        String response =
                geminiService.generateContent(
                        request.getPrompt()
                );

        return AIResponse.builder()
                .response(response)
                .build();
    }
}