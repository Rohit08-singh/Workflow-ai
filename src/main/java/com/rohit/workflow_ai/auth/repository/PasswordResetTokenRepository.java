package com.rohit.workflow_ai.auth.repository;

import com.rohit.workflow_ai.auth.entity.PasswordResetToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository
        extends MongoRepository<PasswordResetToken, ObjectId> {

    Optional<PasswordResetToken> findByTokenHashAndUsedFalse(
            String tokenHash
    );

    void deleteByUserId(ObjectId userId);
}