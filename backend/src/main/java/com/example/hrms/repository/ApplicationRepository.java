package com.example.hrms.repository;

import com.example.hrms.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // 根据申请人查找申请
    List<Application> findByApplicantId(Long applicantId);

    // 根据当前审批人查找待审批申请
    List<Application> findByCurrentApproverIdAndStatus(Long approverId, Application.ApplicationStatus status);

    // 根据申请类型和状态查找
    List<Application> findByApplicationTypeAndStatus(Application.ApplicationType type, Application.ApplicationStatus status);

    // 复合条件查询
    @Query("SELECT a FROM Application a WHERE " +
           "(:type IS NULL OR a.applicationType = :type) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:applicantId IS NULL OR a.applicant.id = :applicantId)")
    Page<Application> findByConditions(@Param("type") Application.ApplicationType type,
                                      @Param("status") Application.ApplicationStatus status,
                                      @Param("applicantId") Long applicantId,
                                      Pageable pageable);

    // 获取申请统计数据
    @Query("SELECT a.applicationType, a.status, COUNT(a) FROM Application a " +
           "WHERE a.applyTime BETWEEN :startDate AND :endDate " +
           "GROUP BY a.applicationType, a.status")
    List<Object[]> getApplicationStatistics(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    // 获取待审批申请数量
    @Query("SELECT COUNT(a) FROM Application a WHERE a.currentApprover.id = :approverId AND a.status = 'PENDING'")
    long countPendingApplicationsByApprover(@Param("approverId") Long approverId);

    // 根据条件统计申请数量
    @Query("SELECT COUNT(a) FROM Application a WHERE " +
           "(:type IS NULL OR a.applicationType = :type) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:applicantId IS NULL OR a.applicant.id = :applicantId)")
    long countByConditions(@Param("type") Application.ApplicationType type,
                          @Param("status") Application.ApplicationStatus status,
                          @Param("applicantId") Long applicantId);

}
