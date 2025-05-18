package com.example.hrms.controller;

import com.example.hrms.entity.Department;
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

    // 查询所有部门
    @GetMapping
    public List<Department> list() {
        return departmentRepository.findAll();
    }

    // 查询单个部门
    @GetMapping("/{id}")
    public Department get(@PathVariable Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    // 新增部门
    @PostMapping
    public Department create(@RequestBody Department department) {
        return departmentRepository.save(department);
    }

    // 修改部门
    @PutMapping("/{id}")
    public Department update(@PathVariable Long id, @RequestBody Department newDept) {
        return departmentRepository.findById(id)
                .map(dept -> {
                    dept.setName(newDept.getName());
                    return departmentRepository.save(dept);
                }).orElse(null);
    }

    // 删除部门
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        departmentRepository.deleteById(id);
    }
}
