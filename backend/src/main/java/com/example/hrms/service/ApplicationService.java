package com.example.hrms.service;

import com.example.hrms.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

// 申请审批服务接口
public interface ApplicationService {
    
    Page<Application> getApplications(int page, int size, Application.ApplicationType type,
                                     Application.ApplicationStatus status, Long applicantId);
    
    Application getApplicationById(Long id);
    
    Application submitApplication(Application application, MultipartFile attachment);
    
    Application approveApplication(Long id, boolean approved, String reason);
    
    Application cancelApplication(Long id);
    
    Application forwardApplication(Long id, Long newApproverId, String reason);
    
    List<Application> getApplicationsForCurrentUser(Application.ApplicationStatus status);
    
    List<Application> getPendingApprovalsForCurrentUser();
    
    Object getApplicationStatistics(String startDate, String endDate, Application.ApplicationType type);
    
    List<Application> batchApproveApplications(List<Long> applicationIds, boolean approved, String reason);
    
    String getAttachmentDownloadUrl(Long id);
    
    boolean isOwner(Long applicationId);
}