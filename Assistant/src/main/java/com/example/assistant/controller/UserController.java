package com.example.assistant.controller;

import com.example.assistant.dto.RequestUserDto;

import com.example.assistant.dto.ResponseDto;
import com.example.assistant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/keys")
    public ResponseEntity getRsaPublicKey() {
        try {
            return ResponseEntity.ok().body(ResponseDto.success("获取成功", userService.getPublicKeyContent()));
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(404).body(ResponseDto.error("获取失败，公钥不存在"+e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseDto.error("读取公钥失败: " + e.getMessage()));
        }
    }

    @PostMapping("/register-user")
    public ResponseEntity registerUser(@Valid @RequestBody  RequestUserDto requestUserDto) {
        try{
             userService.registerUser(requestUserDto);
            return  ResponseEntity.ok().body(ResponseDto.success("注册成功",""));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ResponseDto.error("注册失败: " + e.getMessage()));
        }
    }
    @PostMapping("/login-user")
    public ResponseEntity loginUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        try{
            Map<String ,Object> map = userService.loginUser(requestUserDto);
            if (map.get("code").equals("200")){
                String token = (String) map.get("token");
                ResponseCookie cookie = ResponseCookie.from("auth_token", token)
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(7 * 24 * 60 * 60)
                        .sameSite("Strict")
                        .build();
               map.remove("token");
                return  ResponseEntity.ok().header("Set-Cookie",cookie.toString()).body(ResponseDto.success("注册成功",map));
            }
            return  ResponseEntity.status(401).body(ResponseDto.success("登录失败",map));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ResponseDto.error("登录失败: " + e.getMessage()));
        }
    }

    @PostMapping("/update-user")
    public ResponseEntity updateUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String account = auth.getName(); // 当前账户
                Map<String, Object> map = userService.updateUser(requestUserDto, account);
                if (map.get("code").equals("200")) {
                    return ResponseEntity.ok().body(ResponseDto.success("修改成功", map));
                }
            }
            return ResponseEntity.status(404).body(ResponseDto.error("修改失败"));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ResponseDto.error("修改失败: " + e.getMessage()));
        }
    }

    @PostMapping("/check-user")
    public ResponseEntity checkUser(){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String account = auth.getName(); // 当前账户
                System.out.println("账号："+account);
                Map<String, Object> map = userService.checkUser(account);
                if (map.get("code").equals("200")) {
                    return ResponseEntity.ok().body(ResponseDto.success("登录成功", map));
                }
            }
            return ResponseEntity.status(404).body(ResponseDto.error("验证失败"));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ResponseDto.error("验证失败"+e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity  logout(){
        ResponseCookie cookie = ResponseCookie.from("auth_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok().header("Set-Cookie",cookie.toString()).body(ResponseDto.success("退出成功",""));
    }
}
