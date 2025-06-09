package com.example.hrms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

// 更新的用户实体 - 支持RBAC，移除@Data注解
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private Boolean enabled = true;

    // 账户是否未过期
    @Column(nullable = false)
    private Boolean accountNonExpired = true;

    // 账户是否未锁定
    @Column(nullable = false)
    private Boolean accountNonLocked = true;

    // 密码是否未过期
    @Column(nullable = false)
    private Boolean credentialsNonExpired = true;

    // 最后登录时间
    private LocalDateTime lastLoginTime;

    // 最后登录IP
    private String lastLoginIp;

    @Column(updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    private LocalDateTime updatedTime = LocalDateTime.now();

    // 用户拥有的角色
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @PreUpdate
    private void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }

    // 重写equals和hashCode，只使用id字段
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
