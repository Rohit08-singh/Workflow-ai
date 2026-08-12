package com.rohit.workflow_ai.client.entity;

import com.rohit.workflow_ai.common.entity.BaseEntity;
import com.rohit.workflow_ai.common.enums.Status;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "clients")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseEntity {

    @Id
    private ObjectId id;

    private ObjectId companyId;

    private String companyName;

    private String contactPerson;

    private String email;

    private String phone;

    private String address;

    @Builder.Default
    private Status status = Status.ACTIVE;
}