package com.rohit.workflow_ai.task.repository;

import com.rohit.workflow_ai.common.enums.TaskStatus;
import com.rohit.workflow_ai.task.entity.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository
        extends MongoRepository<Task, ObjectId> {

    // ==========================
    // Task Management
    // ==========================

    Optional<Task> findByIdAndCompanyId(
            ObjectId id,
            ObjectId companyId
    );

    List<Task> findByProjectIdAndCompanyId(
            ObjectId projectId,
            ObjectId companyId
    );

    // ==========================
    // Company Tasks
    // ==========================

    List<Task> findByCompanyId(
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
            TaskStatus status
    );
}