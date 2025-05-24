package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 外包测试DTO
@Data
public class OutsourcingTestDTO {
    private Long id;
    private String testName;
    private String description;
    private Long testerId;
    private String testerName;
    private Long creatorId;
    private String creatorName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String priority;
    private Integer estimatedHours;
    private Integer actualHours;
    private Integer score;
    private String result;
    private LocalDateTime createdTime;
}
