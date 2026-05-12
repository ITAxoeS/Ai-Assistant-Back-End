package com.example.assistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@Table(name = "chat_list")
//@Table(name = "chat_storage",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","id"}))//不是歌曲收藏，不需要唯一约束
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class ChatList {
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")//保险起见，用大字符串
    private  String chatList;//对话历史列表
}
