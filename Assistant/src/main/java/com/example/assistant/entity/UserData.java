package com.example.assistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@Table(name = "user_data")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//Id为主键

    private String dayRecord;


    private String weekRecord;

    private String monthRecord;

    private  String toDoList;
}
