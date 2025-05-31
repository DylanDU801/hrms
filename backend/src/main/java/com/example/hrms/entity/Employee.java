package com.example.hrms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

// 更新的员工实体 - 包含更多字段，移除@Data避免循环引用
@Entity
@Getter
@Setter
@NoArgsConstructor
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
    
    // 所属部门 - 明确指定外键列名
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
    
    // 对应的系统用户
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    // 直属上级
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
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
    
    // 重写equals和hashCode，只使用id字段
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                '}';
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