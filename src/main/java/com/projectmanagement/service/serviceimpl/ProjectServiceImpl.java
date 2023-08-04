package com.projectmanagement.service.serviceimpl;

import com.projectmanagement.entity.ProjectDetails;
import com.projectmanagement.exception.NoSuchProjectExistException;
import com.projectmanagement.id.NextProjectId;
import com.projectmanagement.repository.NextProjectIdRepository;
import com.projectmanagement.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private NextProjectIdRepository idRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    public void saveProjectDetails(ProjectDetails projectDetails) {
        try {
            projectRepository.save(projectDetails);
            logger.info(String.valueOf(projectDetails));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<ProjectDetails> getAllProjects() {
        return projectRepository.findAll();
    }

    public String generateCustomProjectId() {
        NextProjectId nextId = idRepository.findById("projectId").orElse(new NextProjectId("projectId", 0));
        int currentNextId = nextId.getNextProjectId();
        String sequentialId = String.format("%03d", currentNextId + 1);
        nextId.setNextProjectId(currentNextId + 1);
        idRepository.save(nextId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = sdf.format(new Date());
        return sequentialId +"-"+ dateStr;
    }

    public ProjectDetails getData(String id){
        return projectRepository.findAll().stream()
                .filter(usr->usr.getProjectId().equalsIgnoreCase(id)).findFirst().get();
    }

    public ProjectDetails findByProjectId(String projectId) {
        try {
            return projectRepository.findByProjectId(projectId);
        } catch (Exception e) {
            throw new NoSuchProjectExistException("there is no such project exist" +projectId);
        }
    }


    public void updateProjectDetails(String projectId, int totalEmployeesAllocated) throws NoSuchProjectExistException{
        try {
            ProjectDetails projectDetails = projectRepository.findByProjectId(projectId);

            if (projectDetails != null) {
                projectDetails.setNoOfEmployeesAllocated(totalEmployeesAllocated);
                projectRepository.save(projectDetails);
            }
        } catch (Exception e) {
            throw new NoSuchProjectExistException("There is no project exist" +projectId);
        }
    }



    public ProjectDetails getProjectDetails(String projectId) throws NoSuchProjectExistException {
        ProjectDetails projectDetails = projectRepository.findByProjectId(projectId);

        if (projectDetails != null) {
            return projectDetails;
        } else {
            throw new NoSuchProjectExistException("Project with ID " + projectId + " not found.");
        }
    }

}
