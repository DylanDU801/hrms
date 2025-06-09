package com.example.hrms.dto;

import lombok.Data;

@Data
public class TesterPerformanceDTO {
    private Long employeeId;  // 测试人员姓名
    private String employeeName;  // 测试人员姓名
    private String department;    // 部门
    private Integer totalTasks;   // 总任务数
    private Integer completedTasks; // 已完成任务数
    private Double completionRate; // 完成率（百分比）
    private String averageScore;  // 平均得分
    private Integer totalHours;   // 总工时
    private Double efficiency;    // 效率（百分比）
    private String grade;         // 等级
}
