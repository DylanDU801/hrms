package com.example.hrms.controller;

import com.example.hrms.entity.User;
import com.example.hrms.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 查询所有用户（支持按用户名关键字和状态筛选）
    @GetMapping
    public List<User> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean enabled) {
        if (keyword != null && enabled != null) {
            return userRepository.findByUsernameContainingAndEnabled(keyword, enabled);
        } else if (keyword != null) {
            return userRepository.findByUsernameContaining(keyword);
        } else if (enabled != null) {
            return userRepository.findByEnabled(enabled);
        }
        return userRepository.findAll();
    }

    // 查询单个用户（GET /users/{id}）
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 新增一个用户
    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 删除用户（DELETE /users/{id}）
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    // 修改用户信息（PUT /users/{id}）
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User newUser) {
        return userRepository.findById(id)
            .map(user -> {
                user.setUsername(newUser.getUsername());
                user.setPassword(newUser.getPassword());
                user.setEmail(newUser.getEmail());
                user.setEnabled(newUser.getEnabled());
                return userRepository.save(user);
            })
            .orElse(null);
    }

    @PutMapping("/toggle-status")
    public User updateUser( @RequestBody User newUser) {
        return userRepository.findById(newUser.getId())
                .map(user -> {
                    user.setEnabled(newUser.getEnabled());
                    return userRepository.save(user);
                })
                .orElse(null);
    }
}
