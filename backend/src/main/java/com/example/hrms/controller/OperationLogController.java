package com.example.hrms.controller;

import com.example.hrms.dto.Result;
import com.example.hrms.entity.OperationLog;
import com.example.hrms.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operation-logs")
@Tag(name = "操作日志管理", description = "系统操作日志的查询和分析")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('SECURITY_ADMIN')")
public class OperationLogController {
    
    private final OperationLogService operationLogService;
    
    @Operation(summary = "获取操作日志列表", description = "支持分页和条件查询")
    @GetMapping
    public Result<Page<OperationLog>> getOperationLogs(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "操作类型") @RequestParam(required = false) String operation,
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate,
            @Parameter(description = "IP地址") @RequestParam(required = false) String ipAddress,
            @Parameter(description = "操作状态") @RequestParam(required = false) OperationLog.OperationStatus status) {
        
        Page<OperationLog> logs = operationLogService.getOperationLogs(
            page, size, userId, operation, startDate, endDate, ipAddress, status);
        return Result.success(logs);
    }
    
    @Operation(summary = "获取操作日志详情")
    @GetMapping("/{id}")
    public Result<OperationLog> getOperationLog(@PathVariable Long id) {
        OperationLog log = operationLogService.getOperationLogById(id);
        return Result.success(log);
    }
    
    @Operation(summary = "获取用户操作统计")
    @GetMapping("/user-statistics")
    public Result<?> getUserOperationStatistics(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "30") int days) {
        Object statistics = operationLogService.getUserOperationStatistics(userId, days);
        return Result.success(statistics);
    }
    
    @Operation(summary = "获取操作类型统计")
    @GetMapping("/operation-statistics")
    public Result<?> getOperationTypeStatistics(
            @Parameter(description = "统计开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "统计结束日期") @RequestParam(required = false) String endDate) {
        Object statistics = operationLogService.getOperationTypeStatistics(startDate, endDate);
        return Result.success(statistics);
    }
    
    @Operation(summary = "获取异常操作记录")
    @GetMapping("/anomalies")
    public Result<List<OperationLog>> getAnomalousOperations(
            @Parameter(description = "检查天数") @RequestParam(defaultValue = "7") int days,
            @Parameter(description = "异常类型") @RequestParam(required = false) String anomalyType) {
        List<OperationLog> anomalies = operationLogService.getAnomalousOperations(days, anomalyType);
        return Result.success(anomalies);
    }
    
    @Operation(summary = "获取登录日志")
    @GetMapping("/logins")
    public Result<Page<OperationLog>> getLoginLogs(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate) {
        Page<OperationLog> loginLogs = operationLogService.getLoginLogs(page, size, userId, startDate, endDate);
        return Result.success(loginLogs);
    }
    
    @Operation(summary = "导出操作日志", description = "导出指定条件的操作日志")
    @GetMapping("/export")
    public Result<String> exportOperationLogs(
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "操作类型") @RequestParam(required = false) String operation) {
        String exportPath = operationLogService.exportOperationLogs(startDate, endDate, userId, operation);
        return Result.success(exportPath);
    }
    
    @Operation(summary = "清理历史日志", description = "清理指定天数之前的历史日志")
    @DeleteMapping("/cleanup")
    public Result<?> cleanupOldLogs(@Parameter(description = "保留天数") @RequestParam int retentionDays) {
        int deletedCount = operationLogService.cleanupOldLogs(retentionDays);
        return Result.success("已清理 " + deletedCount + " 条历史日志");
    }
    
    @Operation(summary = "获取安全事件", description = "获取可疑的安全相关操作")
    @GetMapping("/security-events")
    public Result<List<OperationLog>> getSecurityEvents(
            @Parameter(description = "检查天数") @RequestParam(defaultValue = "7") int days,
            @Parameter(description = "严重级别") @RequestParam(defaultValue = "HIGH") String severity) {
        List<OperationLog> securityEvents = operationLogService.getSecurityEvents(days, severity);
        return Result.success(securityEvents);
    }
}