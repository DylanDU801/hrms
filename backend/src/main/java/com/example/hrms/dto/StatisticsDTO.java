package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 统计数据DTO
@Data
public class StatisticsDTO {
    private String period;
    private Long totalCount;
    private Long successCount;
    private Long failCount;
    private Double successRate;
    private Double averageScore;
    private java.util.Map<String, Object> details;
}
