package com.projectmanagement.repository;

import com.projectmanagement.entity.EmployeeSuggestion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeSuggestionRepository extends MongoRepository<EmployeeSuggestion,String> {
    EmployeeSuggestion findBySuggestionId(String suggestionId);
}
