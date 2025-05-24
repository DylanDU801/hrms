package com.example.hrms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

// 更新的员工实体 - 包含更多字段
@Entity
@Data
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 50)
    private String position;
    
    // 员工类型
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EmployeeType employeeType = EmployeeType.FULL_TIME;
    
    // 所属部门
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    // 对应的系统用户
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    // 直属上级
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;
    
    // 入职日期
    private LocalDate hireDate;
    
    // 员工状态
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;
    
    // 手机号
    @Column(length = 20)
    private String phone;
    
    // 工号
    @Column(unique = true, length = 20)
    private String employeeNumber;
    
    @Column(updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();
    
    private LocalDateTime updatedTime = LocalDateTime.now();
    
    @PreUpdate
    private void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
    
    public enum EmployeeType {
        FULL_TIME("正式员工"),
        PART_TIME("兼职员工"), 
        CONTRACTOR("外包人员"),
        INTERN("实习生");
        
        private final String description;
        
        EmployeeType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum EmployeeStatus {
        ACTIVE("在职"),
        INACTIVE("离职"),
        SUSPENDED("停职");
        
        private final String description;
        
        EmployeeStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}