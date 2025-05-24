package com.example.hrms.repository;

import com.example.hrms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // 根据角色名查找
    Optional<Role> findByName(String name);
    
    // 查找启用的角色
    @Query("SELECT r FROM Role r WHERE r.enabled = true")
    Set<Role> findEnabledRoles();
    
    // 根据用户ID查找角色
    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId")
    Set<Role> findByUserId(@Param("userId") Long userId);
    
    // 检查角色名是否存在
    boolean existsByName(String name);
}