package com.projectmanagement.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DepartmentDto {
    private String departmentId;
    private String departmentName;
    private int employeesAvailable;
    private Map<String, Integer> employeesRequired;
}