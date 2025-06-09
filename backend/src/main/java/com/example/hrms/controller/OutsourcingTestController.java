package com.example.hrms.controller;

import com.example.hrms.dto.Result;
import com.example.hrms.dto.TestStatistics;
import com.example.hrms.dto.TesterPerformanceDTO;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.OutsourcingTest;
import com.example.hrms.service.OutsourcingTestService;
import com.example.hrms.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/outsourcing-tests")
@Tag(name = "外包测试管理", description = "外包测试任务的创建、分配、监控和评估")
@RequiredArgsConstructor
public class OutsourcingTestController {

    private final OutsourcingTestService outsourcingTestService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "获取测试任务列表", description = "支持分页和条件查询")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TESTER')")
    public Page<OutsourcingTest> getTests(
            @Parameter(description = "页码", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "页大小", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "测试名称关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态过滤") @RequestParam(required = false) OutsourcingTest.TestStatus status,
            @Parameter(description = "测试人员ID") @RequestParam(required = false) Long testerId) {
        page = page-1;
        Page<OutsourcingTest> tests = outsourcingTestService.getTests(page, size, keyword, status, testerId);
        List<OutsourcingTest> content = tests.getContent();
        return tests;
    }

    @Operation(summary = "获取测试任务详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TESTER')")
    public Result<OutsourcingTest> getTest(@PathVariable Long id) {
        OutsourcingTest test = outsourcingTestService.getTestById(id);
        return Result.success(test);
    }

    @Operation(summary = "创建测试任务", description = "项目经理创建新的测试任务")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public void createTest(@Valid @RequestBody OutsourcingTest test) {
        OutsourcingTest createdTest = outsourcingTestService.createTest(test);
    }

    @Operation(summary = "分配测试任务", description = "将测试任务分配给外包测试人员")
    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public Result<OutsourcingTest> assignTest(
            @PathVariable Long id,
            @Parameter(description = "测试人员ID") @RequestParam Long testerId) {
        OutsourcingTest test = outsourcingTestService.assignTest(id, testerId);
        return Result.success(test);
    }

    @Operation(summary = "更新测试任务")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public Result<OutsourcingTest> updateTest(@PathVariable Long id, @Valid @RequestBody OutsourcingTest test) {
        OutsourcingTest updatedTest = outsourcingTestService.updateTest(id, test);
        return Result.success(updatedTest);
    }

    @Operation(summary = "提交测试结果", description = "测试人员提交测试结果")
    @PutMapping("/{id}/submit")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public Result<OutsourcingTest> submitTestResult(
            @PathVariable Long id,
            @Parameter(description = "测试结果") @RequestParam String result,
            @Parameter(description = "实际工时") @RequestParam(required = false) Integer actualHours) {
        OutsourcingTest test = outsourcingTestService.submitTestResult(id, result, actualHours);
        return Result.success(test);
    }

    @Operation(summary = "审核测试结果", description = "项目经理审核测试结果并评分")
    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public Result<OutsourcingTest> reviewTest(
            @PathVariable Long id,
            @Parameter(description = "测试得分(0-100)") @RequestParam Integer score,
            @Parameter(description = "审核意见") @RequestParam(required = false) String comment) {
        OutsourcingTest test = outsourcingTestService.reviewTest(id, score, comment);
        return Result.success(test);
    }

    @Operation(summary = "删除测试任务")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public Result<Void> deleteTest(@PathVariable Long id) {
        outsourcingTestService.deleteTest(id);
        return Result.success(null);
    }

    @Operation(summary = "获取我的测试任务", description = "获取当前用户分配的测试任务")
    @GetMapping("/my-tests")
    @PreAuthorize("hasRole('TESTER')")
    public List<OutsourcingTest> getMyTests(OutsourcingTest.TestStatus status) {
        List<OutsourcingTest> tests = outsourcingTestService.getTestsForCurrentUser(status);
        return tests;
    }

    @Operation(summary = "获取测试任务根据id", description = "获取测试任务根据id")
    @GetMapping("/getTestsById")
    @PreAuthorize("hasRole('TESTER')")
    public List<OutsourcingTest> getTestsById(Long id) {
        List<OutsourcingTest> tests = outsourcingTestService.getTestsById(id);
        return tests;
    }

    @Operation(summary = "获取测试统计", description = "查询总任务数、进行中数量、已完成数量、平均分数")
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public Result<TestStatistics> getTestStatistics() {
        TestStatistics statistics = outsourcingTestService.getTestStatistics();
        return Result.success(statistics);
    }

    @Operation(summary = "获取测试人员绩效", description = "获取外包测试人员的绩效统计")
    @GetMapping("/performance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public Result<List<TesterPerformanceDTO>> getTesterPerformance(
            @Parameter(description = "测试人员ID") @RequestParam(required = false) Long testerId,
            @Parameter(description = "统计月份") @RequestParam(required = false) String month) {
        List<TesterPerformanceDTO> performance = outsourcingTestService.getTesterPerformance(testerId, month);
        return Result.success(performance);
    }
}
