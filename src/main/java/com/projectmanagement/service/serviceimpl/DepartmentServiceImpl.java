package com.projectmanagement.service.serviceimpl;


import com.projectmanagement.dto.DepartmentDto;
import com.projectmanagement.entity.DepartmentDetails;
import com.projectmanagement.entity.User;
import com.projectmanagement.id.NextDepartmentId;
import com.projectmanagement.repository.DepartmentRepository;
import com.projectmanagement.repository.NextDepartmentIdRepository;
import com.projectmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private NextDepartmentIdRepository idRepository;

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    public void incrementEmployeesAllocated(String departmentId) {
        DepartmentDetails department = departmentRepository.findByDepartmentId(departmentId);

            department.setNoOfEmployees(department.getNoOfEmployees() +1);
            department.setEmployeesAvailable(department.getEmployeesAvailable() + 1);
            departmentRepository.save(department);

    }

    public void saveDepartment(DepartmentDetails department) {
        departmentRepository.save(department);
        logger.info(String.valueOf(department));
    }

    public List<DepartmentDetails> getAllDepartment() {
        return departmentRepository.findAll();
    }

    public String generateCustomDepartmentId() {
        NextDepartmentId nextId = idRepository.findById("departmentId")
                .orElse(new NextDepartmentId("departmentId", 0));
        int currentNextId = nextId.getNextDepartmentId();
        String sequentialId = String.format("%03d", currentNextId + 1);
        nextId.setNextDepartmentId(currentNextId + 1);
        idRepository.save(nextId);
        return "D-" + sequentialId;
    }

    public List<DepartmentDto> getAllDepartments() {
        List<DepartmentDetails> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public boolean checkEmployeesAvailability(Map<String, Integer> employeesRequired, String projectId, String manager,String architect) {
        int totalEmployeesAllocated = 0;

        for (Map.Entry<String, Integer> entry : employeesRequired.entrySet()) {
            String departmentId = entry.getKey();
            int employeesRequiredCount = entry.getValue();

            DepartmentDetails department = departmentRepository.findByDepartmentId(departmentId);

            if (department == null || department.getEmployeesAvailable() < employeesRequiredCount) {
                return false;
            }

            totalEmployeesAllocated += allocateEmployees(department, employeesRequiredCount, projectId,manager,architect);
            updateDepartmentDetails(department, employeesRequiredCount);
        }

        projectService.updateProjectDetails(projectId, totalEmployeesAllocated);
        return true;
    }

    private int allocateEmployees(DepartmentDetails department, int employeesRequiredCount, String projectId,String manager,String architect) {
        List<User> employees;
        if ("Manager".equals(department.getDepartmentName())){
            employees = userRepository.findByFullName(manager);
        }
        else if ("Solution_Architect".equals(department.getDepartmentName())){
            employees = userRepository.findByFullName(architect);

        }
        else {
            employees = userRepository.findByDepartmentAndProjectAssignedFalse(department.getDepartmentId());
        }

        Collections.shuffle(employees);

        int assignedEmployeesCount = 0;

        if (employeesRequiredCount != 0) {
            for (User employee : employees) {
                if (!employee.isProjectAssigned()) {
                    employee.setProjectAssigned(true);
                    employee.setProjectId(projectId);
                    userRepository.save(employee);
                    assignedEmployeesCount++;

                    emailService.sendProjectEmail(employee);
                }

                if (assignedEmployeesCount == employeesRequiredCount) {
                    break;
                }
            }
        }

        return assignedEmployeesCount;
    }

    private void updateDepartmentDetails(DepartmentDetails department, int employeesRequiredCount) {
        int newEmployeesAvailable = department.getEmployeesAvailable() - employeesRequiredCount;
        int newEmployeesAllocated = department.getEmployeesAllocated() + employeesRequiredCount;
        department.setEmployeesAvailable(newEmployeesAvailable);
        department.setEmployeesAllocated(newEmployeesAllocated);
        departmentRepository.save(department);
    }

    private DepartmentDto convertToDto(DepartmentDetails department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setEmployeesAvailable(department.getEmployeesAvailable());
        return dto;
    }

    public boolean departmentExists(String departmentName) {
        DepartmentDetails exist = departmentRepository.findByDepartmentName(departmentName);
            if (exist !=null)  return true;
        return false;
    }

    public Object findManagerByName(String manager) {
        DepartmentDetails departmentDetails = departmentRepository.findByDepartmentName("Manager");
        return departmentDetails.getDepartmentId();

    }

    public String getAllManagers(String manager) {
        DepartmentDetails department = departmentRepository.findByDepartmentName(manager);
        return department.getDepartmentId();
    }

    public String getAllArchitects(String architect) {
        DepartmentDetails department = departmentRepository.findByDepartmentName(architect);
        return department.getDepartmentId();
    }
}
