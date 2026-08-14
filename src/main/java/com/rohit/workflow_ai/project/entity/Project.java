package com.rohit.workflow_ai.project.entity;

import com.rohit.workflow_ai.common.entity.BaseEntity;
import com.rohit.workflow_ai.common.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "projects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

    @Id
    private ObjectId id;

    private ObjectId companyId;

    private ObjectId clientId;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private ProjectStatus status = ProjectStatus.PLANNED;
}