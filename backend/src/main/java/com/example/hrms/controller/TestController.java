package com.example.hrms.controller;

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/test")
@Tag(name = "测试接口", description = "用于验证Swagger是否正常工作")
public class TestController {
    
    @Operation(summary = "测试接口", description = "返回简单的测试信息")
    @GetMapping("/hello")
    public String hello() {
        return "Hello, HRMS API is working!";
    }
}