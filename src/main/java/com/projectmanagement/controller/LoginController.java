package com.projectmanagement.controller;

import com.projectmanagement.entity.User;
import com.projectmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller("/")
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/app/v1/home-page")
    public String homePageView(){
        return "home-page";
    }
    @GetMapping("/app/v1/login")
    public String loginPage(){return "login-form";}

    @GetMapping("app/v1/otp/verification")
    public String otpSent() {
        return "otp-verification";

    }

    @PostMapping("/app/v1/otp/verification")
    public String otpVerification(@ModelAttribute("otpValue") User userDetail) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User users = userRepository.findByUsername(user.getUsername());
        String role = users.getRole().getRole();

        if(users.getOtp() == userDetail.getOtp() && role.equals("Employee")) {
            users.setOtp(0);
            userRepository.save(users);
            return "redirect:/app/v1/employee";
        }
        else if(users.getOtp() == userDetail.getOtp() && role.equals("Admin")){
            users.setOtp(0);
            userRepository.save(users);
            return "redirect:/app/v1/admin";
        }
        else
            return "redirect:/app/v1/otp/employee?error";
    }

    @GetMapping("/app/v1/employee")
    public String login(){
        return "employee-dashboard";
    }

    @GetMapping("/app/v1/admin")
    public String admin(){
        return "admin-dashboard";
    }

}
