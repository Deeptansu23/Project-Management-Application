package com.projectmanagement.service.serviceimpl;

import com.projectmanagement.dto.UserDto;
import com.projectmanagement.entity.AppUserPermission;
import com.projectmanagement.entity.AppUserRole;
import com.projectmanagement.entity.DepartmentDetails;
import com.projectmanagement.entity.User;
import com.projectmanagement.exception.NoSuchUserExistException;
import com.projectmanagement.id.NextUserId;
import com.projectmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DepartmentServiceImpl departmentService;
    @Autowired
    private NextUserIdRepository idRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    AppPermissionRepository appUserPermission;
    @Autowired
    AppUserRepository appUserRepository;
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        saveUserDetail();
        User user=userRepository.findAll().stream()
                .filter(user1->user1.getUsername().equalsIgnoreCase(username))
                .findFirst().get();
        UserDto userDto=new UserDto(user);

        return userDto;
    }

    public void saveUserDetail() {
        if (userRepository.count() == 0) {
            appUserPermission.save(new AppUserPermission("Employee_Read"));
            appUserPermission.save(new AppUserPermission("Employee_Write"));

            appUserRepository.save(new AppUserRole("Employee", appUserPermission.findAll().stream().filter(perm -> perm.getPermission().equalsIgnoreCase("Employee_Read")).collect(Collectors.toSet())));
            appUserRepository.save(new AppUserRole("Admin", appUserPermission.findAll().stream().collect(Collectors.toSet())));
            AppUserRole userRole = appUserRepository.findAll().stream().filter(role ->
                    role.getRole().equalsIgnoreCase("Admin")).findFirst().get();
            userRepository.save(new User("Deeptansu kumar Sahu","deeptansu23@gmail.com", passwordEncoder.encode("Deepu@123"), userRole, userRole.getPermissions(), true, true, true, true,"9645330312", "D-100", false,"Not Assigned",0));
                    }
    }

    public void saveEmployee(User user) {
        AppUserRole userRole1 = appUserRepository.findAll().stream().filter(role ->
                role.getRole().equalsIgnoreCase("Employee")).findFirst().get();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setProjectAssigned(false);
        user.setProjectId("Not Assigned");
        user.setOtp(0);
        user.setRole(userRole1);
        user.setPermissions(userRole1.getPermissions());
        userRepository.save(user);
        logger.info(String.valueOf(user));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String generateCustomUserId() {
        NextUserId nextId = idRepository.findById("userId").orElse(new NextUserId("userId", 0));
        int currentNextId = nextId.getNextUserId();
        String sequentialId = String.format("%03d", currentNextId + 1);
        nextId.setNextUserId(currentNextId + 1);
        idRepository.save(nextId);
        return "FT-" + sequentialId;
    }

    public User getData(String username){
        return userRepository.findAll().stream().filter(user->user.getUsername().equalsIgnoreCase(username)).findFirst().get();
    }

    public User findUserByUserId(String userId) {
        try{
            return userRepository.findByUserId(userId);
        }catch (NoSuchUserExistException error){
             throw new NoSuchUserExistException("User with userId '" + userId + "' not found.");
        }
    }

    public void deleteEmployee(String userId) {
        User employee = userRepository.findByUserId(userId);

        String departmentId = employee.getDepartment();
        DepartmentDetails department = departmentRepository.findByDepartmentId(departmentId);

        if (department != null) {
            department.setNoOfEmployees(department.getNoOfEmployees() - 1);
            department.setEmployeesAvailable(department.getEmployeesAvailable() - 1);
            departmentRepository.save(department);
        }
        userRepository.delete(employee);
    }

    public void updateEmployee(User users) {
        User user = userRepository.findByUserId(users.getUserId());
        user.setUsername(users.getUsername());
        user.setFullName(users.getFullName());
        user.setContactNo(users.getContactNo());
        userRepository.save(user);
    }

    public List<User> getAllManagers() {
        String manager = "Manager";
        String managerId = departmentService.getAllManagers(manager).toString();
        return userRepository.findByDepartmentAndProjectAssignedFalse(managerId);
    }

    public List<User> getAllArchitects() {
        String architect = "Solution_Architect";
        String architectId = departmentService.getAllArchitects(architect).toString();
        return userRepository.findByDepartmentAndProjectAssignedFalse(architectId);
    }

    public boolean IsUserExist(String username) {
        User exist = userRepository.findByUsername(username);
        if (exist !=null)  return true;
        return false;
    }

    public List<User> getEmployeeByProjectId(String projectId) {
        return userRepository.findByProjectId(projectId);
    }

    public User findUserByUsername(String username) {
        try{
            return userRepository.findByUsername(username);
        }catch (NoSuchUserExistException message){
            throw message;
        }
    }

    public void changeUserPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}