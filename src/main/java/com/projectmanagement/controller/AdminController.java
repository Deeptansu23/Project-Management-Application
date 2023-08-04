package com.projectmanagement.controller;

import com.projectmanagement.entity.DepartmentDetails;
import com.projectmanagement.entity.User;

import com.projectmanagement.service.serviceimpl.DepartmentServiceImpl;
import com.projectmanagement.service.serviceimpl.EmailServiceImpl;
import com.projectmanagement.service.serviceimpl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminServiceImpl userService;
    @Autowired
    private DepartmentServiceImpl departmentService;
    @Autowired
    private EmailServiceImpl emailService;



    @GetMapping("/add-employee")
    public String showUserForm(Model model) {
        List<DepartmentDetails> departments = departmentService.getAllDepartment();
        model.addAttribute("departments", departments);
        model.addAttribute("user", new User());
        return "employee-creation-form";
    }

    @PostMapping("/add-employee")
    public String addUser(@ModelAttribute User user) {
        boolean isExist = userService.IsUserExist(user.getUsername());
        if(!isExist) {
            String customId = userService.generateCustomUserId();
            user.setUserId(customId);

            String rawPassword = user.getPassword();

            userService.saveEmployee(user);
            User user1 = new User();
            user1.setUsername(user.getUsername());
            user1.setFullName(user.getFullName());
            user1.setPassword(rawPassword);
            emailService.sendEmail(user1);

            if (user.getDepartment() != null) {
                departmentService.incrementEmployeesAllocated(user.getDepartment());
            }

            return "redirect:/admin/add-employee?success=true";
        }
        else
            return "redirect:/admin/add-employee?error=true";
    }

    @GetMapping("/view-employee")
    public String listUsers(Model model) {
        List<User> userList = userService.getAllUsers();
        List<DepartmentDetails> departments = departmentService.getAllDepartment();
        Map<String, String> departmentMap = new HashMap<>();
        for (DepartmentDetails department : departments) {
            departmentMap.put(department.getDepartmentId(), department.getDepartmentName());
        }

        model.addAttribute("departments", departmentMap);
        model.addAttribute("userList", userList);
        return "view-employee-details";
    }

    @GetMapping("/update-employee/{username}")
    public String updateUser(@PathVariable String username, Model model) {
        model.addAttribute("user", userService.findUserByUsername(username));
        return "update-employee-details";
    }

    @PostMapping("/update-employee")
    public String update(@ModelAttribute User user){
        boolean isExist = userService.IsUserExist(user.getUsername());   // Checking User is existed or not
        if(!isExist) {
            userService.updateEmployee(user);
            return "redirect:/admin/update-success";
        }

        else return "redirect:/admin/update-failed";
    }

    @GetMapping("/delete-employee/{userId}")
    public String deleteUser(@PathVariable String userId, Model model) {

        User employee = userService.findUserByUserId(userId);
        if (employee.isProjectAssigned()) {
            return "redirect:/admin/view-employee?error=true";
        } else {
            userService.deleteEmployee(userId);
            return "redirect:/admin/view-employee?success=true";
        }

    }

    @GetMapping("/show-employee")
    public String viewEmployeeEmail(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("userList", userList);
        return "employee-details-view";
    }

    @GetMapping("/send-email-employee/{username}")
    public String fetchEmail(@PathVariable String username, Model model) {
        model.addAttribute("user", userService.findUserByUsername(username));
        return "send-email-page";
    }

    @PostMapping("/send-email-employee")
    public String sentEmail(@ModelAttribute User user, @RequestParam("message") String message){
        User user1 = new User();
        user1.setUserId(user.getUserId());
        user1.setFullName(user.getFullName());
        user1.setUsername(user.getUsername());

        emailService.sendEmailToEmployee(user1,message);
        return "redirect:/admin/email-success";
    }

    @GetMapping("/update-success")
    public String showSuccessPage(Model model) {
        String successMessage = "Employee Details Updated Successfully";
        model.addAttribute("successMessage", successMessage);
        return "success-page-admin";
    }

    @GetMapping("/update-failed")
    public String showFailedPage(Model model) {
        String successMessage = "Username already exist please try with another one!";
        model.addAttribute("successMessage", successMessage);
        return "success-page-admin";
    }

    @GetMapping("/email-success")
    public String showEmailSuccessPage(Model model) {
        String successMessage = "Email Sent Successfully";
        model.addAttribute("successMessage", successMessage);
        return "success-page-admin";
    }

}
