package com.example.assistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
//@Data
@Table(name = "chat_list", uniqueConstraints = @UniqueConstraint(
        name = "uk_chat_list_user_id",
        columnNames = "user_id"  //避免总是返回多条
),indexes = @Index(
        name = "idx_chat_list_user_id",
        columnList = "user_id"
))
//@Table(name = "chat_storage",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","id"}))//不是歌曲收藏，不需要唯一约束
@DynamicUpdate
//@NoArgsConstructor
//@AllArgsConstructor
public class ChatList {
    //    @ManyToOne
//    @JoinColumn(name = "user_id")
//    @JsonIgnore
//    private User user;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")//保险起见，用大字符串
    private String chatList;//对话历史列表

    public ChatList() {
    }

    public ChatList(Long userId, Long id, String chatList) {
        this.userId = userId;
        this.id = id;
        this.chatList = chatList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getChatList() {
        return chatList;
    }

    public void setChatList(String chatList) {
        this.chatList = chatList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
