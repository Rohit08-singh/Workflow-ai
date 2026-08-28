package com.rohit.workflow_ai.activity.service;

import com.rohit.workflow_ai.activity.dto.ActivityResponse;
import com.rohit.workflow_ai.activity.entity.Activity;
import com.rohit.workflow_ai.activity.repository.ActivityRepository;
import com.rohit.workflow_ai.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            UserRepository userRepository) {

        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    // ==========================
    // Create Activity
    // ==========================

    public void createActivity(
            ObjectId companyId,
            ObjectId performedBy,
            String type,
            String message,
            ObjectId projectId,
            ObjectId taskId,
            ObjectId clientId,
            ObjectId targetUserId) {

        Activity activity = Activity.builder()
                .companyId(companyId)
                .performedBy(performedBy)
                .type(type)
                .message(message)
                .projectId(projectId)
                .taskId(taskId)
                .clientId(clientId)
                .targetUserId(targetUserId)
                .build();

        activityRepository.save(activity);
    }

    // ==========================
    // Get Recent Activities
    // ==========================

    public List<ActivityResponse> getRecentActivities(
            ObjectId companyId) {

        return activityRepository
                .findTop10ByCompanyIdOrderByCreatedAtDesc(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==========================
    // Get All Activities
    // ==========================

    public List<ActivityResponse> getAllActivities(
            ObjectId companyId) {

        return activityRepository
                .findByCompanyIdOrderByCreatedAtDesc(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==========================
    // Get Project Activities
    // ==========================

    public List<ActivityResponse> getProjectActivities(
            ObjectId projectId,
            ObjectId companyId) {

        return activityRepository
                .findByCompanyIdAndProjectIdOrderByCreatedAtDesc(
                        companyId,
                        projectId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==========================
    // Get Task Activities
    // ==========================

    public List<ActivityResponse> getTaskActivities(
            ObjectId taskId,
            ObjectId companyId) {

        return activityRepository
                .findByCompanyIdAndTaskIdOrderByCreatedAtDesc(
                        companyId,
                        taskId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==========================
    // Mapper
    // ==========================

    private ActivityResponse toResponse(Activity activity) {

        String performedBy = null;

        if (activity.getPerformedBy() != null) {

            performedBy = userRepository
                    .findById(activity.getPerformedBy())
                    .map(user ->
                            user.getFirstName() + " " +
                                    user.getLastName()
                    )
                    .orElse("Unknown User");
        }

        return ActivityResponse.builder()
                .id(activity.getId().toHexString())
                .type(activity.getType())
                .message(activity.getMessage())
                .performedBy(performedBy)
                .projectId(toString(activity.getProjectId()))
                .taskId(toString(activity.getTaskId()))
                .clientId(toString(activity.getClientId()))
                .targetUserId(toString(activity.getTargetUserId()))
                .createdAt(activity.getCreatedAt())
                .build();
    }

    // ==========================
    // ObjectId → String
    // ==========================

    private String toString(ObjectId id) {

        return id != null
                ? id.toHexString()
                : null;
    }
}