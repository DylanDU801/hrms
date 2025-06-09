package com.example.hrms.controller;

import com.example.hrms.annotation.OperationLog;
import com.example.hrms.entity.User;
import com.example.hrms.repository.UserRepository;
import com.example.hrms.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @OperationLog("登录")
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginInfo) {
        String username = loginInfo.get("username");
        String password = loginInfo.get("password");

//        log.info("=== 登录调试开始 ===");
//        log.info("登录请求 - 用户名: {}", username);
//        log.info("登录请求 - 密码: {}", password);

        // 从数据库查询用户
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (!userOpt.isPresent()) {
            log.error("❌ 用户不存在: {}", username);
            Map<String, Object> res = new HashMap<>();
            res.put("code", 60204);
            res.put("message", "用户不存在");
            return res;
        }

        User user = userOpt.get();
//        log.info("✅ 找到用户: {}", user.getUsername());
//        log.info("用户ID: {}", user.getId());
//        log.info("用户启用状态: {}", user.getEnabled());
//        log.info("账户未过期: {}", user.getAccountNonExpired());
//        log.info("账户未锁定: {}", user.getAccountNonLocked());
//        log.info("凭据未过期: {}", user.getCredentialsNonExpired());
//        log.info("数据库中的密码哈希: {}", user.getPassword());

        // 检查用户是否启用
        if (!user.getEnabled()) {
            log.error("❌ 用户已禁用: {}", username);
            Map<String, Object> res = new HashMap<>();
            res.put("code", 60204);
            res.put("message", "用户已禁用");
            return res;
        }

        // 验证密码
//        log.info("开始验证密码...");
//        log.info("输入密码: [{}]", password);
//        log.info("存储哈希: [{}]", user.getPassword());

        boolean passwordMatch;
        try {
            passwordMatch = passwordEncoder.matches(password, user.getPassword());
//            log.info("密码验证结果: {}", passwordMatch);
        } catch (Exception e) {
//            log.error("❌ 密码验证过程出错: ", e);
            Map<String, Object> res = new HashMap<>();
            res.put("code", 50000);
            res.put("message", "密码验证失败");
            return res;
        }

        if (passwordMatch) {
//            log.info("✅ 密码验证通过");
            try {
                String token = jwtTokenUtil.generateToken(user.getUsername());

                Map<String, Object> data = new HashMap<>();
                data.put("token", token);

                Map<String, Object> res = new HashMap<>();
                res.put("code", 20000);
                res.put("data", data);

//                log.info("✅ 用户 {} 登录成功，生成Token: {}...", username, token.substring(0, Math.min(token.length(), 30)));
//                log.info("=== 登录调试结束 ===");
                return res;
            } catch (Exception e) {
//                log.error("❌ 生成Token失败", e);
                Map<String, Object> res = new HashMap<>();
                res.put("code", 50000);
                res.put("message", "登录失败：生成Token错误");
                return res;
            }
        } else {
//            log.error("❌ 密码验证失败");
//            log.error("可能的原因:");
//            log.error("1. 输入的密码不正确");
//            log.error("2. 数据库中的哈希值有问题");
//            log.error("3. PasswordEncoder配置有问题");

            // 额外调试：尝试生成新的哈希并验证
            try {
                String newHash = passwordEncoder.encode(password);
//                log.info("当前输入密码的新哈希: {}", newHash);
                boolean newHashMatches = passwordEncoder.matches(password, newHash);
//                log.info("新哈希是否匹配: {}", newHashMatches);
            } catch (Exception e) {
//                log.error("生成新哈希时出错: ", e);
            }

            Map<String, Object> res = new HashMap<>();
            res.put("code", 60204);
            res.put("message", "用户名或密码错误");
//            log.info("=== 登录调试结束 ===");
            return res;
        }
    }

    @GetMapping("/info")
    public Map<String, Object> info(@RequestParam String token) {
//        log.info("获取用户信息请求，Token: {}...", token.substring(0, Math.min(token.length(), 20)));

        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Optional<User> userOpt = userRepository.findByUsername(username);

            if (userOpt.isPresent() && jwtTokenUtil.validateToken(token, username)) {
                User user = userOpt.get();

                Map<String, Object> data = new HashMap<>();
                data.put("roles", new String[]{"admin"});
                data.put("introduction", "用户简介");
                data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
                data.put("name", user.getUsername());

                Map<String, Object> res = new HashMap<>();
                res.put("code", 20000);
                res.put("data", data);

//                log.info("返回用户信息: {}", username);
                return res;
            }
        } catch (Exception e) {
//            log.error("解析Token错误", e);
        }

//        log.warn("Token无效或用户不存在");
        Map<String, Object> res = new HashMap<>();
        res.put("code", 50008);
        res.put("message", "Token无效");
        return res;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
//        log.info("用户退出登录");
        Map<String, Object> res = new HashMap<>();
        res.put("code", 20000);
        res.put("data", "success");
        return res;
    }

    /**
     * 密码测试接口 - 仅用于调试
     * GET /user/test-password?username=admin&password=123456
     */
    @GetMapping("/test-password")
    public Map<String, Object> testPassword(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();

//        log.info("=== 密码测试开始 ===");

        try {
            // 1. 查找用户
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (!userOpt.isPresent()) {
                result.put("error", "用户不存在");
                return result;
            }

            User user = userOpt.get();

            // 2. 显示详细信息
            result.put("username", username);
            result.put("inputPassword", password);
            result.put("storedHash", user.getPassword());
            result.put("userEnabled", user.getEnabled());

            // 3. 测试密码匹配
            boolean matches = passwordEncoder.matches(password, user.getPassword());
            result.put("passwordMatches", matches);

            // 4. 生成新的密码哈希用于对比
            String newHash = passwordEncoder.encode(password);
            result.put("newHash", newHash);
            boolean newMatches = passwordEncoder.matches(password, newHash);
            result.put("newHashMatches", newMatches);

            // 5. 测试特定哈希是否匹配123456
            String knownGoodHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH";
            boolean knownHashMatches = passwordEncoder.matches("123456", knownGoodHash);
            result.put("knownHashMatches123456", knownHashMatches);

//            log.info("密码测试结果: 用户={}, 密码匹配={}", username, matches);
//            log.info("=== 密码测试结束 ===");

        } catch (Exception e) {
//            log.error("密码测试出错", e);
            result.put("error", e.getMessage());
        }

        return result;
    }
}
