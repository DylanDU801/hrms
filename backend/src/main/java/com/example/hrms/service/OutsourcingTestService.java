package com.example.hrms.service;

import com.example.hrms.entity.OutsourcingTest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

// 外包测试服务接口
public interface OutsourcingTestService {
    
    Page<OutsourcingTest> getTests(int page, int size, String keyword, 
                                  OutsourcingTest.TestStatus status, Long testerId);
    
    OutsourcingTest getTestById(Long id);
    
    OutsourcingTest createTest(OutsourcingTest test);
    
    OutsourcingTest assignTest(Long testId, Long testerId);
    
    OutsourcingTest updateTest(Long id, OutsourcingTest test);
    
    OutsourcingTest submitTestResult(Long id, String result, Integer actualHours);
    
    OutsourcingTest reviewTest(Long id, Integer score, String comment);
    
    void deleteTest(Long id);
    
    List<OutsourcingTest> getTestsForCurrentUser();
    
    Object getTestStatistics(String startDate, String endDate);
    
    Object getTesterPerformance(Long testerId, String month);
}
