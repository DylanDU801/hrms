package com.example.hrms.controller;

import com.example.hrms.dto.Result;
import com.example.hrms.entity.Application;
import com.example.hrms.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "申请审批管理", description = "员工申请的提交、审批和流程管理")
@RequiredArgsConstructor
public class ApplicationController {
    
    private final ApplicationService applicationService;
    
    @Operation(summary = "获取申请列表", description = "支持分页和条件查询")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER')")
    public Result<Page<Application>> getApplications(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "申请类型") @RequestParam(required = false) Application.ApplicationType type,
            @Parameter(description = "申请状态") @RequestParam(required = false) Application.ApplicationStatus status,
            @Parameter(description = "申请人ID") @RequestParam(required = false) Long applicantId) {
        
        Page<Application> applications = applicationService.getApplications(page, size, type, status, applicantId);
        return Result.success(applications);
    }
    
    @Operation(summary = "获取申请详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER') or @applicationService.isOwner(#id)")
    public Result<Application> getApplication(@PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        return Result.success(application);
    }
    
    @Operation(summary = "提交申请", description = "员工提交新的申请")
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public Result<Application> submitApplication(
            @Valid @RequestBody Application application,
            @Parameter(description = "附件文件") @RequestParam(required = false) MultipartFile attachment) {
        Application createdApplication = applicationService.submitApplication(application, attachment);
        return Result.success(createdApplication);
    }
    
    @Operation(summary = "审批申请", description = "审批人处理申请")
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER')")
    public Result<Application> approveApplication(
            @PathVariable Long id,
            @Parameter(description = "是否批准") @RequestParam boolean approved,
            @Parameter(description = "审批意见") @RequestParam(required = false) String reason) {
        Application application = applicationService.approveApplication(id, approved, reason);
        return Result.success(application);
    }
    
    @Operation(summary = "撤回申请", description = "申请人撤回自己的申请")
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @applicationService.isOwner(#id)")
    public Result<Application> cancelApplication(@PathVariable Long id) {
        Application application = applicationService.cancelApplication(id);
        return Result.success(application);
    }
    
    @Operation(summary = "转发申请", description = "将申请转发给其他审批人")
    @PutMapping("/{id}/forward")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER')")
    public Result<Application> forwardApplication(
            @PathVariable Long id,
            @Parameter(description = "新审批人ID") @RequestParam Long newApproverId,
            @Parameter(description = "转发原因") @RequestParam(required = false) String reason) {
        Application application = applicationService.forwardApplication(id, newApproverId, reason);
        return Result.success(application);
    }
    
    @Operation(summary = "获取我的申请", description = "获取当前用户提交的申请")
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public Result<List<Application>> getMyApplications(
            @Parameter(description = "申请状态") @RequestParam(required = false) Application.ApplicationStatus status) {
        List<Application> applications = applicationService.getApplicationsForCurrentUser(status);
        return Result.success(applications);
    }
    
    @Operation(summary = "获取待审批申请", description = "获取当前用户需要审批的申请")
    @GetMapping("/pending-approvals")
    @PreAuthorize("hasRole('HR') or hasRole('MANAGER') or hasRole('ADMIN')")
    public Result<List<Application>> getPendingApprovals() {
        List<Application> applications = applicationService.getPendingApprovalsForCurrentUser();
        return Result.success(applications);
    }
    
    @Operation(summary = "获取申请统计", description = "获取申请的统计数据")
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER')")
    public Result<?> getApplicationStatistics(
            @Parameter(description = "统计开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "统计结束日期") @RequestParam(required = false) String endDate,
            @Parameter(description = "申请类型") @RequestParam(required = false) Application.ApplicationType type) {
        Object statistics = applicationService.getApplicationStatistics(startDate, endDate, type);
        return Result.success(statistics);
    }
    
    @Operation(summary = "批量审批", description = "批量处理多个申请")
    @PutMapping("/batch-approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER')")
    public Result<List<Application>> batchApproveApplications(
            @Parameter(description = "申请ID列表") @RequestParam List<Long> applicationIds,
            @Parameter(description = "是否批准") @RequestParam boolean approved,
            @Parameter(description = "审批意见") @RequestParam(required = false) String reason) {
        List<Application> applications = applicationService.batchApproveApplications(applicationIds, approved, reason);
        return Result.success(applications);
    }
    
    @Operation(summary = "下载附件", description = "下载申请的附件文件")
    @GetMapping("/{id}/attachment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('MANAGER') or @applicationService.isOwner(#id)")
    public Result<String> downloadAttachment(@PathVariable Long id) {
        String downloadUrl = applicationService.getAttachmentDownloadUrl(id);
        return Result.success(downloadUrl);
    }
}