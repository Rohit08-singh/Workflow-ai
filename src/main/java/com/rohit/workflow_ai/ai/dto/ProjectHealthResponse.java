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
public class ProjectHealthResponse {

    private int healthScore;

    private String riskLevel;

    private String summary;

    private List<String> risks;

    private List<String> recommendations;
}