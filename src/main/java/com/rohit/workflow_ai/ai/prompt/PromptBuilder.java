package com.rohit.workflow_ai.ai.prompt;

public class PromptBuilder {

    private PromptBuilder() {}

    public static String build(String prompt) {

        return """
                You are Workflow AI Assistant.

                %s
                """.formatted(prompt);

    }

}