package com.example.assistant.controller;

import com.example.assistant.dto.RequestDataDto;
import com.example.assistant.dto.ResponseDto;
import com.example.assistant.service.DataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/datas")
public class DataController {
    private  final  DataService dataService;
    @Autowired
    public  DataController(DataService dataService){
        this.dataService = dataService;
    }

    @PostMapping("/save-chat")
    public ResponseEntity saveChat(@Valid @RequestBody RequestDataDto requestDataDto){//保存对话，更新ChatStorage和ChatList
        try{
            Map<String,Object>  map = dataService.saveChat(requestDataDto);
            if(map.get("code").equals("200")) {
                return ResponseEntity.ok().body(ResponseDto.success("保存成功", null, map));
            }
            return ResponseEntity.status(404).body(ResponseDto.error("保存失败，用户不存在或数据库错误"));
        }catch(Exception e) {
            return  ResponseEntity.status(500).body(ResponseDto.error("保存失败"+e.getMessage()));
        }
    }

    public ResponseEntity getChat(){//根据ID获取对话

        return  ResponseEntity.ok().body("");
    }
    public ResponseEntity getChatList(){//获取对话列表

        return  ResponseEntity.ok().body("");
    }

    public ResponseEntity saveUserData(){//获取对话列表

        return  ResponseEntity.ok().body("");
    }
    public ResponseEntity getUserData(){//获取对话列表

        return  ResponseEntity.ok().body("");
    }
}
