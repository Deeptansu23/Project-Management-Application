package com.projectmanagement.controller;

import com.projectmanagement.entity.DepartmentDetails;
import com.projectmanagement.entity.ProjectDetails;
import com.projectmanagement.entity.User;
import com.projectmanagement.service.serviceimpl.AdminServiceImpl;
import com.projectmanagement.service.serviceimpl.DepartmentServiceImpl;
import com.projectmanagement.service.serviceimpl.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private DepartmentServiceImpl departmentService;

    @GetMapping("/dashboard")
    public String showProjectDashboard(Model model) {
        return "admin-dashboard";
    }

    @GetMapping("/add-project")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new ProjectDetails());
        List<User> manager = adminService.getAllManagers();
        model.addAttribute("managers",manager);
        List<User> architect = adminService.getAllArchitects();
        model.addAttribute("architects", architect);
        return "project-creation-form";
    }

    @PostMapping("/save-project")
    public String saveProjectDetails(@ModelAttribute ProjectDetails projectDetails, HttpSession session) {
        String customId = projectService.generateCustomProjectId();
        projectDetails.setProjectId(customId);
        projectService.saveProjectDetails(projectDetails);

        session.setAttribute("projectId", customId);
        return "redirect:/department/add-required-employees";
    }

    @GetMapping("/view-projects")
    public String showProjectDetails(Model model) {
        List<ProjectDetails> allProjects = projectService.getAllProjects();
        model.addAttribute("listAll", allProjects);
        return "view-project-details";
    }

    @GetMapping("/update-project/{id}")
    public String updateProject(@PathVariable String id, Model model) {
        ProjectDetails project = projectService.getData(id);
        model.addAttribute("project",project);
        return "update-project-details";
    }

    @GetMapping("/view-project-team/{id}")
    public String ViewProjectTeam(@PathVariable String id, Model model) {
        ProjectDetails project = projectService.getData(id);
        List<User> userList = adminService.getEmployeeByProjectId(project.getProjectId());
        List<DepartmentDetails> departments = departmentService.getAllDepartment();
        Map<String, String> departmentMap = new HashMap<>();

        for (DepartmentDetails department : departments) {
            departmentMap.put(department.getDepartmentId(), department.getDepartmentName());
        }
        model.addAttribute("departments", departmentMap);
        model.addAttribute("userLists", userList);
        model.addAttribute("project",project);
        return "show-project-members";
    }

    @PostMapping("/update-project")
    public String update(@ModelAttribute ProjectDetails project){
        projectService.saveProjectDetails(project);
        return "redirect:/project/update-success";
    }

    @GetMapping("/project-success")
    public String showSuccessPage(Model model) {
        String successMessage = "Project Created Successfully";
        model.addAttribute("successMessage", successMessage);
        return "success-page-admin";    }

    @GetMapping("/update-success")
    public String showSuccessPageProject(Model model) {
        String successMessage = "Project Details Updated Successfully";
        model.addAttribute("successMessage", successMessage);
        return "success-page-admin";    }
}