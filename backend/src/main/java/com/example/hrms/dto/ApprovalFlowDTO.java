package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 审批流程DTO
@Data
public class ApprovalFlowDTO {
    private Long applicationId;
    private String applicationTitle;
    private String applicantName;
    private String currentStep;
    private java.util.List<ApprovalStepDTO> steps;
    private LocalDateTime submitTime;
    private String status;
    private Integer totalSteps;
    private Integer currentStepIndex;
}