package com.projectmanagement.repository;


import com.projectmanagement.entity.AppUserPermission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppPermissionRepository extends MongoRepository<AppUserPermission,String> {
}
