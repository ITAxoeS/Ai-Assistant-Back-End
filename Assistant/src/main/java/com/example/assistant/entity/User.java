package com.example.assistant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@Table(name = "user")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//Id为主键

    @Column(unique = true, length = 20)
    private String userAccount;//用户账号

    @Column(length = 16)
    private String userPassword;//用户密码

    @Lob
    @Column(length = 7_500_000)
    private String userAvatar;//用户头像

    @Column(length = 7)
    private String userName;//用户昵称

    @Column(length = 1)
    private String userSex;// 用户性别

    @Column(length = 6)
    private String userBirth;//用户生日

    @Column(length = 7)
    private String userArea;//用户地区
}
