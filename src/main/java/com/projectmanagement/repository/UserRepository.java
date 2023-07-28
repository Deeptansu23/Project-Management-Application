package com.projectmanagement.repository;

import com.projectmanagement.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    List<User> findByDepartmentAndProjectAssignedFalse(String department);
    User findByUserId(String userId);

    User findByUsername(String username);

    List<User> findByFullName(String fullName);

    List<User> findByDepartment(String department);

    List<User> findByProjectId(String projectId);
}
