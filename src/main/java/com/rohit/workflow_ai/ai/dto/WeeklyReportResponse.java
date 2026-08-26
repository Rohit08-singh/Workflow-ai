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
public class WeeklyReportResponse {

    private String projectName;

    private String period;

    private String progressSummary;

    private List<String> completedWork;

    private List<String> pendingWork;

    private List<String> overdueWork;

    private List<String> risks;

    private List<String> nextWeekFocus;
}