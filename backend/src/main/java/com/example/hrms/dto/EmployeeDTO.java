package com.example.hrms.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String name;
    private String email;
    private String position;
    private String departmentName;
    private Long departmentId;
    private String username;
    private Long userId;

}
