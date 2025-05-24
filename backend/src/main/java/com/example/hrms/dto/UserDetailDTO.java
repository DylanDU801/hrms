package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 用户详情DTO
@Data
public class UserDetailDTO {
    private Long id;
    private String username;
    private String email;
    private Boolean enabled;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private LocalDateTime createdTime;
    private java.util.List<RoleDTO> roles;
    private java.util.List<PermissionDTO> permissions;
}