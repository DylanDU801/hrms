package com.example.hrms.config;

import com.example.hrms.config.initializer.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 主数据初始化器
 * 按顺序执行各个模块的数据初始化
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1) // 确保在其他组件启动前执行
public class DataInitializer implements CommandLineRunner {

    private final PermissionInitializer permissionInitializer;
    private final RoleInitializer roleInitializer;
    private final DepartmentInitializer departmentInitializer;
    private final UserInitializer userInitializer;

    @Override
    public void run(String... args) throws Exception {
        log.info("=================================");
        log.info("开始执行系统数据初始化...");
        log.info("=================================");
        
        try {
            // 1. 首先初始化权限数据（基础数据）
            permissionInitializer.initPermissions();
            
            // 2. 然后初始化角色数据（依赖权限）
            roleInitializer.initRoles();
            
            // 3. 初始化部门数据（独立数据）
            departmentInitializer.initDepartments();
            
            // 4. 最后初始化用户数据（依赖角色和部门）
            userInitializer.initUsers();
            
            log.info("=================================");
            log.info("系统数据初始化完成！");
            log.info("=================================");
            
            printDefaultAccounts();
            
        } catch (Exception e) {
            log.error("数据初始化失败: ", e);
            throw e;
        }
    }
    
    /**
     * 打印默认账号信息
     */
    private void printDefaultAccounts() {
        log.info("");
        log.info("=============== 默认账号信息 ===============");
        log.info("系统管理员: admin / 123456");
        log.info("人事专员: hr001 / 123456");
        log.info("项目经理: pm001 / 123456");
        log.info("外包测试: tester001 / 123456");
        log.info("普通员工: emp001 / 123456");
        log.info("========================================");
        log.info("Swagger文档地址: http://localhost:8080/swagger-ui.html");
        log.info("========================================");
        log.info("");
    }
}