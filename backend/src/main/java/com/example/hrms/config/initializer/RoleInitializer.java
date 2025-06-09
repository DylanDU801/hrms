package com.example.hrms.config.initializer;

import com.example.hrms.entity.Permission;
import com.example.hrms.entity.Role;
import com.example.hrms.repository.PermissionRepository;
import com.example.hrms.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional // 添加事务注解
    public void initRoles() {
        if (roleRepository.count() > 0) {
//            log.info("角色数据已存在，跳过初始化");
            return;
        }

//        log.info("开始初始化角色数据...");

        // 1. 系统管理员角色 - 拥有所有权限
        createAdminRole();

        // 2. HR人事专员角色
        createHrRole();

        // 3. 项目经理角色
        createProjectManagerRole();

        // 4. 外包测试人员角色
        createTesterRole();

        // 5. 普通员工角色
        createEmployeeRole();

        // 6. 部门经理角色
        createDepartmentManagerRole();

//        log.info("角色数据初始化完成，共创建 {} 个角色", roleRepository.count());
    }

    private void createAdminRole() {
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole.setDescription("系统管理员 - 拥有所有权限");
        adminRole.setEnabled(true);

        // 先保存角色，再设置权限
        adminRole = roleRepository.save(adminRole);

        // 获取所有权限 - 重新查询避免懒加载问题
        List<Permission> allPermissions = permissionRepository.findAll();
        adminRole.setPermissions(new HashSet<>(allPermissions));
        roleRepository.save(adminRole);

//        log.info("创建系统管理员角色，分配 {} 个权限", allPermissions.size());
    }

    private void createHrRole() {
        Role hrRole = new Role();
        hrRole.setName("HR");
        hrRole.setDescription("人事专员 - 负责员工管理、薪资管理、申请审批");
        hrRole.setEnabled(true);

        // 先保存角色
        hrRole = roleRepository.save(hrRole);

        Set<Permission> hrPermissions = new HashSet<>();

        // 员工管理权限
        hrPermissions.addAll(getPermissionsByResource("员工"));

        // 部门管理权限
        hrPermissions.addAll(getPermissionsByResource("部门"));

        // 薪资管理权限
        hrPermissions.addAll(getPermissionsByResource("薪资"));

        // 申请审批权限
        hrPermissions.addAll(getPermissionsByResource("申请"));

        // 用户查看权限
        addPermissionByName(hrPermissions, "USER_READ");

        // 报表查看权限
        hrPermissions.addAll(getPermissionsByResource("报表"));

        hrRole.setPermissions(hrPermissions);
        roleRepository.save(hrRole);
//        log.info("创建HR角色，分配 {} 个权限", hrPermissions.size());
    }

    private void createProjectManagerRole() {
        Role pmRole = new Role();
        pmRole.setName("PROJECT_MANAGER");
        pmRole.setDescription("项目经理 - 负责外包测试管理、申请审批");
        pmRole.setEnabled(true);

        // 先保存角色
        pmRole = roleRepository.save(pmRole);

        Set<Permission> pmPermissions = new HashSet<>();

        // 测试管理权限
        pmPermissions.addAll(getPermissionsByResource("测试"));

        // 申请审批权限
        addPermissionByName(pmPermissions, "APPLICATION_READ");
        addPermissionByName(pmPermissions, "APPLICATION_APPROVE");
        addPermissionByName(pmPermissions, "APPLICATION_FORWARD");

        // 员工查看权限
        addPermissionByName(pmPermissions, "EMPLOYEE_READ");

        // 报表查看权限
        addPermissionByName(pmPermissions, "REPORT_VIEW");

        pmRole.setPermissions(pmPermissions);
        roleRepository.save(pmRole);
//        log.info("创建项目经理角色，分配 {} 个权限", pmPermissions.size());
    }

    private void createTesterRole() {
        Role testerRole = new Role();
        testerRole.setName("TESTER");
        testerRole.setDescription("外包测试人员 - 执行测试任务");
        testerRole.setEnabled(true);

        // 先保存角色
        testerRole = roleRepository.save(testerRole);

        Set<Permission> testerPermissions = new HashSet<>();

        // 测试执行权限
        addPermissionByName(testerPermissions, "TEST_READ");
        addPermissionByName(testerPermissions, "TEST_EXECUTE");

        // 申请权限
        addPermissionByName(testerPermissions, "APPLICATION_READ");
        addPermissionByName(testerPermissions, "APPLICATION_WRITE");

        testerRole.setPermissions(testerPermissions);
        roleRepository.save(testerRole);
//        log.info("创建测试人员角色，分配 {} 个权限", testerPermissions.size());
    }

    private void createEmployeeRole() {
        Role employeeRole = new Role();
        employeeRole.setName("EMPLOYEE");
        employeeRole.setDescription("普通员工 - 基础权限");
        employeeRole.setEnabled(true);

        // 先保存角色
        employeeRole = roleRepository.save(employeeRole);

        Set<Permission> empPermissions = new HashSet<>();

        // 申请相关权限
        addPermissionByName(empPermissions, "APPLICATION_READ");
        addPermissionByName(empPermissions, "APPLICATION_WRITE");

        // 查看自己的信息
        addPermissionByName(empPermissions, "EMPLOYEE_READ");

        employeeRole.setPermissions(empPermissions);
        roleRepository.save(employeeRole);
//        log.info("创建普通员工角色，分配 {} 个权限", empPermissions.size());
    }

    private void createDepartmentManagerRole() {
        Role deptManagerRole = new Role();
        deptManagerRole.setName("DEPT_MANAGER");
        deptManagerRole.setDescription("部门经理 - 管理本部门员工和审批申请");
        deptManagerRole.setEnabled(true);

        // 先保存角色
        deptManagerRole = roleRepository.save(deptManagerRole);

        Set<Permission> deptManagerPermissions = new HashSet<>();

        // 员工管理权限（限本部门）
        addPermissionByName(deptManagerPermissions, "EMPLOYEE_READ");
        addPermissionByName(deptManagerPermissions, "EMPLOYEE_WRITE");

        // 申请审批权限
        addPermissionByName(deptManagerPermissions, "APPLICATION_READ");
        addPermissionByName(deptManagerPermissions, "APPLICATION_APPROVE");
        addPermissionByName(deptManagerPermissions, "APPLICATION_FORWARD");

        // 部门查看权限
        addPermissionByName(deptManagerPermissions, "DEPT_READ");

        // 报表查看权限
        addPermissionByName(deptManagerPermissions, "REPORT_VIEW");

        deptManagerRole.setPermissions(deptManagerPermissions);
        roleRepository.save(deptManagerRole);
//        log.info("创建部门经理角色，分配 {} 个权限", deptManagerPermissions.size());
    }

    private List<Permission> getPermissionsByResource(String resource) {
        return permissionRepository.findByResource(resource);
    }

    private void addPermissionByName(Set<Permission> permissions, String permissionName) {
        permissionRepository.findByName(permissionName)
            .ifPresent(permissions::add);
    }
}
