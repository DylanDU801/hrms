package com.example.hrms.controller;

import com.example.hrms.entity.User;
import com.example.hrms.repository.UserRepository;
import com.example.hrms.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginInfo) {
        String username = loginInfo.get("username");
        String password = loginInfo.get("password");

        // 从数据库查询用户
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            String token = jwtTokenUtil.generateToken(user.getUsername());
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            
            Map<String, Object> res = new HashMap<>();
            res.put("code", 20000);
            res.put("data", data);
            return res;
        } else {
            // 登录失败，返回自定义错误码和信息
            Map<String, Object> res = new HashMap<>();
            res.put("code", 60204); // vue-element-admin默认的登录失败码
            res.put("message", "用户名或密码错误");
            return res;
        }
    }

    @GetMapping("/info")
    public Map<String, Object> info(@RequestParam String token) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Optional<User> userOpt = userRepository.findByUsername(username);
            
            if (userOpt.isPresent() && jwtTokenUtil.validateToken(token, username)) {
                User user = userOpt.get();
                
                Map<String, Object> data = new HashMap<>();
                data.put("roles", new String[]{"admin"});  // 这里可以从数据库中获取用户角色
                data.put("introduction", "用户简介");
                data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
                data.put("name", user.getUsername());
                
                Map<String, Object> res = new HashMap<>();
                res.put("code", 20000);
                res.put("data", data);
                return res;
            }
        } catch (Exception e) {
            // Token解析错误
        }

        Map<String, Object> res = new HashMap<>();
        res.put("code", 50008);
        res.put("message", "Token无效");
        return res;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 20000);
        res.put("data", "success");
        return res;
    }
}