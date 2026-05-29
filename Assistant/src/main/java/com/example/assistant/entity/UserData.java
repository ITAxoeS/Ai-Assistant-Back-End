package com.example.assistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
//@Data
@Table(name = "user_data")
@DynamicUpdate
//@NoArgsConstructor
//@AllArgsConstructor
public class UserData {
    //    @ManyToOne
//    @JoinColumn(name = "user_id")
//    @JsonIgnore
//    private User user;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//Id为主键

    private String dayRecord;


    private String weekRecord;

    private String monthRecord;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String toDoList;

    public UserData() {
    }

    public UserData(
            Long userId,
            Long id,
            String dayRecord,
            String weekRecord,
            String monthRecord,
            String toDoList
    ) {
        this.userId = userId;
        this.id = id;
        this.dayRecord = dayRecord;
        this.weekRecord = weekRecord;
        this.monthRecord = monthRecord;
        this.toDoList = toDoList;
    }

    public UserData( Long userId, String dayRecord, String weekRecord, String monthRecord) {
        this.userId = userId;
        this.dayRecord = dayRecord;
        this.weekRecord = weekRecord;
        this.monthRecord = monthRecord;
        this.toDoList = "";
    }

    public UserData(Long userId, String toDoList) {
        this.userId = userId;
        this.toDoList = toDoList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDayRecord() {
        return dayRecord;
    }

    public void setDayRecord(String dayRecord) {
        this.dayRecord = dayRecord;
    }

    public String getWeekRecord() {
        return weekRecord;
    }

    public void setWeekRecord(String weekRecord) {
        this.weekRecord = weekRecord;
    }

    public String getMonthRecord() {
        return monthRecord;
    }

    public void setMonthRecord(String monthRecord) {
        this.monthRecord = monthRecord;
    }

    public String getToDoList() {
        return toDoList;
    }

    public void setToDoList(String toDoList) {
        this.toDoList = toDoList;
    }
}
