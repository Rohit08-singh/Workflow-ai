package com.rohit.workflow_ai.auth.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "password_reset_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    @Id
    private ObjectId id;

    private ObjectId userId;

    private String tokenHash;

    private LocalDateTime expiresAt;

    @Builder.Default
    private boolean used = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}