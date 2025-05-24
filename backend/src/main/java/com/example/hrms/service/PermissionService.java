package com.example.hrms.service;

import com.example.hrms.entity.Permission;
import java.util.List;

// 权限服务接口
public interface PermissionService {
    
    List<Permission> getAllPermissions();
    
    Permission getPermissionById(Long id);
    
    Permission createPermission(Permission permission);
    
    Permission updatePermission(Long id, Permission permission);
    
    void deletePermission(Long id);
    
    Object getPermissionsGroupedByResource();
}