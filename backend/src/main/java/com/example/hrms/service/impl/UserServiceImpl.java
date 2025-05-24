package com.example.hrms.service.impl;

import com.example.hrms.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public boolean isCurrentUser(Long userId) {
        return false;
    }

    // ... 其他现有方法的空实现，全部return null或false即可
}
