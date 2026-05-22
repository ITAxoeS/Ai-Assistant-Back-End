package com.example.assistant.tools;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTool {
    private final SecretKey key = Keys.hmacShaKeyFor(
            "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes()
    );
    private final long EXPIRATION_TIME = 604800000;

    public String generateToken(String account, Long userId) {
        // 创建Token的payload（负载）
        Map<String, Object> claims = new HashMap<>();
        claims.put("account", account);
        claims.put("userId", userId);

        // 构建JWT
        return Jwts.builder()
                .setClaims(claims)                      // 设置用户信息
                .setSubject(account)                   // 设置主题
                .setIssuedAt(new Date())               // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 设置过期时间
                .signWith(key)                         // 使用密钥签名
                .compact();                            // 生成最终的Token字符串
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)  // 用同一个密钥验证
                    .build()
                    .parseClaimsJws(token); // 解析Token
            return true; // 如果没有异常，说明Token有效
        } catch (Exception e) {
            System.out.println("Token验证失败: " + e.getMessage());
            return false;
        }
    }

    public String getAccountFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("account", String.class);
        } catch (Exception e) {
            throw new RuntimeException("无法从Token中提取账号", e);
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            throw new RuntimeException("无法从Token中提取用户ID", e);
        }
    }


    public static void main(String[] args) {
        JwtTool jwtTool = new JwtTool();

        // 测试生成Token
        String token = jwtTool.generateToken("testuser", 123L);
        System.out.println("生成的Token: " + token);

        // 测试验证Token
        boolean isValid = jwtTool.validateToken(token);
        System.out.println("Token是否有效: " + isValid);

        // 测试提取信息
        if (isValid) {
            String account = jwtTool.getAccountFromToken(token);
            Long userId = jwtTool.getUserIdFromToken(token);
            System.out.println("从Token中提取的账号: " + account);
            System.out.println("从Token中提取的用户ID: " + userId);
        }
    }
}
