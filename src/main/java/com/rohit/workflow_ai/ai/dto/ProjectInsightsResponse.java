package com.rohit.workflow_ai.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInsightsResponse {

    private String summary;

    private List<String> keyInsights;

    private List<String> priorityActions;

    private String nextStep;
}