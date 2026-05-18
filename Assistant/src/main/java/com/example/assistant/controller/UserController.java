package com.example.assistant.controller;

import com.example.assistant.dto.RequestUserDto;

import com.example.assistant.dto.ResponseDto;
import com.example.assistant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
            return ResponseEntity.ok().body(ResponseDto.success("获取成功",null, userService.getPublicKeyContent()));
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
            return  ResponseEntity.ok().body(ResponseDto.success("注册成功",null,""));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ResponseDto.error("注册失败: " + e.getMessage()));
        }
    }
    @PostMapping("/login-user")
    public ResponseEntity loginUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        try{
            Map<String ,Object> map = userService.loginUser(requestUserDto);
            if (map.get("code").equals("200")){
                return  ResponseEntity.ok().body(ResponseDto.success("注册成功",null,map));
            }
            return  ResponseEntity.status(401).body(ResponseDto.success("登录失败",null,map));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ResponseDto.error("登录失败: " + e.getMessage()));
        }
    }

    @PostMapping("/update-user")
    public ResponseEntity updateUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        try{
            Map<String ,Object> map = userService.updateUser(requestUserDto);
            if (map.get("code").equals("200")){
                return  ResponseEntity.ok().body(ResponseDto.success("修改成功",null,map));
            }
            return  ResponseEntity.status(404).body(ResponseDto.success("修改失败",null,map));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ResponseDto.error("修改失败: " + e.getMessage()));
        }
    }

    public ResponseEntity deleteUser(@RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity changePassword(@RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.ok().build();
    }
}
