package com.example.assistant.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

@Data
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
}
