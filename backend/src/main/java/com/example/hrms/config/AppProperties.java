package com.example.hrms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用配置属性
 */
@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    
    /**
     * 初始化配置
     */
    private Init init = new Init();
    
    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();
    
    /**
     * 文件上传配置
     */
    private Upload upload = new Upload();
    
    @Data
    public static class Init {
        /**
         * 是否初始化测试数据
         */
        private boolean testData = false;
        
        /**
         * 是否强制重新初始化
         */
        private boolean force = false;
    }
    
    @Data
    public static class Jwt {
        /**
         * JWT密钥
         */
        private String secret = "hrms_default_secret_key_2025";
        
        /**
         * JWT过期时间（秒）
         */
        private long expiration = 86400; // 24小时
        
        /**
         * Token前缀
         */
        private String tokenPrefix = "Bearer ";
        
        /**
         * Token头
         */
        private String tokenHeader = "Authorization";
    }
    
    @Data
    public static class Upload {
        /**
         * 上传文件存储路径
         */
        private String path = "./uploads/";
        
        /**
         * 允许的文件类型
         */
        private String[] allowedTypes = {"jpg", "jpeg", "png", "gif", "pdf", "doc", "docx", "xls", "xlsx"};
        
        /**
         * 最大文件大小（MB）
         */
        private long maxSize = 10;
    }
}
