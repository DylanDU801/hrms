package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 审批步骤DTO
@Data
public class ApprovalStepDTO {
    private Integer stepOrder;
    private String stepName;
    private String approverName;
    private String status;
    private LocalDateTime processTime;
    private String comment;
    private Boolean isCurrentStep;
}