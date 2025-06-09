package com.example.hrms.repository;

import com.example.hrms.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // 根据权限名查找
    Optional<Permission> findByName(String name);

    // 根据资源查找权限
    List<Permission> findByResource(String resource);

    // 根据资源和操作查找权限
    Optional<Permission> findByResourceAndAction(String resource, String action);

//    // 根据角色ID查找权限
//    @Query("SELECT p FROM Permission p JOIN p.roles r WHERE r.id = :roleId")
//    Set<Permission> findByRoleId(@Param("roleId") Long roleId);
//
//    // 根据用户ID查找权限（通过角色）
//    @Query("SELECT DISTINCT p FROM Permission p JOIN p.roles r JOIN r.users u WHERE u.id = :userId")
//    Set<Permission> findByUserId(@Param("userId") Long userId);

    // 获取所有资源列表
    @Query("SELECT DISTINCT p.resource FROM Permission p ORDER BY p.resource")
    List<String> findAllResources();

    // 检查权限是否存在
    boolean existsByName(String name);
}
