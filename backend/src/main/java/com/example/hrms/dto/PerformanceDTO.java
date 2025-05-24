package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;


// 绩效统计DTO
@Data
public class PerformanceDTO {
    private Long employeeId;
    private String employeeName;
    private String department;
    private Integer totalTasks;
    private Integer completedTasks;
    private Double averageScore;
    private Double completionRate;
    private Integer totalHours;
    private Double efficiency;
    private String grade;
}
