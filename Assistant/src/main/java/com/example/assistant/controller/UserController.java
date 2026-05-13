package com.example.assistant.controller;

import com.example.assistant.dto.RequestUserDto;
import com.example.assistant.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

 public ResponseEntity registerUser(@RequestBody RequestUserDto requestUserDto){
     return ResponseEntity.ok().build();
  }
  public ResponseEntity loginUser(@RequestBody RequestUserDto requestUserDto){
      return ResponseEntity.ok().build();
  }

  public ResponseEntity updateUser(@RequestBody RequestUserDto requestUserDto){
      return ResponseEntity.ok().build();
  }

  public ResponseEntity deleteUser(@RequestBody RequestUserDto requestUserDto){
     return ResponseEntity.ok().build();
  }

  public ResponseEntity changePassword(@RequestBody RequestUserDto requestUserDto){
     return ResponseEntity.ok().build();
  }
}
