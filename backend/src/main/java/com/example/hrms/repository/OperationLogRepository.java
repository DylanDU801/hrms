package com.example.hrms.repository;

import com.example.hrms.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    // 根据用户ID查找操作日志
    Page<OperationLog> findByUserIdOrderByCreatedTimeDesc(Long userId, Pageable pageable);

    // 根据操作类型查找
    Page<OperationLog> findByOperationContainingOrderByCreatedTimeDesc(String operation, Pageable pageable);

    // 根据IP地址查找
    List<OperationLog> findByIpAddressOrderByCreatedTimeDesc(String ipAddress);

    // 复合条件查询
    @Query("SELECT l FROM OperationLog l " +
           "ORDER BY l.createdTime DESC")
    Page<OperationLog> findByConditions(
                                       Pageable pageable);

    // 获取用户操作统计
    @Query("SELECT l.operation, COUNT(l) FROM OperationLog l " +
           "WHERE (:userId IS NULL OR l.user.id = :userId) " +
           "AND l.createdTime >= :startDate " +
           "GROUP BY l.operation ORDER BY COUNT(l) DESC")
    List<Object[]> getUserOperationStatistics(@Param("userId") Long userId,
                                              @Param("startDate") LocalDateTime startDate);

    // 获取操作类型统计
    @Query("SELECT l.operation, l.status, COUNT(l) FROM OperationLog l " +
           "WHERE l.createdTime BETWEEN :startDate AND :endDate " +
           "GROUP BY l.operation, l.status")
    List<Object[]> getOperationTypeStatistics(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    // 获取登录日志
    @Query("SELECT l FROM OperationLog l WHERE l.operation = 'LOGIN' " +
           "AND (:userId IS NULL OR l.user.id = :userId) " +
           "AND l.createdTime BETWEEN :startDate AND :endDate " +
           "ORDER BY l.createdTime DESC")
    Page<OperationLog> findLoginLogs(@Param("userId") Long userId,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    Pageable pageable);

    // 获取失败的操作
    @Query("SELECT l FROM OperationLog l WHERE l.status = 'FAILED' " +
           "AND l.createdTime >= :startDate " +
           "ORDER BY l.createdTime DESC")
    List<OperationLog> findFailedOperations(@Param("startDate") LocalDateTime startDate);

    // 获取异常操作（执行时间过长的操作）
    @Query("SELECT l FROM OperationLog l WHERE l.executionTime > :threshold " +
           "AND l.createdTime >= :startDate " +
           "ORDER BY l.executionTime DESC")
    List<OperationLog> findSlowOperations(@Param("threshold") long threshold,
                                         @Param("startDate") LocalDateTime startDate);

    // 清理历史日志
    @Modifying
    @Query("DELETE FROM OperationLog l WHERE l.createdTime < :cutoffDate")
    int deleteOldLogs(@Param("cutoffDate") LocalDateTime cutoffDate);

    // 获取同一IP的频繁操作（可能的安全威胁）
    @Query("SELECT l.ipAddress, COUNT(l) FROM OperationLog l " +
           "WHERE l.createdTime >= :startDate " +
           "GROUP BY l.ipAddress " +
           "HAVING COUNT(l) > :threshold " +
           "ORDER BY COUNT(l) DESC")
    List<Object[]> findFrequentOperationsByIp(@Param("startDate") LocalDateTime startDate,
                                              @Param("threshold") long threshold);


    @Modifying
    @Query("DELETE FROM OperationLog o WHERE o.createdTime < :cutoffDate")
    int deleteByCreateTimeBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
}
