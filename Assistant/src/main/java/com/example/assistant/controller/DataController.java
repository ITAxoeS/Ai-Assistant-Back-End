package com.example.assistant.controller;

import com.example.assistant.dto.RequestDataDto;
import com.example.assistant.dto.ResponseDto;
import com.example.assistant.service.DataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    //保存对话，更新ChatStorage和ChatList
    public ResponseEntity saveChat(@Valid @RequestBody RequestDataDto requestDataDto){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String account = auth.getName(); // 当前账户
                Map<String,Object>  map = dataService.saveChat(requestDataDto,account);
                if(map.get("code").equals("200")) {
                    return ResponseEntity.ok().body(ResponseDto.success("保存成功", map));
                }
            }
            return ResponseEntity.status(404).body(ResponseDto.error("保存失败，用户不存在或数据库错误"));
        }catch(Exception e) {
            return  ResponseEntity.status(500).body(ResponseDto.error("保存失败"+e.getMessage()));
        }
    }

    @GetMapping("/get-chat")
    public ResponseEntity getChat(@RequestParam("chatId") String chatId){//根据ID获取对话
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String account = auth.getName(); // 当前账户
               Map<String,Object> map = dataService.getChat(chatId,account);
               if (map.get("code").equals("200")) {
                   return ResponseEntity.ok().body(ResponseDto.success("获取成功", map));
               }else{
                   return ResponseEntity.status(404).body(ResponseDto.error("对话不存在"));
               }
            }else {
                return ResponseEntity.status(404).body(ResponseDto.error("用户不存在"));
            }
        }catch (Exception e){
            return  ResponseEntity.status(500).body(ResponseDto.error("系统错误"));
        }
    }

    @PostMapping("/delete-chat")
    public  ResponseEntity  deleteChat(@RequestParam("chatId") String chatId){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String account = auth.getName(); // 当前账户
                 dataService.deleteChat(account,chatId);
            }
                return ResponseEntity.ok().body(ResponseDto.success("删除成功",""));
        }catch (Exception e){
            return  ResponseEntity.status(500).body(ResponseDto.error("系统错误"));
        }
    }
    //更新ChatList
    // 修改标题等操作
    @PostMapping("/update-chatlist")
    public ResponseEntity updateChatList(@Valid @RequestBody RequestDataDto requestDataDto){//更新对话列表
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String account = auth.getName(); // 当前账户
                Map<String,Object> result = dataService.updateChatList(requestDataDto,account);
                if (!result.isEmpty()) {
                    return ResponseEntity.ok().body(ResponseDto.success("保存成功", "对话列表成功更新保存"));
                } else {
                    return ResponseEntity.status(404).body(ResponseDto.error("用户不存在"));
                }
            }else {
                return ResponseEntity.status(404).body(ResponseDto.error("用户不存在"));
            }
        }catch (Exception e){
            return  ResponseEntity.status(500).body(ResponseDto.error("系统错误"));
        }
    }

    @PostMapping("save-timedata")
    public  ResponseEntity saveTimedata(@Valid @RequestBody RequestDataDto requestDataDto){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String account = auth.getName(); // 当前账户
                boolean result = dataService.saveTimedata(requestDataDto,account);
                if (result) {
                    return ResponseEntity.ok().body(ResponseDto.success("保存成功", "时间成功更新保存"));
                } else {
                    return ResponseEntity.status(404).body(ResponseDto.error("用户不存在"));
                }
            }else {
                return ResponseEntity.status(404).body(ResponseDto.error("用户不存在"));
            }
        }catch (Exception e){
            return  ResponseEntity.status(500).body(ResponseDto.error("系统错误"));
        }
    }

    @PostMapping("/update-todo")
    public ResponseEntity updateTodo(@Valid @RequestBody RequestDataDto requestDataDto){//获取对话列表
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String account = auth.getName(); // 当前账户
                boolean result = dataService.updateTodo(requestDataDto,account);
                if (result) {
                    return ResponseEntity.ok().body(ResponseDto.success("保存成功", "待办事项成功更新保存"));
                } else {
                    return ResponseEntity.status(404).body(ResponseDto.error("用户不存在"));
                }
            }else {
                return ResponseEntity.status(404).body(ResponseDto.error("用户不存在"));
            }
        }catch (Exception e){
            return  ResponseEntity.status(500).body(ResponseDto.error("系统错误"));
        }
    }

}
