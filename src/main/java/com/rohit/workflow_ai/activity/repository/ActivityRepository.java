package com.rohit.workflow_ai.activity.repository;

import com.rohit.workflow_ai.activity.entity.Activity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityRepository
        extends MongoRepository<Activity, ObjectId> {

    List<Activity> findByCompanyIdOrderByCreatedAtDesc(
            ObjectId companyId
    );

    List<Activity> findTop10ByCompanyIdOrderByCreatedAtDesc(
            ObjectId companyId
    );

    List<Activity> findByCompanyIdAndProjectIdOrderByCreatedAtDesc(
            ObjectId companyId,
            ObjectId projectId
    );

    List<Activity> findByCompanyIdAndTaskIdOrderByCreatedAtDesc(
            ObjectId companyId,
            ObjectId taskId
    );
}