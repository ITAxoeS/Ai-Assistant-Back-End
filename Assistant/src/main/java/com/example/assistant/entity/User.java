package com.example.assistant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
//@Data
@Table(name = "users")
@DynamicUpdate
//@NoArgsConstructor
//@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//Id为主键

    @Column(unique = true, length = 20)
    private String userAccount;//用户账号

    @Column()
    private String userPassword;//用户密码

    private String userRegisterTime;//注册时间

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String userAvatar;//用户头像

    @Column(length = 7)
    private String userName;//用户昵称

    @Column(length = 2)
    private String userSex;// 用户性别

    @Column(length = 6)
    private String userBirth;//用户生日

    @Column(length = 7)
    private String userArea;//用户地区

    public User() {
    }

    public  User(String userAccount, String userPassword, String userRegisterTime){
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userRegisterTime = userRegisterTime;
        this.userAvatar="";
        this.userName="";
        this.userSex="";
        this.userBirth="";
        this.userArea="";
    }
    public User(
            Long id,
            String userAccount,
            String userPassword,
            String userRegisterTime,
            String userAvatar,
            String userName,
            String userSex,
            String userBirth,
            String userArea
    ) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userRegisterTime = userRegisterTime;
        this.userAvatar = userAvatar;
        this.userName = userName;
        this.userSex = userSex;
        this.userBirth = userBirth;
        this.userArea = userArea;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRegisterTime() {
        return userRegisterTime;
    }

    public void setUserRegisterTime(String userRegisterTime) {
        this.userRegisterTime = userRegisterTime;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserArea() {
        return userArea;
    }

    public void setUserArea(String userArea) {
        this.userArea = userArea;
    }
}
