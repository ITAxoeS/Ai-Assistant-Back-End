package com.example.assistant.tools;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws IOException, ServletException {
        //  从Cookie取Token
        String token = extractToken(request);

        if (token != null && jwtTool.validateToken(token)) {
            // 2. 验证创建认证信息
            String account = jwtTool.getAccountFromToken(token);
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    account, null, List.of(new SimpleGrantedAuthority("USER")) // 简单角色
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        //  继续执行过滤链进入下一步
        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // Cookie提取逻辑
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
