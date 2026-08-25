package com.rohit.workflow_ai.task.entity;

import com.rohit.workflow_ai.common.entity.BaseEntity;
import com.rohit.workflow_ai.common.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity {

    @Id
    private ObjectId id;
    @Builder.Default
    private TaskStatus status = TaskStatus.TODO;
    private ObjectId companyId;

    private ObjectId projectId;

    private String title;

    private String description;

    private ObjectId assignedUserId;

    private LocalDate dueDate;
}