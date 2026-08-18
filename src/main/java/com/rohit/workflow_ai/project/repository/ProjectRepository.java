package com.rohit.workflow_ai.project.repository;

import com.rohit.workflow_ai.common.enums.ProjectStatus;
import com.rohit.workflow_ai.project.entity.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends MongoRepository<Project, ObjectId> {

    boolean existsByNameAndCompanyId(
            String name,
            ObjectId companyId
    );

    List<Project> findByCompanyId(
            ObjectId companyId
    );

    List<Project> findByCompanyIdAndStatus(
            ObjectId companyId,
            ProjectStatus status
    );

    Optional<Project> findByIdAndCompanyId(
            ObjectId id,
            ObjectId companyId
    );

    // ==========================
    // Dashboard
    // ==========================
    long countByCompanyId(
            ObjectId companyId
    );

    long countByCompanyIdAndStatus(
            ObjectId companyId,
            ProjectStatus status
    );
}