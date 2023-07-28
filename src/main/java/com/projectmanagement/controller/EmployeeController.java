package com.projectmanagement.controller;

import com.projectmanagement.entity.DepartmentDetails;
import com.projectmanagement.entity.ProjectDetails;
import com.projectmanagement.entity.User;
import com.projectmanagement.service.serviceimpl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeServiceImpl employeeService;
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private DepartmentServiceImpl departmentService;
    @Autowired
    private EmailServiceImpl emailService;

    @GetMapping("/view-profile")
    public String showEmployeeDetails(Model model, Principal principal) {
        String username = principal.getName();
        User employeeDetails = employeeService.findByUsername(username);
        List<DepartmentDetails> departments = departmentService.getAllDepartment();
        Map<String, String> departmentMap = new HashMap<>();
        for (DepartmentDetails department : departments) {
            departmentMap.put(department.getDepartmentId(), department.getDepartmentName());
        }

        model.addAttribute("departments", departmentMap);
        model.addAttribute("employeeDetails", employeeDetails);
        return "show-employee-details";
    }

    @GetMapping("/update-profile/{id}")
    public String updateUser(@PathVariable String id, Model model) {

        model.addAttribute("user", employeeService.getData(id));
        return "edit-employee-details";
    }

    @PostMapping("/update-profile")
    public String update(@ModelAttribute User user){
        employeeService.updateEmployee(user);
        return "redirect:/employee/update-success";
    }

    @GetMapping("/view-project")
    public String showProjectDetails(Model model, Principal principal) {
        String username = principal.getName();
        User employeeDetails = employeeService.findByUsername(username);
        if (employeeDetails.isProjectAssigned()) {
            String projectId = employeeDetails.getProjectId();
            ProjectDetails projectDetails = projectService.findByProjectId(projectId);
            model.addAttribute("projectDetails", projectDetails);
            return "show-project-details";
        }
        return "project-not-assigned-page";
    }

    @GetMapping("/view-employees")
    public String showEmployees(Model model, Principal principal) {
        String username = principal.getName();
        User employeeDetails = employeeService.findByUsername(username);

        String employeeDepartment = employeeDetails.getDepartment();
        List<User> userList = employeeService.getUsersByDepartment(employeeDepartment);
        List<DepartmentDetails> departments = departmentService.getAllDepartment();
        Map<String, String> departmentMap = new HashMap<>();
        for (DepartmentDetails department : departments) {
            departmentMap.put(department.getDepartmentId(), department.getDepartmentName());
        }

        model.addAttribute("departments", departmentMap);
            model.addAttribute("userLists", userList);
            return "view-employee-detail";
    }

    @GetMapping("/view-project-members")
    public String showProjectMembers(Model model, Principal principal) {
        String username = principal.getName();
        User employeeDetails = employeeService.findByUsername(username);

        String employeeDepartment = employeeDetails.getProjectId();
        List<User> userList = employeeService.getUsersByProjectId(employeeDepartment);
        List<DepartmentDetails> departments = departmentService.getAllDepartment();
        Map<String, String> departmentMap = new HashMap<>();
        for (DepartmentDetails department : departments) {
            departmentMap.put(department.getDepartmentId(), department.getDepartmentName());
        }

        model.addAttribute("departments", departmentMap);
        model.addAttribute("userLists", userList);
        return "view-project-members";
    }

    @GetMapping("/update-success")
    public String showSuccessPage(Model model) {
        String successMessage = "Employee Details Updated Successfully";
        model.addAttribute("successMessage", successMessage);
        return "success-page-employee";
    }

}
