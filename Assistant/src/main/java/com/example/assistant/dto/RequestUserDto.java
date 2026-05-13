package com.example.assistant.dto;

import lombok.Data;

@Data
public class RequestUserDto {

    private  String userId;
    private String userAccount;
    private String userName;
    private String passWord;
    private String userArea;
    private String userBirth;
    private String userSex;
    private String userAvatar;
}
