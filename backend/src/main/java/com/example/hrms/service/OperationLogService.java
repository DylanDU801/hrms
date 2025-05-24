package com.example.hrms.service;

import com.example.hrms.entity.OperationLog;
import org.springframework.data.domain.Page;
import java.util.List;

// 操作日志服务接口
public interface OperationLogService {
    
    void logOperation(String operation, String method, Object params, Object result, 
                     String ipAddress, String userAgent, long executionTime);
    
    Page<OperationLog> getOperationLogs(int page, int size, Long userId, String operation,
                                       String startDate, String endDate, String ipAddress,
                                       OperationLog.OperationStatus status);
    
    OperationLog getOperationLogById(Long id);
    
    Object getUserOperationStatistics(Long userId, int days);
    
    Object getOperationTypeStatistics(String startDate, String endDate);
    
    List<OperationLog> getAnomalousOperations(int days, String anomalyType);
    
    Page<OperationLog> getLoginLogs(int page, int size, Long userId, String startDate, String endDate);
    
    String exportOperationLogs(String startDate, String endDate, Long userId, String operation);
    
    int cleanupOldLogs(int retentionDays);
    
    List<OperationLog> getSecurityEvents(int days, String severity);
}