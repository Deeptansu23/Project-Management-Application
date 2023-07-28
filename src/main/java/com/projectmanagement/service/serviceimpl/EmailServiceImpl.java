package com.projectmanagement.service.serviceimpl;

import com.projectmanagement.entity.User;
import com.projectmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

        @Autowired
        private JavaMailSender javaMailSender;
        @Autowired
        private UserRepository userRepository;

        private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

        public void sendEmail(User user1) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("kamilpraseej742@gmail.com");
            message.setTo(user1.getUsername());
            message.setSubject("Welcome to Future Technologies - Your Login Credentials");
            message.setText("Dear "+ user1.getFullName()+ ",\n\n"+
                    "We are thrilled to welcome you to the Future Technologies team! On behalf of the entire company, I would like to extend our warmest greetings and express our gratitude for joining us."+
                    "\n\nHere are your login credentials to access our company's portal:\n\n"+
                    "Username : "+user1.getUsername()+ "\n\nPassword : "+user1.getPassword()
                    +"\n\nAt Future Technologies, we believe in fostering a collaborative and innovative work environment, where every employee plays a crucial role in our success. We are confident that your skills and expertise will make a significant impact on our company's growth and achievements." +
                    "\n\nOnce again, welcome aboard! We are excited to have you as a part of our Future Technologies family." +
                    "\n\nBest Regards,\n\nKamil Praseej\nHR Manager\nFuture Technologies");
            javaMailSender.send(message);
            logger.info("Email Sent Successfully to "+user1.getUsername());
        }

        public void sendProjectEmail(User employee) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("kamilpraseej742@gmail.com");
            message.setTo(employee.getUsername());
            message.setSubject("New Project Assignment - Action Required");
            message.setText("Dear "+employee.getFullName()+",\n\n"+
                    "We hope this email finds you well. We are excited to inform you that you have been assigned to a new project at Future Technologies. Congratulations on this new opportunity!\n\n"+
                    "You can log in to our company's portal to view more details about the project:\n\n"+
                    "In the portal, you will find essential project-related information, including timelines, tasks, resources, and team members. We encourage you to review the details carefully and reach out to your project manager if you have any questions or need further clarification.\n\n"+
                    "Your dedication and hard work have been recognized, and we have full confidence that your contributions to this project will be invaluable to its success.\n\n"+
                    "Once again, congratulations on being selected for this project! We believe that your skills and expertise will play a significant role in its accomplishment.\n\n"+
                    "\n\nBest Regards,\n\nKamil Praseej\nHR Manager\nFuture Technologies");
            javaMailSender.send(message);
            logger.info("Email Sent Successfully to "+employee.getUsername());

        }

        public void sendSuggestionEmail(User user1, String messageText) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("kamilpraseej742@gmail.com");
            message.setTo(user1.getUsername());
            message.setSubject("Employee Suggestion Message");
            message.setText("Employee ID: "+user1.getUserId()+"\nEmployee Name: "+user1.getFullName()+"\nDepartment ID : "+user1.getDepartment()+"\nProject ID: "+user1.getProjectId()+"\n\nDear Admin,\n\n"+messageText);
            javaMailSender.send(message);
            logger.info("Email Sent Successfully to "+user1.getUsername());

        }

        public void sendEmailToEmployee(User user1, String messageText) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("kamilpraseej742@gmail.com");
            message.setTo(user1.getUsername());
            message.setSubject("Message from Admin - Future Technologies");
            message.setText("Dear "+user1.getFullName()+",\n\n"+messageText);
            javaMailSender.send(message);
            logger.info("Email Sent Successfully to "+user1.getUsername());

        }

        public String sendOtpEmail(User users) {
            int otp =  (int) (Math.random() * 9000) + 1000;
            users.setOtp(otp);
            userRepository.save(users);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("kamilpraseej742@gmail.com");
            message.setTo(users.getUsername());
            message.setSubject("Your One-Time Password (OTP) for Future Technologies");
            message.setText("Dear "+users.getFullName()+",\n\n"+
                    "Thank you for logging in to Future Technologies. To ensure the security of your account, we have generated a one-time password (OTP) for you.\n\n"+
                    "OTP : "+users.getOtp()+"\n\nPlease use this OTP to complete your login process. It is valid for a limited time and for a single use only.\n\n"+
                    "If you did not attempt to log in or receive this email, please ignore it. However, if you suspect any unauthorized access to your account, please contact our support team immediately.\n\n" +
                    "Best Regards, \nFuture Technologies Team");
            javaMailSender.send(message);
            return "success";
        }

        public void sendForgotPasswordOtp(User users) {
            int otp =  (int) (Math.random() * 9000) + 1000;
            users.setOtp(otp);
            userRepository.save(users);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("kamilpraseej742@gmail.com");
            message.setTo(users.getUsername());
            message.setSubject("Future Technologies - Forgot Password OTP Verification");
            message.setText("Dear "+users.getFullName()+",\n\n"+
                    "We have received a request to reset the password for your Future Technologies account. To proceed with the password reset, please use the One-Time Password (OTP) provided below:\n\n"+
                    "OTP : "+users.getOtp()+"\n\nPlease enter this OTP on the password reset page to verify your account and set a new password.\n"+
                    "Thank you for using Future Technologies services. If you have any questions or need further assistance, please don't hesitate to contact our support team at futuretechnologies@gmail.com\n\n"+
                    "Best Regards, \nFuture Technologies Team");
            javaMailSender.send(message);
        }

}


