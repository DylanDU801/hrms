package com.example.hrms.controller;

import com.example.hrms.entity.Employee;
//import org.springframework.http.HttpStatus;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.DepartmentRepository;
import com.example.hrms.dto.EmployeeDTO;

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

    public EmployeeController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    // 1. 查询所有员工（返回DTO列表，不分页）
    @GetMapping
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
    public Page<EmployeeDTO> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
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
    public Employee create(@RequestBody Employee employee) {
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            departmentRepository.findById(employee.getDepartment().getId())
                    .ifPresent(employee::setDepartment);
        }
        return employeeRepository.save(employee);
    }

    // 6. 修改员工信息
    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody Employee newEmp) {
        return employeeRepository.findById(id)
                .map(emp -> {
                    emp.setName(newEmp.getName());
                    emp.setEmail(newEmp.getEmail());
                    emp.setPosition(newEmp.getPosition());
                    if (newEmp.getDepartment() != null && newEmp.getDepartment().getId() != null) {
                        departmentRepository.findById(newEmp.getDepartment().getId())
                                .ifPresent(emp::setDepartment);
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
