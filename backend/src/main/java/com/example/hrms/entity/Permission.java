package com.example.hrms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

// 权限实体 - 移除@Data注解，手动实现equals和hashCode避免懒加载问题
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    
    @Column(nullable = false, length = 100)
    private String resource;
    
    @Column(nullable = false, length = 50)
    private String action;
    
    @Column(length = 200)
    private String description;
    
    @Column(updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();
    
    // 拥有此权限的角色
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles;
    
    // 构造方法
    public Permission(String name, String resource, String action, String description) {
        this.name = name;
        this.resource = resource;
        this.action = action;
        this.description = description;
        this.createdTime = LocalDateTime.now();
    }
    
    // 重写equals和hashCode，只使用id字段，避免懒加载问题
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", resource='" + resource + '\'' +
                ", action='" + action + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}