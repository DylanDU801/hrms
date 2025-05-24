package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 仪表板数据DTO
@Data
public class DashboardDTO {
    private Long totalEmployees;
    private Long activeTests;
    private Long pendingApplications;
    private Long totalUsers;
    private java.util.List<StatisticsDTO> recentStatistics;
    private java.util.List<OperationLogDTO> recentLogs;
    private java.util.Map<String, Object> charts;
}