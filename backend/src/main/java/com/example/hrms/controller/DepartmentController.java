package com.example.hrms.controller;

import com.example.hrms.dto.Result;
import com.example.hrms.entity.Department;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.repository.DepartmentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public Result<List<Department>> list() {
        return Result.success(departmentRepository.findAll());
    }

    @GetMapping("/{id}")
    public Result<Department> get(@PathVariable Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("部门不存在，ID: " + id));
        return Result.success(department);
    }

    @PostMapping
    public Result<Department> create(@RequestBody Department department) {
        return Result.success(departmentRepository.save(department));
    }

    @PutMapping("/{id}")
    public Result<Department> update(@PathVariable Long id, @RequestBody Department newDept) {
        Department department = departmentRepository.findById(id)
                .map(dept -> {
                    dept.setName(newDept.getName());
                    return departmentRepository.save(dept);
                })
                .orElseThrow(() -> new ResourceNotFoundException("部门不存在，ID: " + id));
        
        return Result.success(department);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("部门不存在，ID: " + id);
        }
        departmentRepository.deleteById(id);
        return Result.success(null);
    }
}