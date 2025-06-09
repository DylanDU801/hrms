package com.example.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 新增请求体类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRolesRequest {
    private Long userId;
    private List<Long> roleIds;
}
