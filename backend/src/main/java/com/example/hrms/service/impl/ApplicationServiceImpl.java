package com.example.hrms.service.impl;

import com.example.hrms.config.UserContext;
import com.example.hrms.entity.Application;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.User;
import com.example.hrms.repository.ApplicationRepository;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Page<Application> getApplications(int page, int size, Application.ApplicationType type,
                                             Application.ApplicationStatus status, Long applicantId) {
        // 简单分页查询实现
        return applicationRepository.findByConditions(type, status, applicantId, PageRequest.of(page, size));
    }

    @Override
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    @Override
    public Application submitApplication(Application application, MultipartFile attachment) {
        User user = UserContext.getUser();
        Long id = user.getId();
        Employee employeeByUserId = employeeRepository.findEmployeeByUserId(id);
        application.setApplicant(employeeByUserId);
        return applicationRepository.save(application);
    }

    @Override
    public Application approveApplication(Long id, boolean approved, String reason) {
        return applicationRepository.findById(id)
                .map(application -> {
                    // 设置审批状态
                    application.setStatus(approved ?
                        Application.ApplicationStatus.APPROVED :
                        Application.ApplicationStatus.REJECTED);

                    // 记录审批信息
                    application.setApproveTime(LocalDateTime.now());
                    application.setApproveReason(reason);

                    // 设置审批人（从当前用户获取）
                    User user = UserContext.getUser();
                    Employee approver = employeeRepository.findEmployeeByUserId(user.getId());
                    application.setCurrentApprover(approver);

                    return applicationRepository.save(application);
                })
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
    }

    @Override
    public Application cancelApplication(Long id) {
        // TODO: 实现取消逻辑
        return null;
    }

    @Override
    public Application forwardApplication(Long id, Long newApproverId, String reason) {
        // TODO: 实现转交逻辑
        return null;
    }

    @Override
    public List<Application> getApplicationsForCurrentUser(Application.ApplicationStatus status) {
        // 获取当前用户ID
        User user = UserContext.getUser();
        Long userId = user.getId();
        
        // 通过用户ID查找对应的员工
        Employee employee = employeeRepository.findEmployeeByUserId(userId);
        if (employee == null) {
            return Collections.emptyList();
        }
        
        // 根据状态查询该员工的所有申请
        return applicationRepository.findByConditions(
            null,  // 不限制申请类型
            status,  // 使用传入的状态过滤
            employee.getId(),  // 当前员工的ID
            PageRequest.of(0, Integer.MAX_VALUE)  // 获取所有记录
        ).getContent();
    }

    @Override
    public List<Application> getPendingApprovalsForCurrentUser() {
        // 获取当前用户ID
        User user = UserContext.getUser();
        Long userId = user.getId();
        
        // 通过用户ID查找对应的员工
        Employee approver = employeeRepository.findEmployeeByUserId(userId);
        if (approver == null) {
            return Collections.emptyList();
        }
        
        // 查询当前审批人待处理的申请
        return applicationRepository.findByConditions(
            null,  // 不限制申请类型
            Application.ApplicationStatus.PENDING,  // 只查询待审批状态
            null,  // 不限制申请人
            PageRequest.of(0, Integer.MAX_VALUE)  // 获取所有记录
        ).getContent();
    }

    @Override
    public Object getApplicationStatistics(String startDate, String endDate, Application.ApplicationType type) {
        Map<String, Long> stats = new HashMap<>();

        // 统计总申请数
        stats.put("total", applicationRepository.countByConditions(type, null, null));

        // 统计待审批数量
        stats.put("pending", applicationRepository.countByConditions(
            type, Application.ApplicationStatus.PENDING, null));

        // 统计已批准数量
        stats.put("approved", applicationRepository.countByConditions(
            type, Application.ApplicationStatus.APPROVED, null));

        // 统计已拒绝数量
        stats.put("rejected", applicationRepository.countByConditions(
            type, Application.ApplicationStatus.REJECTED, null));

        return stats;
    }

    @Override
    public List<Application> batchApproveApplications(List<Long> applicationIds, boolean approved, String reason) {
        // TODO: 实现批量审批
        return null;
    }

    @Override
    public String getAttachmentDownloadUrl(Long id) {
        // TODO: 实现附件下载链接
        return null;
    }

    @Override
    public boolean isOwner(Long applicationId) {
        // TODO: 实现判断当前用户是否是申请单所有人
        return false;
    }
}
