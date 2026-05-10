package com.example.assistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@Table(name = "chat_storage",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","id"}))
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class ChatStorage {
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String createTime;//时间戳

    private String chatTitle;

    @Lob
    @Column(columnDefinition = "LONGTEXT")//保险起见，用大字符串
    private  String message;//对话历史消息字符串
}
