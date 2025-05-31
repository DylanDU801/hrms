package com.example.hrms.config.initializer;

import com.example.hrms.entity.Department;
import com.example.hrms.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepartmentInitializer {
    
    private final DepartmentRepository departmentRepository;
    
    public void initDepartments() {
        if (departmentRepository.count() > 0) {
            log.info("部门数据已存在，跳过初始化");
            return;
        }
        
        log.info("开始初始化部门数据...");
        
        // 创建基础部门
        createDepartment("人事部", "负责人力资源管理、招聘、薪酬福利等工作");
        createDepartment("研发部", "负责产品研发、技术架构、系统开发等工作");
        createDepartment("测试部", "负责产品测试、质量保证、测试管理等工作");
        createDepartment("产品部", "负责产品规划、需求分析、产品设计等工作");
        createDepartment("运营部", "负责产品运营、市场推广、客户服务等工作");
        createDepartment("财务部", "负责财务管理、预算控制、成本分析等工作");
        createDepartment("行政部", "负责行政管理、办公设施、后勤保障等工作");
        createDepartment("销售部", "负责市场销售、客户关系、业务拓展等工作");
        
        log.info("部门数据初始化完成，共创建 {} 个部门", departmentRepository.count());
    }
    
    private void createDepartment(String name, String description) {
        Department department = new Department();
        department.setName(name);
        // 如果Department实体有description字段的话
        // department.setDescription(description);
        departmentRepository.save(department);
        log.debug("创建部门: {}", name);
    }
}