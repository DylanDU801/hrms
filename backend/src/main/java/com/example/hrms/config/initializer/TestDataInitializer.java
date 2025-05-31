package com.example.hrms.config.initializer;

import com.example.hrms.entity.*;
import com.example.hrms.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 测试数据初始化器
 * 只在开发环境下执行，用于创建一些示例数据
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.init.test-data", havingValue = "true", matchIfMissing = false)
public class TestDataInitializer {
    
    private final OutsourcingTestRepository outsourcingTestRepository;
    private final ApplicationRepository applicationRepository;
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    
    public void initTestData() {
        log.info("开始初始化测试数据...");
        
        // 创建一些外包测试任务
        createOutsourcingTests();
        
        // 创建一些申请记录
        createApplications();
        
        // 创建一些薪资记录
        createSalaries();
        
        log.info("测试数据初始化完成");
    }
    
    private void createOutsourcingTests() {
        if (outsourcingTestRepository.count() > 0) {
            return;
        }
        
        List<Employee> employees = employeeRepository.findAll();
        if (employees.size() < 2) {
            return;
        }
        
        Employee creator = employees.get(0); // 假设第一个是创建者
        Employee tester = employees.get(1);  // 假设第二个是测试者
        
        // 创建测试任务1
        OutsourcingTest test1 = new OutsourcingTest();
        test1.setTestName("用户登录功能测试");
        test1.setDescription("测试用户登录、注册、密码重置等核心功能");
        test1.setCreator(creator);
        test1.setTester(tester);
        test1.setStatus(OutsourcingTest.TestStatus.IN_PROGRESS);
        test1.setPriority(OutsourcingTest.Priority.HIGH);
        test1.setEstimatedHours(16);
        test1.setStartTime(LocalDateTime.now().minusDays(3));
        test1.setEndTime(LocalDateTime.now().plusDays(2));
        outsourcingTestRepository.save(test1);
        
        // 创建测试任务2
        OutsourcingTest test2 = new OutsourcingTest();
        test2.setTestName("员工管理模块测试");
        test2.setDescription("测试员工增删改查、部门分配等功能");
        test2.setCreator(creator);
        test2.setTester(tester);
        test2.setStatus(OutsourcingTest.TestStatus.COMPLETED);
        test2.setPriority(OutsourcingTest.Priority.MEDIUM);
        test2.setEstimatedHours(24);
        test2.setActualHours(20);
        test2.setScore(85);
        test2.setResult("测试完成，发现3个小问题已修复");
        test2.setStartTime(LocalDateTime.now().minusDays(10));
        test2.setEndTime(LocalDateTime.now().minusDays(3));
        outsourcingTestRepository.save(test2);
        
        // 创建测试任务3
        OutsourcingTest test3 = new OutsourcingTest();
        test3.setTestName("薪资管理功能测试");
        test3.setDescription("测试薪资计算、发放记录、报表生成功能");
        test3.setCreator(creator);
        test3.setTester(tester);
        test3.setStatus(OutsourcingTest.TestStatus.PENDING);
        test3.setPriority(OutsourcingTest.Priority.MEDIUM);
        test3.setEstimatedHours(12);
        test3.setStartTime(LocalDateTime.now().plusDays(1));
        test3.setEndTime(LocalDateTime.now().plusDays(5));
        outsourcingTestRepository.save(test3);
        
        log.info("创建了 {} 个测试任务", 3);
    }
    
