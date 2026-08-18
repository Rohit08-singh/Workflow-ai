package com.rohit.workflow_ai.user.repository;

import com.rohit.workflow_ai.common.enums.Status;
import com.rohit.workflow_ai.user.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
<<<<<<< HEAD
=======
<<<<<<< Updated upstream
=======
>>>>>>> 4b7cca23b8715849d7cd5ee14bb476d2d91fef54

    boolean existsByEmailAndCompanyId(String email, ObjectId companyId);

    List<User> findByCompanyId(ObjectId companyId);

    Optional<User> findByIdAndCompanyId(
            ObjectId id,
            ObjectId companyId
    );
<<<<<<< HEAD
=======

    // ==========================
    // Dashboard
    // ==========================
    long countByCompanyId(ObjectId companyId);

    long countByCompanyIdAndStatus(
            ObjectId companyId,
            Status status
    );
>>>>>>> Stashed changes
>>>>>>> 4b7cca23b8715849d7cd5ee14bb476d2d91fef54
}