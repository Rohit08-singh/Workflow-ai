package com.rohit.workflow_ai.activity.entity;

import com.rohit.workflow_ai.common.entity.BaseEntity;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "activities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activity extends BaseEntity {

    @Id
    private ObjectId id;

    private ObjectId companyId;

    private ObjectId performedBy;

    private String type;

    private String message;

    private ObjectId projectId;

    private ObjectId taskId;

    private ObjectId clientId;

    private ObjectId targetUserId;
}