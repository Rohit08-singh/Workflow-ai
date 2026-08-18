package com.rohit.workflow_ai.dashboard.service;

import com.rohit.workflow_ai.client.repository.ClientRepository;
import com.rohit.workflow_ai.common.enums.ProjectStatus;
import com.rohit.workflow_ai.common.enums.Status;
import com.rohit.workflow_ai.common.enums.TaskStatus;
import com.rohit.workflow_ai.dashboard.dto.DashboardResponse;
import com.rohit.workflow_ai.project.repository.ProjectRepository;
import com.rohit.workflow_ai.task.repository.TaskRepository;
import com.rohit.workflow_ai.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DashboardService(UserRepository userRepository,
                            ClientRepository clientRepository,
                            ProjectRepository projectRepository,
                            TaskRepository taskRepository) {

        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public DashboardResponse getDashboard(ObjectId companyId) {

        return DashboardResponse.builder()

                // Employee
                .totalEmployees(
                        userRepository.countByCompanyId(companyId)
                )
                .activeEmployees(
                        userRepository.countByCompanyIdAndStatus(
                                companyId,
                                Status.ACTIVE
                        )
                )

                // Client
                .totalClients(
                        clientRepository.countByCompanyId(companyId)
                )

                // Project
                .totalProjects(
                        projectRepository.countByCompanyId(companyId)
                )
                .activeProjects(
                        projectRepository.countByCompanyIdAndStatus(
                                companyId,
                                ProjectStatus.IN_PROGRESS
                        )
                )
                .completedProjects(
                        projectRepository.countByCompanyIdAndStatus(
                                companyId,
                                ProjectStatus.COMPLETED
                        )
                )

                // Task
                .totalTasks(
                        taskRepository.countByCompanyId(companyId)
                )
                .todoTasks(
                        taskRepository.countByCompanyIdAndStatus(
                                companyId,
                                TaskStatus.TODO
                        )
                )
                .inProgressTasks(
                        taskRepository.countByCompanyIdAndStatus(
                                companyId,
                                TaskStatus.IN_PROGRESS
                        )
                )
                .completedTasks(
                        taskRepository.countByCompanyIdAndStatus(
                                companyId,
                                TaskStatus.DONE
                        )
                )

                .build();
    }
}

