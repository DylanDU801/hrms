package com.example.hrms.dto;

import lombok.Data;
import java.time.LocalDateTime;

// 权限DTO
@Data
public class PermissionDTO {
    private Long id;
    private String name;
    private String resource;
    private String action;
    private String description;
    private LocalDateTime createdTime;
}
