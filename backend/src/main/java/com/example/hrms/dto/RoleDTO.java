package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 角色DTO
@Data
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean enabled;
    private LocalDateTime createdTime;
    private java.util.List<PermissionDTO> permissions;
}