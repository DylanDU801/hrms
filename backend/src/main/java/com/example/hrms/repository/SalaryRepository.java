package com.example.hrms.repository;

import com.example.hrms.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findByEmployeeId(Long employeeId); // 查某员工的所有薪资
}
