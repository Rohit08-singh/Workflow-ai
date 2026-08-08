package com.rohit.workflow_ai.user.entity;

import com.rohit.workflow_ai.common.entity.BaseEntity;
import com.rohit.workflow_ai.common.enums.Status;
import com.rohit.workflow_ai.common.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    private ObjectId id;

    private ObjectId companyId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String phone;

    @Builder.Default
    private UserRole role = UserRole.EMPLOYEE;

    @Builder.Default
    private Status status = Status.ACTIVE;

    private String profileImage;
}