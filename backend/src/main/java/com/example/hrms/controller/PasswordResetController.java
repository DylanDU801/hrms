package com.example.hrms.controller;

import com.example.hrms.entity.User;
import com.example.hrms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@Slf4j
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 重置所有用户密码为123456
     * GET /admin/reset-all-passwords
     */
    @GetMapping("/reset-all-passwords")
    public Map<String, Object> resetAllPasswords() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取所有用户
            Iterable<User> users = userRepository.findAll();
            int resetCount = 0;
            
            for (User user : users) {
                String oldHash = user.getPassword();
                String newHash = passwordEncoder.encode("123456");
                
                user.setPassword(newHash);
                userRepository.save(user);
                
                log.info("重置用户 {} 的密码", user.getUsername());
                log.info("旧哈希: {}", oldHash);
                log.info("新哈希: {}", newHash);
                
                resetCount++;
            }
            
            result.put("success", true);
            result.put("message", "所有用户密码已重置为123456");
            result.put("resetCount", resetCount);
            
            log.info("成功重置 {} 个用户的密码", resetCount);
            
        } catch (Exception e) {
            log.error("重置密码失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 重置单个用户密码
     * GET /admin/reset-password?username=admin&password=123456
     */
    @GetMapping("/reset-password")
    public Map<String, Object> resetUserPassword(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (!userOpt.isPresent()) {
                result.put("error", "用户不存在");
                return result;
            }
            
            User user = userOpt.get();
            String oldHash = user.getPassword();
            String newHash = passwordEncoder.encode(password);
            
            user.setPassword(newHash);
            userRepository.save(user);
            
            result.put("success", true);
            result.put("username", username);
            result.put("oldHash", oldHash);
            result.put("newHash", newHash);
            result.put("message", "密码重置成功");
            
            // 验证新密码
            boolean verification = passwordEncoder.matches(password, newHash);
            result.put("verification", verification);
            
            log.info("用户 {} 密码重置成功，验证结果: {}", username, verification);
            
        } catch (Exception e) {
            log.error("重置密码失败", e);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 生成密码哈希
     * GET /admin/generate-hash?password=123456
     */
    @GetMapping("/generate-hash")
    public Map<String, Object> generatePasswordHash(@RequestParam String password) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String hash = passwordEncoder.encode(password);
            boolean verification = passwordEncoder.matches(password, hash);
            
            result.put("password", password);
            result.put("hash", hash);
            result.put("verification", verification);
            result.put("encoderClass", passwordEncoder.getClass().getSimpleName());
            
        } catch (Exception e) {
            log.error("生成哈希失败", e);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}
