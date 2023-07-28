package com.projectmanagement.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Project_Details")
public class ProjectDetails {

    @Id
    private String projectId;
    private String organizationName;
    private String requirements;
    private String projectManager;
    private String solutionArchitect;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateCreated;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    private int noOfEmployeesAllocated;

}
