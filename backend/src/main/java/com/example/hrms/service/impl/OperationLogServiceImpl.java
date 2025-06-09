package com.example.hrms.service.impl;

import com.example.hrms.config.UserContext;
import com.example.hrms.entity.OperationLog;
import com.example.hrms.entity.User;
import com.example.hrms.repository.OperationLogRepository;
import com.example.hrms.repository.UserRepository;
import com.example.hrms.service.OperationLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogRepository operationLogRepository;
    private final UserRepository userRepository;

    public OperationLogServiceImpl(OperationLogRepository operationLogRepository,
                                  UserRepository userRepository) {
        this.operationLogRepository = operationLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void logOperation(String operation, String method, Object params, Object result,
                           String ipAddress, String userAgent, long executionTime) {
        // 获取当前认证用户
        User user = UserContext.getUser();
        // 创建日志实体
        OperationLog log = new OperationLog();
        log.setUser(user);
        log.setOperation(operation);
        log.setMethod(method);
        log.setParams(params != null ? params.toString() : null);
        log.setResult(result != null ? result.toString() : null);
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        log.setExecutionTime(executionTime);

        // 设置操作状态
        if (result instanceof Throwable) {
            log.setStatus(OperationLog.OperationStatus.ERROR);
        } else {
            log.setStatus(OperationLog.OperationStatus.SUCCESS);
        }

        operationLogRepository.save(log);
    }

    @Override
    public Page<OperationLog> getOperationLogs(int page, int size) {

        // 调用Repository的分页查询方法
        return operationLogRepository.findByConditions(
            PageRequest.of(page, size)
        );
    }

    @Override
    public OperationLog getOperationLogById(Long id) {
        // TODO: 查询单条日志
        return null;
    }

    @Override
    public Object getUserOperationStatistics(Long userId, int days) {
        // TODO: 用户操作统计
        return null;
    }

    @Override
    public Object getOperationTypeStatistics(String startDate, String endDate) {
        // TODO: 操作类型统计
        return null;
    }

    @Override
    public List<OperationLog> getAnomalousOperations(int days, String anomalyType) {
        // TODO: 查询异常操作
        return null;
    }

    @Override
    public Page<OperationLog> getLoginLogs(int page, int size, Long userId, String startDate, String endDate) {
        // TODO: 登录日志分页
        return null;
    }

    @Override
    public String exportOperationLogs(String startDate, String endDate, Long userId, String operation) {
        // TODO: 导出日志
        return null;
    }

    @Override
    @Transactional
    public int cleanupOldLogs(int retentionDays) {
        // 计算截止日期
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);

        // 删除早于截止日期的日志
        return operationLogRepository.deleteByCreateTimeBefore(cutoffDate);
    }

    @Override
    public List<OperationLog> getSecurityEvents(int days, String severity) {
        // TODO: 查询安全事件
        return null;
    }
}
