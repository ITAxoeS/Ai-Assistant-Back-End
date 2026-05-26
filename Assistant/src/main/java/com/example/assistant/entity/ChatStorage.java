package com.example.assistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
//@Data
@Table(name = "chat_storage")
//@Table(name = "chat_storage",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","id"}))//不是歌曲收藏，不需要唯一约束
@DynamicUpdate
//@NoArgsConstructor
//@AllArgsConstructor
public class ChatStorage {
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String createTime;//时间戳

    @Column(columnDefinition = "TEXT")
    private String chatTitle;

    @Lob
    @Column(columnDefinition = "LONGTEXT")//保险起见，用大字符串
    private  String message;//对话历史消息字符串

    public ChatStorage() {
    }

    public ChatStorage(User user, Long id, String createTime, String chatTitle,String message ) {
        this.id = id;
        this.user = user;
        this.createTime = createTime;
        this.message = message;
        this.chatTitle = chatTitle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
