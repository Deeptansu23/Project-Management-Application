package com.projectmanagement.controller;

import com.projectmanagement.entity.User;
import com.projectmanagement.repository.UserRepository;
import com.projectmanagement.service.serviceimpl.AdminServiceImpl;
import com.projectmanagement.service.serviceimpl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
    @Autowired
    private AdminServiceImpl userService;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/user")
    public String forgotPassword()
    {
        return "username-verification";
    }
    @PostMapping("/user")
    public String usernameVerification(@ModelAttribute("username") User userDetail, RedirectAttributes redirectAttributes) {

        boolean exist = userService.IsUserExist(userDetail.getUsername());
        if (exist) {
            User user = userService.findUserByUsername(userDetail.getUsername());
            emailService.sendForgotPasswordOtp(user);

            redirectAttributes.addAttribute("username", user.getUsername());

            return "redirect:/forgot-password/otp/verification";
        }
        else {
            return "redirect:/forgot-password/user?error";
        }
    }

    @GetMapping("/otp/verification")
    public String otpSent(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "forgot-password-otp-form";
    }

    @PostMapping("/otp/verification")
    public String otpVerification(@ModelAttribute("otpValue") User userDetail) {

        User users = userRepository.findByUsername(userDetail.getUsername());
        if(users.getOtp() == userDetail.getOtp()) {
            return "redirect:/forgot-password/change-password?username=" + userDetail.getUsername();
        }
        else
            return "redirect:/forgot-password/otp/verification?username=error";
    }

    @GetMapping("/change-password")
    public String showPasswordChangeForm(@RequestParam("username") String username, Model model) {
        User user = userService.findUserByUsername(username);
        model.addAttribute("user", user);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String processPasswordChange(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        userService.changeUserPassword(user.getUsername(), user.getPassword());
        return "redirect:/forgot-password/success";
    }

    @GetMapping("/success")
    public String showSuccessPage(Model model) {
        String successMessage = "Password Changed Successfully";
        String successMessage1 = "Go back to Home Page";

        model.addAttribute("successMessage", successMessage);
        return "success-page-password";
    }
}
