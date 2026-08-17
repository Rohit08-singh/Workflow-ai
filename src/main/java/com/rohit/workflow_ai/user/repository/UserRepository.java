package com.rohit.workflow_ai.user.repository;

import com.rohit.workflow_ai.user.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndCompanyId(String email, ObjectId companyId);

    List<User> findByCompanyId(ObjectId companyId);

    Optional<User> findByIdAndCompanyId(
            ObjectId id,
            ObjectId companyId
    );
}