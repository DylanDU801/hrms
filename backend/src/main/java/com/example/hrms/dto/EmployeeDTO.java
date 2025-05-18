package com.example.hrms.dto;

public class EmployeeDTO {
    private Long id;
    private String name;
    private String email;
    private String position;
    private String departmentName;

    // 构造方法
    public EmployeeDTO() {}

    public EmployeeDTO(Long id, String name, String email, String position, String departmentName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.position = position;
        this.departmentName = departmentName;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}
