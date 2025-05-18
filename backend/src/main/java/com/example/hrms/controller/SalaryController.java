package com.example.hrms.controller;

import com.example.hrms.entity.Salary;
//import com.example.hrms.entity.Employee;
import com.example.hrms.repository.SalaryRepository;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.dto.SalaryDTO;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/salaries")
public class SalaryController {
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;

    public SalaryController(SalaryRepository salaryRepository, EmployeeRepository employeeRepository) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
    }

    // 查询所有薪资（返回DTO）
    @GetMapping
    public List<SalaryDTO> list() {
        return salaryRepository.findAll().stream().map(sal -> {
            SalaryDTO dto = new SalaryDTO();
            dto.setId(sal.getId());
            dto.setPayDate(sal.getPayDate());
            dto.setAmount(sal.getAmount());
            dto.setRemark(sal.getRemark());
            if (sal.getEmployee() != null) {
                dto.setEmployeeId(sal.getEmployee().getId());
                dto.setEmployeeName(sal.getEmployee().getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // 查询某员工的所有薪资
    @GetMapping("/employee/{employeeId}")
    public List<SalaryDTO> byEmployee(@PathVariable Long employeeId) {
        return salaryRepository.findByEmployeeId(employeeId).stream().map(sal -> {
            SalaryDTO dto = new SalaryDTO();
            dto.setId(sal.getId());
            dto.setPayDate(sal.getPayDate());
            dto.setAmount(sal.getAmount());
            dto.setRemark(sal.getRemark());
            if (sal.getEmployee() != null) {
                dto.setEmployeeId(sal.getEmployee().getId());
                dto.setEmployeeName(sal.getEmployee().getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // 新增薪资（必须指定员工id）
    @PostMapping
    public Salary create(@RequestBody Salary salary) {
        if (salary.getEmployee() != null && salary.getEmployee().getId() != null) {
            employeeRepository.findById(salary.getEmployee().getId())
                .ifPresent(salary::setEmployee);
        }
        return salaryRepository.save(salary);
    }

    // 修改薪资
    @PutMapping("/{id}")
    public Salary update(@PathVariable Long id, @RequestBody Salary newSalary) {
        return salaryRepository.findById(id)
            .map(salary -> {
                salary.setPayDate(newSalary.getPayDate());
                salary.setAmount(newSalary.getAmount());
                salary.setRemark(newSalary.getRemark());
                if (newSalary.getEmployee() != null && newSalary.getEmployee().getId() != null) {
                    employeeRepository.findById(newSalary.getEmployee().getId())
                        .ifPresent(salary::setEmployee);
                }
                return salaryRepository.save(salary);
            }).orElse(null);
    }

    // 删除薪资
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        salaryRepository.deleteById(id);
    }
}
