package com.example.hrms.service;

import com.example.hrms.entity.Role;
import com.example.hrms.entity.Permission;
import java.util.List;
import java.util.Set;

// 角色服务接口
public interface RoleService {
    
    List<Role> getAllRoles();
    
    Role getRoleById(Long id);
    
    Role createRole(Role role);
    
    Role updateRole(Long id, Role role);
    
    void deleteRole(Long id);
    
    Role assignPermissions(Long roleId, List<Long> permissionIds);
    
    Role removePermission(Long roleId, Long permissionId);
    
    Set<Permission> getRolePermissions(Long roleId);
    
    void assignRolesToUser(Long userId, List<Long> roleIds);
    
    void removeRoleFromUser(Long userId, Long roleId);
    
    Set<Role> getUserRoles(Long userId);
    
    Set<Permission> getUserPermissions(Long userId);
}
