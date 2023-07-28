package com.projectmanagement.config;


import com.projectmanagement.entity.User;
import com.projectmanagement.repository.UserRepository;
import com.projectmanagement.service.serviceimpl.EmailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private EmailServiceImpl emailService;
    private static final Logger logger = LoggerFactory.getLogger(CustomSuccessHandler.class);


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User users = userRepository.findAll().stream().filter(user->user.getUsername().equalsIgnoreCase(username)).findFirst().get();
        String output = users.getRole().getRole();

        emailService.sendOtpEmail(users);

        logger.info(output);

        if(output.equalsIgnoreCase("employee") || output.equalsIgnoreCase("admin") )
            redirectUrl="/app/v1/otp/verification";
        else
            redirectUrl="app/v1/login?error";

        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

}
