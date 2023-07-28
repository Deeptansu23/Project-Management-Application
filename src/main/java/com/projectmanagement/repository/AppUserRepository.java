package com.projectmanagement.repository;


import com.projectmanagement.entity.AppUserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepository extends MongoRepository<AppUserRole,String> {
}
