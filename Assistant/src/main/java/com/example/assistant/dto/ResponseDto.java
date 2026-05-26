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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private boolean success;//是否成功
    private String message;//提示信息
    private T data;//响应的内容，更新东西

    public ResponseDto() {
    }

    public ResponseDto(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(true, message,  data);
    }

    public static <T> ResponseDto<T> error(String message) {
        return new ResponseDto<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
