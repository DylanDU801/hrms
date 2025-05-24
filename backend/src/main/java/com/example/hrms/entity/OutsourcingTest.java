package com.example.hrms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "outsourcing_tests")
public class OutsourcingTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String testName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    // 测试执行人员
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tester_id", nullable = false)
    private Employee tester;
    
    // 任务创建人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Employee creator;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TestStatus status = TestStatus.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String result;
    
    // 测试优先级
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;
    
    // 预计工时
    private Integer estimatedHours;
    
    // 实际工时
    private Integer actualHours;
    
    // 测试得分(0-100)
    private Integer score;
    
    @Column(updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();
    
    private LocalDateTime updatedTime = LocalDateTime.now();
    
    @PreUpdate
    private void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
    
    // 枚举定义
    public enum TestStatus {
        PENDING("待分配"),
        IN_PROGRESS("进行中"), 
        SUBMITTED("已提交"),
        REVIEWED("已审核"),
        COMPLETED("已完成"),
        CANCELLED("已取消");
        
        private final String description;
        
        TestStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum Priority {
        LOW("低"),
        MEDIUM("中"),
        HIGH("高"),
        URGENT("紧急");
        
        private final String description;
        
        Priority(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}