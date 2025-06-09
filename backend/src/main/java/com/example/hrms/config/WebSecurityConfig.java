package com.example.hrms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 启用 CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            // 禁用 CSRF
            .csrf(csrf -> csrf.disable())
            // 配置请求授权
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/user/login", "/user/info", "/user/logout").permitAll()  // 允许登录相关接口
                .requestMatchers("/employees/**", "/departments/**", "/salaries/**").permitAll()  // 临时允许所有业务接口
                .requestMatchers("/api/**").permitAll()  // 允许所有API接口
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()  // 允许Swagger
                .anyRequest().permitAll()  // 临时完全放开，后续可以根据需要调整
            )
            // 配置会话管理
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // 配置异常处理
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":50008,\"message\":\"未授权访问\"}");
                })
            );

        return http.build();
    }
}
