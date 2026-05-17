package com.example.assistant.controller;

import com.example.assistant.dto.RequestUserDto;

import com.example.assistant.serve.UserServe;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServe userServe;

    @GetMapping("/keys")
    public ResponseEntity<String> getRsaPublicKey() {
        try {
            String publicKey = userServe.getPublicKeyContent();
            return ResponseEntity.ok().body(publicKey);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("读取公钥失败: " + e.getMessage());
        }
    }

    @PostMapping("/register-user")
    public ResponseEntity registerUser(@Valid @RequestBody  RequestUserDto requestUserDto) {
        String account  = requestUserDto.getUserAccount();
        String password = requestUserDto.getUserPassWord();
        String registerTime = String.valueOf(Instant.now().toEpochMilli());
        try{
            return  ResponseEntity.ok().body(userServe.registerUser(account,password,registerTime));
        }catch (Exception e){
            return ResponseEntity.status(500)
                    .body("注册失败: " + e.getMessage());
        }
    }

    public ResponseEntity loginUser(@RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateUser(@RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteUser(@RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity changePassword(@RequestBody RequestUserDto requestUserDto) {
        return ResponseEntity.ok().build();
    }
}
