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

    public UserData() {
    }

    public UserData(
            User user,
            Long id,
            String dayRecord,
            String weekRecord,
            String monthRecord,
            String toDoList
    ) {
        this.user = user;
        this.id = id;
        this.dayRecord = dayRecord;
        this.weekRecord = weekRecord;
        this.monthRecord = monthRecord;
        this.toDoList = toDoList;
    }

    public  UserData(User user, String dayRecord, String weekRecord, String monthRecord){
        this.user = user;
        this.dayRecord = dayRecord;
        this.weekRecord = weekRecord;
        this.monthRecord = monthRecord;
        this.toDoList = "";
    }
    public  UserData (User user,String toDoList){
        this.user = user;
        this.toDoList = toDoList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
