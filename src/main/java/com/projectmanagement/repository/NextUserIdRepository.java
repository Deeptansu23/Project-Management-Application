package com.projectmanagement.repository;

import com.projectmanagement.id.NextUserId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NextUserIdRepository extends MongoRepository<NextUserId, String> {
}