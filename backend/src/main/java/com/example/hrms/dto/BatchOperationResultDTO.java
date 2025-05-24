package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 批量操作结果DTO
@Data
public class BatchOperationResultDTO {
    private Integer total;
    private Integer success;
    private Integer failed;
    private java.util.List<String> successIds;
    private java.util.List<String> failedIds;
    private java.util.List<String> errors;
}
