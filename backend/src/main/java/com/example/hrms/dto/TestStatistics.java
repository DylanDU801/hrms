package com.example.hrms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "测试任务统计结果")
public class TestStatistics {
    @Schema(description = "总任务数")
    private Long totalTasks;

    @Schema(description = "进行中任务数")
    private Long inProgressCount;

    @Schema(description = "已完成任务数")
    private Long completedCount;

    @Schema(description = "平均分数（已审核任务）")
    private String averageScore;
}
