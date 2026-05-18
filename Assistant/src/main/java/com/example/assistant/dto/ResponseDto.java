package com.example.assistant.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//<dependency>必要依赖
//<groupId>com.fasterxml.jackson.core</groupId>
//<artifactId>jackson-databind</artifactId>
//</dependency>

//是返回给前端的JSON对象
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private boolean success;//是否成功
    private String message;//提示信息
    private String token;//身份令牌
    private T data;//响应的内容，更新东西


    public ResponseDto(boolean success, String message, String token, T data) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.data = data;
    }

    public static <T> ResponseDto<T> success(String message, String token, T data) {
        return new ResponseDto<>(true, message, token, data);
    }

    public static <T> ResponseDto<T> error(String message) {
        return new ResponseDto<>(false, message, null, null);
    }
}
