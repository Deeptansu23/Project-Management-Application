package com.projectmanagement.repository;

import com.projectmanagement.id.NextDepartmentId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NextDepartmentIdRepository extends MongoRepository<NextDepartmentId, String> {
}