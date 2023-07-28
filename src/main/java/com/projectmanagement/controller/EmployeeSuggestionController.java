package com.projectmanagement.controller;

import com.projectmanagement.entity.EmployeeSuggestion;
import com.projectmanagement.entity.User;
import com.projectmanagement.service.serviceimpl.EmailServiceImpl;
import com.projectmanagement.service.serviceimpl.EmployeeServiceImpl;
import com.projectmanagement.service.serviceimpl.EmployeeSuggestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/suggestion")
public class EmployeeSuggestionController {
    @Autowired
    private EmployeeServiceImpl employeeService;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private EmployeeSuggestionServiceImpl suggestionService;
    @GetMapping("/add-suggestion")
    public String showSuggestionPage(Model model, Principal principal) {
        String username = principal.getName();
        User employeeDetails = employeeService.findByUsername(username);
        model.addAttribute("employeeDetails", employeeDetails);
        return "suggestion-page";
    }

    @PostMapping("/add-suggestion")
    public String  sendSuggestion(@ModelAttribute User user, @RequestParam("message") String message){

        EmployeeSuggestion suggestion = new EmployeeSuggestion();
        String customId = suggestionService.generateCustomDepartmentId();
        suggestion.setSuggestionId(customId);
        suggestion.setUserId(user.getUserId());
        suggestion.setFullName(user.getFullName());
        suggestion.setUsername(user.getUsername());
        suggestion.setDepartment(user.getDepartment());
        suggestion.setProjectId(user.getProjectId());
        suggestion.setMessages(message);
        suggestionService.saveSuggestion(suggestion);

        emailService.sendSuggestionEmail(user,message);
        return "redirect:/suggestion/email-success";
    }
    @GetMapping("/view-suggestions")
    public String getAllDepartments(Model model) {
        List<EmployeeSuggestion> suggestionList = suggestionService.getAllSuggestions();
        model.addAttribute("listAll", suggestionList);
        return "view-suggestion-list";
    }

    @GetMapping("/email-success")
    public String showSuccessPage(Model model) {
        String successMessage = "Email Sent Successfully";
        model.addAttribute("successMessage", successMessage);
        return "success-page-employee";    }

    @GetMapping("/delete-suggestion/{suggestion}")
    public String deleteUser(@PathVariable String suggestion) {
        suggestionService.deleteEmployeeSuggestionBySuggestionId(suggestion);
        return "redirect:/suggestion/view-suggestions";
    }

}
