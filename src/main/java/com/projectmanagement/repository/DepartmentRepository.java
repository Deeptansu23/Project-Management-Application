package com.projectmanagement.repository;

import com.projectmanagement.entity.DepartmentDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface DepartmentRepository extends MongoRepository<DepartmentDetails, Integer> {
    DepartmentDetails findByDepartmentName(String departmentName);
    DepartmentDetails findByDepartmentId(String departmentId);
}


