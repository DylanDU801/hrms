package com.example.hrms.repository;

import com.example.hrms.entity.Employee;
import com.example.hrms.entity.OutsourcingTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutsourcingTestRepository extends JpaRepository<OutsourcingTest, Long> {

    // 根据测试人员查找测试任务
    List<OutsourcingTest> findByTesterIdAndStatus(Long tester_id, OutsourcingTest.TestStatus status);

    List<OutsourcingTest> findByTesterId(Long id);

    // 根据创建人查找测试任务
    List<OutsourcingTest> findByCreatorId(Long creatorId);

    // 根据状态查找测试任务
    List<OutsourcingTest> findByStatus(OutsourcingTest.TestStatus status);

    // 复合条件查询
    @Query("SELECT t FROM OutsourcingTest t WHERE " +
           "(:keyword IS NULL OR t.testName LIKE %:keyword% OR t.description LIKE %:keyword%) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:testerId IS NULL OR t.tester.id = :testerId)")
    Page<OutsourcingTest> findByConditions(@Param("keyword") String keyword,
                                          @Param("status") OutsourcingTest.TestStatus status,
                                          @Param("testerId") Long testerId,
                                          Pageable pageable);

    // 获取指定时间范围内的测试统计
//    @Query("SELECT COUNT(t), AVG(t.score), t.status FROM OutsourcingTest t " +
//           "WHERE  t.tester.id = :testerId " +
//           "GROUP BY t.status")
//    List<Object[]> getTestStatistics(Long testerId);

    // 获取测试人员绩效统计
    @Query("SELECT t.tester.id, t.tester.name, t.tester.department.name, " +
           "COUNT(t), " +  // 总任务数
           "SUM(CASE WHEN t.status = 'REVIEWED' THEN 1 ELSE 0 END), " +  // 已完成任务数（假设REVIEWED为完成状态）
           "AVG(t.score), " +  // 平均得分
           "SUM(t.actualHours) " +  // 总工时
           "FROM OutsourcingTest t " +
           "WHERE (:testerId IS NULL OR t.tester.id = :testerId) " +
           "AND t.createdTime >= :startDate " +
           "GROUP BY t.tester.id, t.tester.name, t.tester.department.name")
    List<Object[]> getTesterPerformance(@Param("testerId") Long testerId,
                                       @Param("startDate") LocalDateTime startDate);

}
