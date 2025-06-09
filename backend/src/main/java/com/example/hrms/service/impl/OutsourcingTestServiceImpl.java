package com.example.hrms.service.impl;

import com.example.hrms.config.UserContext;
import com.example.hrms.dto.TesterPerformanceDTO;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.OutsourcingTest;
import com.example.hrms.entity.User;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.OutsourcingTestRepository;
import com.example.hrms.service.OutsourcingTestService;
import com.example.hrms.util.JwtTokenUtil;
import com.example.hrms.dto.TestStatistics;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Security;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OutsourcingTestServiceImpl implements OutsourcingTestService {

    @Autowired
    private OutsourcingTestRepository outsourcingTestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Page<OutsourcingTest> getTests(int page, int size, String keyword, OutsourcingTest.TestStatus status, Long testerId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return outsourcingTestRepository.findByConditions(keyword, status, testerId, pageRequest);
    }

    @Override
    public OutsourcingTest getTestById(Long id) {
        return outsourcingTestRepository.findById(id).orElse(null);
    }

    @Override
    public OutsourcingTest createTest(OutsourcingTest test) {
        Employee employee = new Employee();
        employee.setId(test.getTester().getId());
        test.setCreator(employee);
        test.setCreatedTime(LocalDateTime.now());
        test.setStatus(OutsourcingTest.TestStatus.IN_PROGRESS);
        return outsourcingTestRepository.save(test);
    }

    @Override
    public OutsourcingTest assignTest(Long testId, Long testerId) {
        OutsourcingTest test = outsourcingTestRepository.findById(testId).orElse(null);
        if (test != null) {
            test.setStatus(OutsourcingTest.TestStatus.IN_PROGRESS);
            // 这里需要注入EmployeeRepository来获取tester对象
            // test.setTester(employeeRepository.findById(testerId).orElse(null));
            return outsourcingTestRepository.save(test);
        }
        return null;
    }

    @Override
    public OutsourcingTest updateTest(Long id, OutsourcingTest test) {
        return outsourcingTestRepository.findById(id)
                .map(existingTest -> {
                    existingTest.setTestName(test.getTestName());
                    existingTest.setDescription(test.getDescription());
                    existingTest.setPriority(test.getPriority());
                    existingTest.setEstimatedHours(test.getEstimatedHours());
                    existingTest.setStartTime(test.getStartTime());
                    existingTest.setEndTime(test.getEndTime());
                    // 新增tester字段更新
                    if (test.getTester() != null) {
                        existingTest.setTester(test.getTester());
                    }
                    return outsourcingTestRepository.save(existingTest);
                })
                .orElse(null);
    }

    @Override
    public OutsourcingTest submitTestResult(Long id, String result, Integer actualHours) {
        return outsourcingTestRepository.findById(id)
                .map(test -> {
                    test.setResult(result);
                    test.setActualHours(actualHours);
                    test.setStatus(OutsourcingTest.TestStatus.SUBMITTED);
                    return outsourcingTestRepository.save(test);
                })
                .orElse(null);
    }

    @Override
    public OutsourcingTest reviewTest(Long id, Integer score, String comment) {
        return outsourcingTestRepository.findById(id)
                .map(test -> {
                    test.setScore(score);
                    test.setResult(comment != null ? test.getResult() + "\n审核意见: " + comment : test.getResult());
                    test.setStatus(OutsourcingTest.TestStatus.REVIEWED);
                    return outsourcingTestRepository.save(test);
                })
                .orElse(null);
    }

    @Override
    public void deleteTest(Long id) {
        outsourcingTestRepository.deleteById(id);
    }

    @Override
    public List<OutsourcingTest> getTestsForCurrentUser(OutsourcingTest.TestStatus status) {
        User user = UserContext.getUser();
        Long id = user.getId();
        Employee employeeByUserId = employeeRepository.findEmployeeByUserId(id);
        if(status!=null){
            return outsourcingTestRepository.findByTesterIdAndStatus(employeeByUserId.getId(),status);
        }
        return outsourcingTestRepository.findByTesterId(employeeByUserId.getId());
    }

    @Override
    public List<OutsourcingTest> getTestsById(Long id) {
        return outsourcingTestRepository.findByTesterId(id);
    }

    @Override
    public TestStatistics getTestStatistics() {
        User user = UserContext.getUser();
        Long id = user.getId();
        Employee employeeByUserId = employeeRepository.findEmployeeByUserId(id);
        List<OutsourcingTest> allTests = outsourcingTestRepository.findByTesterId(employeeByUserId.getId());

        // 总任务数
        long totalTasks = allTests.size();

        // 进行中数量（状态为IN_PROGRESS）
        long inProgressCount = allTests.stream()
            .filter(test -> OutsourcingTest.TestStatus.IN_PROGRESS.equals(test.getStatus()))
            .count();

        // 已完成数量（状态为REVIEWED）
        long completedCount = allTests.stream()
            .filter(test -> OutsourcingTest.TestStatus.REVIEWED.equals(test.getStatus()))
            .count();

        // 平均分数（仅计算已审核任务）
        double averageScore = allTests.stream()
            .filter(test -> OutsourcingTest.TestStatus.REVIEWED.equals(test.getStatus()) && test.getScore() != null)
            .mapToInt(OutsourcingTest::getScore)
            .average()
            .orElse(0.0);
        DecimalFormat df = new DecimalFormat("#.00");
        return new TestStatistics(totalTasks, inProgressCount, completedCount, df.format(averageScore));
    }

    @Override
    public List<TesterPerformanceDTO> getTesterPerformance(Long testerId, String month) {
        LocalDateTime startDate = LocalDateTime.parse(month + "-01T00:00:00");
        List<Object[]> rawData = outsourcingTestRepository.getTesterPerformance(testerId, startDate);

        return rawData.stream().map(data -> {
            TesterPerformanceDTO dto = new TesterPerformanceDTO();
            dto.setEmployeeId((Long) data[0]);

            // 检查 data[1] 是否为 null
            if (data[1] != null) {
                dto.setEmployeeName((String) data[1]);
            } else {
                dto.setEmployeeName("");  // 设置默认值，例如空字符串
            }

            // 检查 data[2] 是否为 null
            if (data[2] != null) {
                dto.setDepartment((String) data[2]);
            } else {
                dto.setDepartment("");  // 设置默认值，例如空字符串
            }

            // 检查 data[3] 是否为 null
            if (data[3] != null) {
                dto.setTotalTasks(((Number) data[3]).intValue());
            } else {
                dto.setTotalTasks(0);  // 设置默认值，例如 0
            }

            // 检查 data[4] 是否为 null
            if (data[4] != null) {
                dto.setCompletedTasks(((Number) data[4]).intValue());
            } else {
                dto.setCompletedTasks(0);  // 设置默认值，例如 0
            }

            // 计算完成率（已完成/总任务数）
            double completionRate = dto.getTotalTasks() == 0 ? 0 :
                    (double) dto.getCompletedTasks() / dto.getTotalTasks() * 100;
            dto.setCompletionRate(Math.round(completionRate * 100) / 100.0);  // 保留两位小数

            // 检查 data[5] 是否为 null
            if (data[5] != null) {

                DecimalFormat df = new DecimalFormat("#.00");
                String format = df.format(((Number) data[5]).doubleValue());

                dto.setAverageScore(format);
            } else {
                dto.setAverageScore("0.00");  // 设置默认值，例如 0.0
            }

            // 检查 data[6] 是否为 null
            if (data[6] != null) {
                dto.setTotalHours(((Number) data[6]).intValue());
            } else {
                dto.setTotalHours(0);  // 设置默认值，例如 0
            }



            // 等级根据平均得分划分（示例逻辑）
            int i = new BigDecimal(dto.getAverageScore()).intValue();
            if ( i >= 90) dto.setGrade("A");
            else if ( i >= 80) dto.setGrade("B");
            else if ( i >= 70) dto.setGrade("C");
            else dto.setGrade("D");

            return dto;
        }).collect(Collectors.toList());
    }
}
