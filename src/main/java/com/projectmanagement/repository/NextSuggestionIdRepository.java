package com.projectmanagement.repository;

import com.projectmanagement.id.NextSuggestionId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NextSuggestionIdRepository extends MongoRepository <NextSuggestionId, String >{
}
