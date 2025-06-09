package com.example.hrms.service.impl;

import com.example.hrms.entity.Role;
import com.example.hrms.entity.Permission;
import com.example.hrms.entity.User;
import com.example.hrms.repository.PermissionRepository;
import com.example.hrms.repository.RoleRepository;
import com.example.hrms.repository.UserRepository;
import com.example.hrms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return null;
    }

    @Override
    public Role createRole(Role role) {
        // 检查角色名称是否已存在
        if (roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Role name already exists");
        }

        // 保存角色
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long id, Role role) {
        return roleRepository.findById(id)
                .map(existingRole -> {
                    // 检查角色名称是否已存在（排除自身）
                    if (!existingRole.getName().equals(role.getName()) &&
                        roleRepository.existsByName(role.getName())) {
                        throw new RuntimeException("Role name already exists");
                    }

                    // 更新角色信息
                    existingRole.setName(role.getName());
                    existingRole.setDescription(role.getDescription());
                    existingRole.setEnabled(role.getEnabled());

                    return roleRepository.save(existingRole);
                })
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Override
    public void deleteRole(Long id) {
        // nothing
        roleRepository.deleteById(id);
    }

    @Override
    public Role assignPermissions(Long roleId, List<Long> permissionIds) {
        // 获取角色
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        // 获取所有权限
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.getPermissions().clear();
        // 为角色添加权限
        role.getPermissions().addAll(permissions);

        // 保存并返回更新后的角色
        return roleRepository.save(role);
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
        // 获取用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 获取所有角色
        List<Role> roles = roleRepository.findAllById(roleIds);

        // 删除用户已有的所有角色
        user.getRoles().clear();

        // 为用户添加新的角色
        user.getRoles().addAll(roles);

        // 保存用户
        userRepository.save(user);

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
