package com.example.hrms.service.impl;

import com.example.hrms.entity.Role;
import com.example.hrms.entity.Permission;
import com.example.hrms.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Override
    public List<Role> getAllRoles() {
        return null;
    }

    @Override
    public Role getRoleById(Long id) {
        return null;
    }

    @Override
    public Role createRole(Role role) {
        return null;
    }

    @Override
    public Role updateRole(Long id, Role role) {
        return null;
    }

    @Override
    public void deleteRole(Long id) {
        // nothing
    }

    @Override
    public Role assignPermissions(Long roleId, List<Long> permissionIds) {
        return null;
    }

    @Override
    public Role removePermission(Long roleId, Long permissionId) {
        return null;
    }

    @Override
    public Set<Permission> getRolePermissions(Long roleId) {
        return null;
    }

    @Override
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // nothing
    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        // nothing
    }

    @Override
    public Set<Role> getUserRoles(Long userId) {
        return null;
    }

    @Override
    public Set<Permission> getUserPermissions(Long userId) {
        return null;
    }
}
