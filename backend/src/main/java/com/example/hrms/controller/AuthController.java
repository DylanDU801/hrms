package com.example.hrms.controller;

//import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class AuthController {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginInfo) {
        String username = loginInfo.get("username");
        String password = loginInfo.get("password");

        // 这里只做演示，正式项目请用数据库校验
        if ("admin".equals(username) && "123456".equals(password)) {
            Map<String, Object> data = new HashMap<>();
            data.put("token", "admin-token");
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
        // 假定只有admin-token有效
        if ("admin-token".equals(token)) {
            Map<String, Object> data = new HashMap<>();
            data.put("roles", new String[]{"admin"});
            data.put("introduction", "I am the super administrator");
            data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            data.put("name", "Super Admin");
            Map<String, Object> res = new HashMap<>();
            res.put("code", 20000);
            res.put("data", data);
            return res;
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put("code", 50008);
            res.put("message", "Token无效");
            return res;
        }
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 20000);
        res.put("data", "success");
        return res;
    }
}
