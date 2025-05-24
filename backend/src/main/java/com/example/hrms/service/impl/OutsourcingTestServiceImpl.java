package com.example.hrms.service.impl;

import com.example.hrms.entity.OutsourcingTest;
import com.example.hrms.service.OutsourcingTestService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutsourcingTestServiceImpl implements OutsourcingTestService {

    @Override
    public Page<OutsourcingTest> getTests(int page, int size, String keyword, OutsourcingTest.TestStatus status, Long testerId) {
        return null;
    }

    @Override
    public OutsourcingTest getTestById(Long id) {
        return null;
    }

    @Override
    public OutsourcingTest createTest(OutsourcingTest test) {
        return null;
    }

    @Override
    public OutsourcingTest assignTest(Long testId, Long testerId) {
        return null;
    }

    @Override
    public OutsourcingTest updateTest(Long id, OutsourcingTest test) {
        return null;
    }

    @Override
    public OutsourcingTest submitTestResult(Long id, String result, Integer actualHours) {
        return null;
    }

    @Override
    public OutsourcingTest reviewTest(Long id, Integer score, String comment) {
        return null;
    }

    @Override
    public void deleteTest(Long id) {
        // nothing
    }

    @Override
    public List<OutsourcingTest> getTestsForCurrentUser() {
        return null;
    }

    @Override
    public Object getTestStatistics(String startDate, String endDate) {
        return null;
    }

    @Override
    public Object getTesterPerformance(Long testerId, String month) {
        return null;
    }
}
