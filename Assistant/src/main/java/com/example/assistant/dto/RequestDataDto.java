package com.example.assistant.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class RequestDataDto {

    private String userAccount;
    private String chatId;
    private String chatTitle;
    @JsonRawValue
    private String messages;  // 告诉Jackson这是原始JSON，不要处理
    @JsonRawValue
    private String chatList;
    private String dayRecord;
    private String weekRecord;
    private String monthRecord;
    private  String toDoList;

    public String getUserAccount() {
        return userAccount;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public String getChatId() {
        return chatId;
    }

    public String getMessages() {
        return messages;
    }

    public String getChatList() {
        return chatList;
    }

    public String getDayRecord() {
        return dayRecord;
    }

    public String getWeekRecord() {
        return weekRecord;
    }

    public String getToDoList() {
        return toDoList;
    }

    public String getMonthRecord() {
        return monthRecord;
    }
}
