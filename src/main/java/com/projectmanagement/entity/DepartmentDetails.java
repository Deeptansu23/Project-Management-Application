package com.projectmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "Department_Details")
public class DepartmentDetails {

    @Id
    private String departmentId;

    private String departmentName;
    private int noOfEmployees;
    private int employeesAllocated;
    private int employeesAvailable;

}
