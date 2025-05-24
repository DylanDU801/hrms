package com.example.hrms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ApplicationType applicationType;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    // 申请人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private Employee applicant;
    
    // 当前审批人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_approver_id")
    private Employee currentApprover;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.PENDING;
    
    // 申请时间
    @Column(nullable = false, updatable = false)
    private LocalDateTime applyTime = LocalDateTime.now();
    
    // 审批时间
    private LocalDateTime approveTime;
    
    // 审批意见
    @Column(length = 500)
    private String approveReason;
    
    // 紧急程度
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;
    
    // 附件路径
    private String attachmentPath;
    
    private LocalDateTime updatedTime = LocalDateTime.now();
    
    @PreUpdate
    private void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
    
    // 枚举定义
    public enum ApplicationType {
        LEAVE("请假申请"),
        OVERTIME("加班申请"), 
        TRANSFER("调岗申请"),
        RESIGNATION("离职申请"),
        EQUIPMENT("设备申请"),
        REIMBURSEMENT("报销申请"),
        OTHER("其他申请");
        
        private final String description;
        
        ApplicationType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum ApplicationStatus {
        PENDING("待审批"),
        IN_REVIEW("审批中"),
        APPROVED("已批准"),
        REJECTED("已拒绝"),
        CANCELLED("已撤销");
        
        private final String description;
        
        ApplicationStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum Priority {
        LOW("低"),
        NORMAL("普通"),
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