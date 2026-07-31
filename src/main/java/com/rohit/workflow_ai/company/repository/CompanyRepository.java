package com.rohit.workflow_ai.company.repository;

import com.rohit.workflow_ai.company.entity.Company;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, ObjectId> {

    boolean existsByCompanyName(String companyName);

}