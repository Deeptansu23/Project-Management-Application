package com.projectmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Employee_Suggestion")
public class EmployeeSuggestion {

    @Id
    private String suggestionId;
    private String userId;
    private String fullName;
    private String username;
    private String department;
    private String projectId;
    private String messages;

}
