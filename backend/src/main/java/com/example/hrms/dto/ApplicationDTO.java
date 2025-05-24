package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 申请审批DTO
@Data
public class ApplicationDTO {
    private Long id;
    private String applicationType;
    private String title;
    private String content;
    private Long applicantId;
    private String applicantName;
    private Long currentApproverId;
    private String currentApproverName;
    private String status;
    private String priority;
    private LocalDateTime applyTime;
    private LocalDateTime approveTime;
    private String approveReason;
    private String attachmentPath;
}