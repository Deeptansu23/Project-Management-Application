package com.projectmanagement.service.serviceimpl;

import com.projectmanagement.entity.EmployeeSuggestion;
import com.projectmanagement.id.NextSuggestionId;
import com.projectmanagement.repository.EmployeeSuggestionRepository;
import com.projectmanagement.repository.NextSuggestionIdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EmployeeSuggestionServiceImpl {
    @Autowired
    private EmployeeSuggestionRepository employeeSuggestionRepository;
    @Autowired
    private NextSuggestionIdRepository idRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeSuggestionServiceImpl.class);

    public void saveSuggestion(EmployeeSuggestion suggestion){
        employeeSuggestionRepository.save(suggestion);
        logger.info(String.valueOf(suggestion));
    }


    public String generateCustomDepartmentId() {
        try {
            NextSuggestionId nextId = idRepository.findById("suggestionId")
                    .orElse(new NextSuggestionId("suggestionId", 0));

            int currentNextId = nextId.getNextSuggestionId();
            String sequentialId = String.format("%03d", currentNextId + 1);
            nextId.setNextSuggestionId(currentNextId + 1);
            idRepository.save(nextId);
            return "S-" + sequentialId;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while generating the custom department ID.";
        }
    }


    public List<EmployeeSuggestion> getAllSuggestions() {
        try {
            return employeeSuggestionRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public void deleteEmployeeSuggestionBySuggestionId(String suggestionId) {
        EmployeeSuggestion suggestion = employeeSuggestionRepository.findBySuggestionId(suggestionId);
        employeeSuggestionRepository.delete(suggestion);
    }

}
