package com.example.hrms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

// 操作日志实体
@Entity
@Data
@Table(name = "operation_logs")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 操作用户
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String operation;

    @Column(nullable = false, length = 20)
    private String method;

    @Column(columnDefinition = "TEXT")
    private String params;

    @Column(columnDefinition = "TEXT")
    private String result;

    @Column(length = 50)
    private String ipAddress;

    @Column(length = 200)
    private String userAgent;

    // 操作状态
    @Enumerated(EnumType.STRING)
    private OperationStatus status = OperationStatus.SUCCESS;

    // 执行时间(毫秒)
    private Long executionTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    public enum OperationStatus {
        SUCCESS("成功"),
        FAILED("失败"),
        ERROR("错误");

        private final String description;

        OperationStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
