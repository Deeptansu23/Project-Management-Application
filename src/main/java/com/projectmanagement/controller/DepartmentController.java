package com.projectmanagement.controller;

import com.projectmanagement.dto.DepartmentDto;
import com.projectmanagement.entity.DepartmentDetails;
import com.projectmanagement.entity.ProjectDetails;
import com.projectmanagement.exception.DepartmentAlreadyExistException;
import com.projectmanagement.service.serviceimpl.DepartmentServiceImpl;
import com.projectmanagement.service.serviceimpl.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentServiceImpl departmentService;
    @Autowired
    private ProjectServiceImpl projectService;

    @GetMapping("/add-department")
    public String addDepartmentForm(Model model) {
        model.addAttribute("department", new DepartmentDetails());
        return "create-new-department";
    }

    @PostMapping("/add-department")
    public String addDepartmentSubmit(@ModelAttribute DepartmentDetails department) {
        boolean isExist = departmentService.departmentExists(department.getDepartmentName());
        if (isExist) {
            throw new DepartmentAlreadyExistException("Department already exists!");
        } else {
            String customId = departmentService.generateCustomDepartmentId();
            department.setDepartmentId(customId);
            departmentService.saveDepartment(department);
            return "redirect:/department/add-department?success=true";
        }
    }

    @ExceptionHandler(DepartmentAlreadyExistException.class)
    public String departmentAlreadyExistException(Model model){
        String message = "Department already exist";
        model.addAttribute("successMessage", message);
        return "success-page-admin";
    }


    @GetMapping("/view-bench-pool")
    public String getAllDepartments(Model model) {
        List<DepartmentDetails> departmentList = departmentService.getAllDepartment();
        model.addAttribute("listAll", departmentList);
        return "view-bench-pool";
    }

    @GetMapping("/add-required-employees")
    public String showAddEmployeesForm(Model model) {
        model.addAttribute("departmentDto", new DepartmentDto());
        model.addAttribute("departmentList", departmentService.getAllDepartments());
        return "employee-requirement-form";
    }

    @PostMapping("/add-required-employees")
    public String addEmployees(@ModelAttribute("departmentDto") DepartmentDto departmentDto, Model model, HttpSession session) {
        Map<String, Integer> employeesRequired = departmentDto.getEmployeesRequired();

        String projectId = (String) session.getAttribute("projectId");

        ProjectDetails projectDetails = projectService.getProjectDetails(projectId);
        String manager = projectDetails.getProjectManager();
        String architect = projectDetails.getSolutionArchitect();

        boolean employeesAvailable = departmentService.checkEmployeesAvailability(employeesRequired, projectId,manager,architect);
        if (employeesAvailable) {
            model.addAttribute("employeesAvailable", true);
            model.addAttribute("departmentList", departmentService.getAllDepartments());

            return "redirect:/project/project-success";
        }
        else {
            return "redirect:/department/add-required-employees?error=true";
        }
    }
}
