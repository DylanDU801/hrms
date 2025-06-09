package com.example.hrms.repository;

import com.example.hrms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // 可按部门查找员工
    List<Employee> findByDepartmentId(Long departmentId);

    //条件查询
    Page<Employee> findByNameContaining(String keyword, Pageable pageable);

    Employee findEmployeeByUserId(Long id);
}
