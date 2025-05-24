package com.example.hrms.service;

// 用户服务接口扩展
public interface UserService {
    
    boolean isCurrentUser(Long userId);
    
    // ... 其他现有方法
}