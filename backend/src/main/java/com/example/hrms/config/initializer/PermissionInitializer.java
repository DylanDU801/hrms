package com.example.hrms.config.initializer;

import com.example.hrms.entity.Permission;
import com.example.hrms.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionInitializer {

    private final PermissionRepository permissionRepository;

    public void initPermissions() {
        if (permissionRepository.count() > 0) {
//            log.info("权限数据已存在，跳过初始化");
            return;
        }

//        log.info("开始初始化权限数据...");

        // 用户管理权限
        createPermission("USER_READ", "用户", "查看", "查看用户信息");
        createPermission("USER_WRITE", "用户", "编辑", "编辑用户信息");
        createPermission("USER_DELETE", "用户", "删除", "删除用户");
        createPermission("USER_ASSIGN_ROLE", "用户", "分配角色", "为用户分配角色");

        // 员工管理权限
        createPermission("EMPLOYEE_READ", "员工", "查看", "查看员工信息");
        createPermission("EMPLOYEE_WRITE", "员工", "编辑", "编辑员工信息");
        createPermission("EMPLOYEE_DELETE", "员工", "删除", "删除员工");
        createPermission("EMPLOYEE_IMPORT", "员工", "导入", "批量导入员工");
        createPermission("EMPLOYEE_EXPORT", "员工", "导出", "导出员工数据");

        // 部门管理权限
        createPermission("DEPT_READ", "部门", "查看", "查看部门信息");
        createPermission("DEPT_WRITE", "部门", "编辑", "编辑部门信息");
        createPermission("DEPT_DELETE", "部门", "删除", "删除部门");

        // 外包测试权限
        createPermission("TEST_READ", "测试", "查看", "查看测试任务");
        createPermission("TEST_WRITE", "测试", "编辑", "创建编辑测试任务");
        createPermission("TEST_DELETE", "测试", "删除", "删除测试任务");
        createPermission("TEST_ASSIGN", "测试", "分配", "分配测试任务");
        createPermission("TEST_REVIEW", "测试", "审核", "审核测试结果");
        createPermission("TEST_EXECUTE", "测试", "执行", "执行测试任务");

        // 申请审批权限
        createPermission("APPLICATION_READ", "申请", "查看", "查看申请");
        createPermission("APPLICATION_WRITE", "申请", "编辑", "创建编辑申请");
        createPermission("APPLICATION_APPROVE", "申请", "审批", "审批申请");
        createPermission("APPLICATION_DELETE", "申请", "删除", "删除申请");
        createPermission("APPLICATION_FORWARD", "申请", "转发", "转发申请");

        // 薪资管理权限
        createPermission("SALARY_READ", "薪资", "查看", "查看薪资");
        createPermission("SALARY_WRITE", "薪资", "编辑", "管理薪资");
        createPermission("SALARY_DELETE", "薪资", "删除", "删除薪资记录");
        createPermission("SALARY_EXPORT", "薪资", "导出", "导出薪资数据");

        // 角色权限管理
        createPermission("ROLE_READ", "角色", "查看", "查看角色");
        createPermission("ROLE_WRITE", "角色", "编辑", "编辑角色");
        createPermission("ROLE_DELETE", "角色", "删除", "删除角色");
        createPermission("PERMISSION_READ", "权限", "查看", "查看权限");
        createPermission("PERMISSION_WRITE", "权限", "编辑", "编辑权限");

        // 操作日志权限
        createPermission("LOG_READ", "日志", "查看", "查看操作日志");
        createPermission("LOG_EXPORT", "日志", "导出", "导出日志");
        createPermission("LOG_DELETE", "日志", "删除", "清理历史日志");

        // 系统监控权限
        createPermission("SYSTEM_MONITOR", "系统", "监控", "系统监控");
        createPermission("SYSTEM_CONFIG", "系统", "配置", "系统配置");

        // 报表统计权限
        createPermission("REPORT_VIEW", "报表", "查看", "查看统计报表");
        createPermission("REPORT_EXPORT", "报表", "导出", "导出报表");

//        log.info("权限数据初始化完成，共创建 {} 个权限", permissionRepository.count());
    }

    private void createPermission(String name, String resource, String action, String description) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setResource(resource);
        permission.setAction(action);
        permission.setDescription(description);
        permissionRepository.save(permission);
        log.debug("创建权限: {}", name);
    }
}
