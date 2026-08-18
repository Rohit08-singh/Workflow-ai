package com.rohit.workflow_ai.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardResponse {

    // Employee
    private long totalEmployees;
    private long activeEmployees;

    // Client
    private long totalClients;

    // Project
    private long totalProjects;
    private long activeProjects;
    private long completedProjects;

    // Task
    private long totalTasks;
    private long todoTasks;
    private long inProgressTasks;
    private long completedTasks;

}