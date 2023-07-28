package com.projectmanagement.repository;

import com.projectmanagement.entity.ProjectDetails;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProjectRepository extends MongoRepository<ProjectDetails,Integer> {
    ProjectDetails findByProjectId(String projectId);
}
