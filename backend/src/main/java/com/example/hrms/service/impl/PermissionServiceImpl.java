package com.example.hrms.service.impl;

import com.example.hrms.entity.Permission;
import com.example.hrms.service.PermissionService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Override
    public List<Permission> getAllPermissions() {
        return null;
    }

    @Override
    public Permission getPermissionById(Long id) {
        return null;
    }

    @Override
    public Permission createPermission(Permission permission) {
        return null;
    }

    @Override
    public Permission updatePermission(Long id, Permission permission) {
        return null;
    }

    @Override
    public void deletePermission(Long id) {
        // nothing
    }

    @Override
    public Object getPermissionsGroupedByResource() {
        return null;
    }
}
