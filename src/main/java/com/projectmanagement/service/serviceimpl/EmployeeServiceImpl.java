package com.projectmanagement.service.serviceimpl;

import com.projectmanagement.entity.AppUserRole;
import com.projectmanagement.entity.User;
import com.projectmanagement.exception.NoSuchProjectExistException;
import com.projectmanagement.exception.NoSuchUserExistException;
import com.projectmanagement.repository.AppPermissionRepository;
import com.projectmanagement.repository.AppUserRepository;
import com.projectmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AppPermissionRepository appUserPermission;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public User findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (NoSuchUserExistException e) {
           throw new NoSuchProjectExistException("There is no user is exist" +username);
        }
    }



    public User getData(String id){
        return userRepository.findAll().stream()
                .filter(user->user.getUserId().equalsIgnoreCase(id)).findFirst().get();
    }

    public void updateEmployee(User users) {
        try {
            User user = userRepository.findByUserId(users.getUserId());
            user.setFullName(users.getFullName());
            user.setContactNo(users.getContactNo());
            user.setPassword(passwordEncoder.encode(users.getPassword()));
            userRepository.save(user);
            logger.info(String.valueOf(user));
        } catch (Exception e) {
        throw new NoSuchUserExistException("There is no user is exist " +users);
        }
    }


    public List<User> getUsersByDepartment(String department) {
        try {
            return userRepository.findByDepartment(department);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public List<User> getUsersByProjectId(String projectId) {
        try {
            return userRepository.findByProjectId(projectId);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
