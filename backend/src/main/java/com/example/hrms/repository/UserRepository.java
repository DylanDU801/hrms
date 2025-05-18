package com.example.hrms.repository;

import com.example.hrms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 可以自定义方法，如 User findByUsername(String username);
}