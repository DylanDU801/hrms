package com.example.hrms.repository;

import com.example.hrms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);


    List<User> findByUsernameContaining(String keyword);
    List<User> findByEnabled(boolean enabled);
    List<User> findByUsernameContainingAndEnabled(String keyword, boolean enabled);
}
