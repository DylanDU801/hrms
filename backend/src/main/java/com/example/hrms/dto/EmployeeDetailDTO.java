package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 员工详情DTO  
@Data
public class EmployeeDetailDTO {
    private Long id;
    private String name;
    private String email;
    private String position;
    private String employeeType;
    private String status;
    private String phone;
    private String employeeNumber;
    private java.time.LocalDate hireDate;
    private Long departmentId;
    private String departmentName;
    private Long managerId;
    private String managerName;
    private Long userId;
    private String username;
    private LocalDateTime createdTime;
}