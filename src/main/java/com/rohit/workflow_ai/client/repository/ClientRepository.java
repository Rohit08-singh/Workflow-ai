package com.rohit.workflow_ai.client.repository;

import com.rohit.workflow_ai.client.entity.Client;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
public interface ClientRepository
        extends MongoRepository<Client, ObjectId> {

    boolean existsByEmailAndCompanyId(
            String email,
            ObjectId companyId
    );

    List<Client> findByCompanyId(
            ObjectId companyId
    );

    Optional<Client> findByIdAndCompanyId(
            ObjectId id,
            ObjectId companyId
    );

    long countByCompanyId(
            ObjectId companyId
    );
}