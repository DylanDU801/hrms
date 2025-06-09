package com.example.hrms.controller;

import com.example.hrms.annotation.OperationLog;
import com.example.hrms.entity.Employee;
//import org.springframework.http.HttpStatus;
import com.example.hrms.entity.User;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.DepartmentRepository;
import com.example.hrms.dto.EmployeeDTO;

import com.example.hrms.repository.UserRepository;
import com.github.pagehelper.PageHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public EmployeeController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    // 新增：查询未绑定员工的用户列表
    @GetMapping("/unbound-users")
    @OperationLog("查询未绑定员工的用户列表")
    public List<User> getUnboundUsers(Long id) {
        // 获取所有用户
        List<User> allUsers = userRepository.findAll();
        // 获取已绑定员工的用户ID（假设员工表有 userId 字段）
        List<Employee> all = employeeRepository.findAll();

        List<Long> boundUserIds = all
                .stream()
                .map(employee -> {
                    User user = employee.getUser();
                    if(user!=null&&!employee.getId().equals(id)){
                        return user.getId();
                    }
                    return null;
                }) // 需根据实际字段名调整
                .toList();
        // 过滤出未绑定的用户

        List<User> list = allUsers.stream()
                .filter(user -> !boundUserIds.contains(user.getId()))
                .toList();
        for (User user : list) {
            user.setRoles(null);
        }

        return list;
    }

    // 1. 查询所有员工（返回DTO列表，不分页）
    @GetMapping
    @OperationLog("查询所有员工")
    public List<EmployeeDTO> list() {
        return employeeRepository.findAll().stream().map(emp -> {
            EmployeeDTO dto = new EmployeeDTO();
            dto.setId(emp.getId());
            dto.setName(emp.getName());
            dto.setEmail(emp.getEmail());
            dto.setPosition(emp.getPosition());
            if (emp.getDepartment() != null) {
                dto.setDepartmentName(emp.getDepartment().getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // 2. 分页查询员工（可选模糊搜索，返回DTO分页对象）
    @GetMapping("/page")
    @OperationLog("分页查询员工")
    public Page<EmployeeDTO> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword
    ) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Employee> employeePage;
        if (keyword == null || keyword.isEmpty()) {
            employeePage = employeeRepository.findAll(pageable);
        } else {
            employeePage = employeeRepository.findByNameContaining(keyword, pageable);
        }
        // 转为DTO分页
        return employeePage.map(emp -> {
            EmployeeDTO dto = new EmployeeDTO();
            dto.setId(emp.getId());
            dto.setName(emp.getName());
            dto.setEmail(emp.getEmail());
            dto.setPosition(emp.getPosition());
            if (emp.getDepartment() != null) {
                dto.setDepartmentName(emp.getDepartment().getName());
                dto.setDepartmentId(emp.getDepartment().getId());
            }
            if (emp.getUser() != null) {
                dto.setUsername(emp.getUser().getUsername());
                dto.setUserId(emp.getUser().getId());
            }
            return dto;
        });
    }

    // 3. 查询某部门下所有员工
    @GetMapping("/byDepartment/{deptId}")
    public List<EmployeeDTO> getByDepartment(@PathVariable Long deptId) {
        return employeeRepository.findByDepartmentId(deptId).stream().map(emp -> {
            EmployeeDTO dto = new EmployeeDTO();
            dto.setId(emp.getId());
            dto.setName(emp.getName());
            dto.setEmail(emp.getEmail());
            dto.setPosition(emp.getPosition());
            if (emp.getDepartment() != null) {
                dto.setDepartmentName(emp.getDepartment().getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // 4. 查询单个员工（返回DTO）
    @GetMapping("/{id}")
    @OperationLog("查询单个员工")
    public EmployeeDTO get(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(emp -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    dto.setId(emp.getId());
                    dto.setName(emp.getName());
                    dto.setEmail(emp.getEmail());
                    dto.setPosition(emp.getPosition());
                    if (emp.getDepartment() != null) {
                        dto.setDepartmentName(emp.getDepartment().getName());
                    }
                    return dto;
                })
                .orElse(null);
    }

    // 5. 新增员工（指定部门ID）
    @PostMapping
    @OperationLog("新增员工")
    public void create(@RequestBody Employee employee) {
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            departmentRepository.findById(employee.getDepartment().getId())
                    .ifPresent(employee::setDepartment);
        }
        employeeRepository.save(employee);
    }

    // 6. 修改员工信息
    @PutMapping("/{id}")
    @OperationLog("修改员工信息")
    public void update(@PathVariable Long id, @RequestBody Employee newEmp) {
        employeeRepository.findById(id)
                .map(emp -> {
                    emp.setName(newEmp.getName());
                    emp.setEmail(newEmp.getEmail());
                    emp.setPosition(newEmp.getPosition());
                    if (newEmp.getDepartment() != null && newEmp.getDepartment().getId() != null) {
                        departmentRepository.findById(newEmp.getDepartment().getId())
                                .ifPresent(emp::setDepartment);
                    }
                    if (newEmp.getUser() != null && newEmp.getUser().getId() != null) {
                        userRepository.findById(newEmp.getUser().getId())
                                .ifPresent(emp::setUser);
                    }
                    return employeeRepository.save(emp);
                }).orElse(null);
    }

    // 7. 删除员工
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }
}
