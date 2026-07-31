package com.rohit.workflow_ai.company.entity;

import com.rohit.workflow_ai.common.entity.BaseEntity;
import com.rohit.workflow_ai.common.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "companies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity {

    @Id
    private ObjectId id;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @Email(message = "Invalid email")
    private String email;

    private String phone;

    private String website;

    private String address;

    private String industry;

    @Builder.Default
    private Status status = Status.ACTIVE;
}