package com.projectmanagement.repository;

import com.projectmanagement.id.NextProjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface NextProjectIdRepository extends MongoRepository<NextProjectId, String> {
}