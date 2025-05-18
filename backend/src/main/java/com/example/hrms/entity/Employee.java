package com.example.hrms.entity;

import jakarta.persistence.*;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 员工姓名
    private String email;
    private String position;

    // 员工属于一个部门
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // Getter and Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}
