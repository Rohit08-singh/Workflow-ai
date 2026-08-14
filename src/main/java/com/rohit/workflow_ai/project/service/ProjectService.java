package com.rohit.workflow_ai.project.service;

import com.rohit.workflow_ai.client.entity.Client;
import com.rohit.workflow_ai.client.repository.ClientRepository;
import com.rohit.workflow_ai.common.enums.Status;
import com.rohit.workflow_ai.exception.ErrorCode;
import com.rohit.workflow_ai.exception.custom.AppException;
import com.rohit.workflow_ai.project.dto.CreateProjectRequest;
import com.rohit.workflow_ai.project.dto.ProjectResponse;
import com.rohit.workflow_ai.project.dto.UpdateProjectRequest;
import com.rohit.workflow_ai.project.entity.Project;
import com.rohit.workflow_ai.project.mapper.ProjectMapper;
import com.rohit.workflow_ai.project.repository.ProjectRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;

    public ProjectService(ProjectRepository projectRepository,
                          ClientRepository clientRepository) {

        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
    }

    public ProjectResponse createProject(CreateProjectRequest request,
                                         ObjectId companyId) {

        ObjectId clientId = new ObjectId(request.getClientId());

        Client client = clientRepository
                .findByIdAndCompanyId(clientId, companyId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.CLIENT_NOT_FOUND));

        if (projectRepository.existsByNameAndCompanyId(
                request.getName(),
                companyId)) {

            throw new AppException(ErrorCode.PROJECT_ALREADY_EXISTS);
        }

        Project project = ProjectMapper.toEntity(
                request,
                companyId,
                clientId
        );

        Project savedProject = projectRepository.save(project);

        return ProjectMapper.toResponse(savedProject);
    }

    public List<ProjectResponse> getAllProjects(ObjectId companyId) {

        List<Project> projects = projectRepository.findByCompanyId(companyId);

        return projects.stream()
                .map(ProjectMapper::toResponse)
                .toList();
    }

    public ProjectResponse getProjectById(
            String projectId,
            ObjectId companyId
    ) {

        Project project = projectRepository
                .findByIdAndCompanyId(
                        new ObjectId(projectId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(ErrorCode.PROJECT_NOT_FOUND));

        return ProjectMapper.toResponse(project);
    }

    public ProjectResponse updateProject(
            String projectId,
            UpdateProjectRequest request,
            ObjectId companyId) {

        Project project = projectRepository
                .findByIdAndCompanyId(
                        new ObjectId(projectId),
                        companyId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.PROJECT_NOT_FOUND));

        ObjectId clientId = new ObjectId(request.getClientId());

        clientRepository.findByIdAndCompanyId(clientId, companyId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.CLIENT_NOT_FOUND));

        project.setClientId(clientId);
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        Project updatedProject = projectRepository.save(project);

        return ProjectMapper.toResponse(updatedProject);
    }

    public void deleteProject(
            String projectId,
            ObjectId companyId
    ) {

        Project project = projectRepository
                .findByIdAndCompanyId(
                        new ObjectId(projectId),
                        companyId
                )
                .orElseThrow(() ->
                        new AppException(ErrorCode.PROJECT_NOT_FOUND));

        project.setRecordStatus(Status.DELETED);

        projectRepository.save(project);
    }
}