    private void createApplications() {
        if (applicationRepository.count() > 0) {
            return;
        }
        
        List<Employee> employees = employeeRepository.findAll();
        if (employees.size() < 2) {
            return;
        }
        
        Employee applicant = employees.get(1); // 申请人
        Employee approver = employees.get(0);  // 审批人
        
        // 创建请假申请
        Application leaveApp = new Application();
        leaveApp.setApplicationType(Application.ApplicationType.LEAVE);
        leaveApp.setTitle("年假申请");
        leaveApp.setContent("申请5月20日-5月24日年假，共5天。原因：家庭聚会。");
        leaveApp.setApplicant(applicant);
        leaveApp.setCurrentApprover(approver);
        leaveApp.setStatus(Application.ApplicationStatus.PENDING);
        leaveApp.setPriority(Application.Priority.NORMAL);
        leaveApp.setApplyTime(LocalDateTime.now().minusDays(1));
        applicationRepository.save(leaveApp);
        
        // 创建加班申请
        Application overtimeApp = new Application();
        overtimeApp.setApplicationType(Application.ApplicationType.OVERTIME);
        overtimeApp.setTitle("项目加班申请");
        overtimeApp.setContent("因项目紧急需求，申请本周末加班完成测试任务。");
        overtimeApp.setApplicant(applicant);
        overtimeApp.setCurrentApprover(approver);
        overtimeApp.setStatus(Application.ApplicationStatus.APPROVED);
        overtimeApp.setPriority(Application.Priority.HIGH);
        overtimeApp.setApplyTime(LocalDateTime.now().minusDays(3));
        overtimeApp.setApproveTime(LocalDateTime.now().minusDays(2));
        overtimeApp.setApproveReason("同意加班申请，注意休息");
        applicationRepository.save(overtimeApp);
        
        // 创建设备申请
        Application equipmentApp = new Application();
        equipmentApp.setApplicationType(Application.ApplicationType.EQUIPMENT);
        equipmentApp.setTitle("笔记本电脑申请");
        equipmentApp.setContent("当前电脑配置较低，影响开发效率，申请更换高配置笔记本。");
        equipmentApp.setApplicant(applicant);
        equipmentApp.setCurrentApprover(approver);
        equipmentApp.setStatus(Application.ApplicationStatus.IN_REVIEW);
        equipmentApp.setPriority(Application.Priority.NORMAL);
        equipmentApp.setApplyTime(LocalDateTime.now().minusDays(5));
        applicationRepository.save(equipmentApp);
        
        log.info("创建了 {} 个申请记录", 3);
    }
    
    private void createSalaries() {
        if (salaryRepository.count() > 0) {
            return;
        }
        
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            return;
        }
        
        // 为每个员工创建最近3个月的薪资记录
        for (Employee employee : employees) {
            // 4月薪资
            Salary salary1 = new Salary();
            salary1.setEmployee(employee);
            salary1.setPayDate(LocalDate.of(2025, 4, 10));
            salary1.setAmount(getSalaryByPosition(employee.getPosition()));
            salary1.setRemark("2025年4月工资");
            salaryRepository.save(salary1);
            
            // 3月薪资
            Salary salary2 = new Salary();
            salary2.setEmployee(employee);
            salary2.setPayDate(LocalDate.of(2025, 3, 10));
            salary2.setAmount(getSalaryByPosition(employee.getPosition()));
            salary2.setRemark("2025年3月工资");
            salaryRepository.save(salary2);
            
            // 2月薪资
            Salary salary3 = new Salary();
            salary3.setEmployee(employee);
            salary3.setPayDate(LocalDate.of(2025, 2, 10));
            salary3.setAmount(getSalaryByPosition(employee.getPosition()));
            salary3.setRemark("2025年2月工资");
            salaryRepository.save(salary3);
        }
        
        log.info("为 {} 个员工创建了薪资记录", employees.size());
    }
    
    private BigDecimal getSalaryByPosition(String position) {
        // 根据职位设置不同的薪资
        if (position == null) return new BigDecimal("8000");
        
        switch (position) {
            case "系统管理员":
                return new BigDecimal("15000");
            case "项目经理":
                return new BigDecimal("18000");
            case "人事专员":
                return new BigDecimal("12000");
            case "软件工程师":
                return new BigDecimal("14000");
            case "外包测试工程师":
                return new BigDecimal("10000");
            default:
                return new BigDecimal("8000");
        }
    }
}