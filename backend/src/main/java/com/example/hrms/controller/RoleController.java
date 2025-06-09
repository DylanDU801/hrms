package com.example.hrms.controller;

import com.example.hrms.dto.AssignRolesRequest;
import com.example.hrms.dto.Result;
import com.example.hrms.entity.Role;
import com.example.hrms.entity.Permission;
import com.example.hrms.service.RoleService;
import com.example.hrms.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "角色管理", description = "系统角色的创建、编辑和权限分配")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "获取所有角色")
    @GetMapping
    public Result<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    @Operation(summary = "获取角色详情")
    @GetMapping("/{id}")
    public Result<Role> getRole(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    @Operation(summary = "创建角色")
    @PostMapping
    public Result<Role> createRole(@Valid @RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return Result.success(createdRole);
    }

    @Operation(summary = "更新角色")
    @PutMapping("/{id}")
    public Result<Role> updateRole(@PathVariable Long id, @Valid @RequestBody Role role) {
        Role updatedRole = roleService.updateRole(id, role);
        return Result.success(updatedRole);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success(null);
    }

    @Operation(summary = "为角色分配权限")
    @PostMapping("/{roleId}/permissions")
    public Result<Role> assignPermissions(
            @PathVariable Long roleId,
            @Parameter(description = "权限ID列表") @RequestBody List<Long> permissionIds) {
        Role role = roleService.assignPermissions(roleId, permissionIds);
        return Result.success(role);
    }

    @Operation(summary = "移除角色权限")
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public Result<Role> removePermission(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        Role role = roleService.removePermission(roleId, permissionId);
        return Result.success(role);
    }

    @Operation(summary = "获取角色的权限列表")
    @GetMapping("/{roleId}/permissions")
    public Result<Set<Permission>> getRolePermissions(@PathVariable Long roleId) {
        Set<Permission> permissions = roleService.getRolePermissions(roleId);
        return Result.success(permissions);
    }
}

@RestController
@RequestMapping("/api/permissions")
@Tag(name = "权限管理", description = "系统权限的管理")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "获取所有权限")
    @GetMapping
    public Result<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return Result.success(permissions);
    }

    @Operation(summary = "获取权限详情")
    @GetMapping("/{id}")
    public Result<Permission> getPermission(@PathVariable Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return Result.success(permission);
    }

    @Operation(summary = "创建权限")
    @PostMapping
    public Result<Permission> createPermission(@Valid @RequestBody Permission permission) {
        Permission createdPermission = permissionService.createPermission(permission);
        return Result.success(createdPermission);
    }

    @Operation(summary = "更新权限")
    @PutMapping("/{id}")
    public Result<Permission> updatePermission(@PathVariable Long id, @Valid @RequestBody Permission permission) {
        Permission updatedPermission = permissionService.updatePermission(id, permission);
        return Result.success(updatedPermission);
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/{id}")
    public Result<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return Result.success(null);
    }

    @Operation(summary = "按资源分组获取权限")
    @GetMapping("/by-resource")
    public Result<?> getPermissionsByResource() {
        Object groupedPermissions = permissionService.getPermissionsGroupedByResource();
        return Result.success(groupedPermissions);
    }
}

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户角色管理", description = "用户角色分配管理")
@RequiredArgsConstructor
class UserRoleController {

    private final RoleService roleService;

    @Operation(summary = "为用户分配角色")
    @PostMapping("/assign-roles")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public Result<?> assignRolesToUser(
            @RequestBody AssignRolesRequest request) {
        roleService.assignRolesToUser(request.getUserId(), request.getRoleIds());
        return Result.success("角色分配成功");
    }

    @Operation(summary = "移除用户角色")
    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public Result<?> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        roleService.removeRoleFromUser(userId, roleId);
        return Result.success("角色移除成功");
    }

    @Operation(summary = "获取用户的角色列表")
    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or @userService.isCurrentUser(#userId)")
    public Result<Set<Role>> getUserRoles(@PathVariable Long userId) {
        Set<Role> roles = roleService.getUserRoles(userId);
        return Result.success(roles);
    }

    @Operation(summary = "获取用户的权限列表")
    @GetMapping("/{userId}/permissions")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or @userService.isCurrentUser(#userId)")
    public Result<Set<Permission>> getUserPermissions(@PathVariable Long userId) {
        Set<Permission> permissions = roleService.getUserPermissions(userId);
        return Result.success(permissions);
    }
}
