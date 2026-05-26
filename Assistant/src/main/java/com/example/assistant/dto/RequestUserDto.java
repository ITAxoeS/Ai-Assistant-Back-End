package com.example.assistant.dto;


public class RequestUserDto {

    private  String userId;
    private String userAccount;
    private String userName;
    private String userPassWord;
    private String userArea;
    private String userBirth;
    private String userSex;
    private String userAvatar;


    public String getUserId() {
        return userId;
    }

    public String getUserArea() {
        return userArea;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public String getUserPassWord() {
        return userPassWord;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public String getUserSex() {
        return userSex;
    }

    public String getUserAvatar() {
        return userAvatar;
    }
}
