package com.example.hrms.service.impl;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.hrms.entity.Permission;
import com.example.hrms.repository.PermissionRepository;
import com.example.hrms.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }

    @Override
    public Permission createPermission(Permission permission) {
        // 检查权限名称是否已存在
        if (permissionRepository.existsByName(permission.getName())) {
            throw new RuntimeException("Permission name already exists");
        }

        // 保存权限
        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(Long id, Permission permission) {
        return permissionRepository.findById(id)
                .map(existingPermission -> {
                    // 检查权限名称是否已存在（排除自身）
                    if (!existingPermission.getName().equals(permission.getName()) &&
                        permissionRepository.existsByName(permission.getName())) {
                        throw new RuntimeException("Permission name already exists");
                    }

                    // 更新权限信息
                    existingPermission.setName(permission.getName());
                    existingPermission.setDescription(permission.getDescription());
                    existingPermission.setResource(permission.getResource());
                    existingPermission.setAction(permission.getAction());

                    return permissionRepository.save(existingPermission);
                })
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }

    @Override
    public void deletePermission(Long id) {
        // 检查权限是否存在
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permission not found with id: " + id);
        }

        // 删除权限
        permissionRepository.deleteById(id);
    }

    @Override
    public Object getPermissionsGroupedByResource() {
        // 获取所有权限
        List<Permission> permissions = permissionRepository.findAll();

        // 按资源分组
        Map<String, List<Permission>> groupedPermissions = permissions.stream()
                .collect(Collectors.groupingBy(Permission::getResource));

        return groupedPermissions;
    }
}
