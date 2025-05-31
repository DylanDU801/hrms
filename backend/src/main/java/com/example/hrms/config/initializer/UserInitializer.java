package com.example.hrms.config.initializer;

import com.example.hrms.entity.*;
import com.example.hrms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInitializer {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional // 添加事务注解
    public void initUsers() {
        if (userRepository.count() > 0) {
            log.info("用户数据已存在，跳过初始化");
            return;
        }
        
        log.info("开始初始化用户数据...");
        
        // 创建系统管理员
        createAdminUser();
        
        // 创建HR用户
        createHrUser();
        
        // 创建项目经理用户
        createProjectManagerUser();
        
        // 创建测试人员用户
        createTesterUser();
        
        // 创建普通员工用户
        createEmployeeUser();
        
        log.info("用户数据初始化完成，共创建 {} 个用户", userRepository.count());
    }
    
    private void createAdminUser() {
        // 创建管理员用户
        User admin = createUser("admin", "123456", "admin@hrms.com");
        
        // 分配管理员角色
        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
        if (adminRole != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            admin = userRepository.save(admin);
        }
        
        // 创建对应的员工记录
        Employee adminEmployee = createEmployee(
            admin, "系统管理员", "admin@hrms.com", "系统管理员", 
            "人事部", "EMP001", Employee.EmployeeType.FULL_TIME
        );
        
        log.info("创建系统管理员用户: admin");
    }
    
    private void createHrUser() {
        User hrUser = createUser("hr001", "123456", "hr001@hrms.com");
        
        Role hrRole = roleRepository.findByName("HR").orElse(null);
        if (hrRole != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(hrRole);
            hrUser.setRoles(roles);
            hrUser = userRepository.save(hrUser);
        }
        
        Employee hrEmployee = createEmployee(
            hrUser, "李小慧", "hr001@hrms.com", "人事专员",
            "人事部", "EMP002", Employee.EmployeeType.FULL_TIME
        );
        
        log.info("创建HR用户: hr001");
    }
    
    private void createProjectManagerUser() {
        User pmUser = createUser("pm001", "123456", "pm001@hrms.com");
        
        Role pmRole = roleRepository.findByName("PROJECT_MANAGER").orElse(null);
        if (pmRole != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(pmRole);
            pmUser.setRoles(roles);
            pmUser = userRepository.save(pmUser);
        }
        
        Employee pmEmployee = createEmployee(
            pmUser, "王项目", "pm001@hrms.com", "项目经理",
            "研发部", "EMP003", Employee.EmployeeType.FULL_TIME
        );
        
        log.info("创建项目经理用户: pm001");
    }
    
    private void createTesterUser() {
        User testerUser = createUser("tester001", "123456", "tester001@hrms.com");
        
        Role testerRole = roleRepository.findByName("TESTER").orElse(null);
        if (testerRole != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(testerRole);
            testerUser.setRoles(roles);
            testerUser = userRepository.save(testerUser);
        }
        
        Employee testerEmployee = createEmployee(
            testerUser, "张测试", "tester001@hrms.com", "外包测试工程师",
            "测试部", "EMP004", Employee.EmployeeType.CONTRACTOR
        );
        
        log.info("创建测试人员用户: tester001");
    }
    
    private void createEmployeeUser() {
        User empUser = createUser("emp001", "123456", "emp001@hrms.com");
        
        Role empRole = roleRepository.findByName("EMPLOYEE").orElse(null);
        if (empRole != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(empRole);
            empUser.setRoles(roles);
            empUser = userRepository.save(empUser);
        }
        
        Employee employee = createEmployee(
            empUser, "刘普通", "emp001@hrms.com", "软件工程师",
            "研发部", "EMP005", Employee.EmployeeType.FULL_TIME
        );
        
        log.info("创建普通员工用户: emp001");
    }
    
    private User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        return userRepository.save(user);
    }
    
    private Employee createEmployee(User user, String name, String email, String position, 
                                  String departmentName, String employeeNumber, Employee.EmployeeType type) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        employee.setPosition(position);
        employee.setEmployeeType(type);
        employee.setStatus(Employee.EmployeeStatus.ACTIVE);
        employee.setHireDate(LocalDate.now());
        employee.setEmployeeNumber(employeeNumber);
        employee.setPhone("138****" + (1000 + (int)(Math.random() * 9000))); // 随机生成电话
        employee.setUser(user);
        
        // 设置部门
        Department department = departmentRepository.findAll().stream()
            .filter(d -> departmentName.equals(d.getName()))
            .findFirst().orElse(null);
        if (department != null) {
            employee.setDepartment(department);
        }
        
        return employeeRepository.save(employee);
    }
}