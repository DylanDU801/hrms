package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 操作日志DTO
@Data
public class OperationLogDTO {
    private Long id;
    private Long userId;
    private String username;
    private String operation;
    private String method;
    private String params;
    private String result;
    private String ipAddress;
    private String userAgent;
    private String status;
    private Long executionTime;
    private LocalDateTime createdTime;
}