package com.example.assistant.service;

import com.example.assistant.dao.ChatListRepository;
import com.example.assistant.dao.ChatStorageRepository;
import com.example.assistant.dao.UserDataRepository;
import com.example.assistant.dao.UserRepository;
import com.example.assistant.dto.RequestDataDto;
import com.example.assistant.entity.ChatList;
import com.example.assistant.entity.ChatStorage;
import com.example.assistant.entity.User;
import com.example.assistant.entity.UserData;
import jakarta.persistence.Lob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DataService {

    private final ChatListRepository chatListRepository;
    private final ChatStorageRepository chatStorageRepository;
    private final UserDataRepository userDataRepository;

    // 通过构造函数注入
    @Autowired // Spring 4.3 以后，如果类只有一个构造函数，这个注解甚至可以省略
    public DataService( ChatListRepository chatListRepository, ChatStorageRepository chatStorageRepository, UserDataRepository userDataRepository) {
        this.chatListRepository = chatListRepository;
        this.chatStorageRepository = chatStorageRepository;
        this.userDataRepository = userDataRepository;
    }

    public Map<String, Object> saveChat(RequestDataDto requestDataDto, Long userId) {
        String createTime = requestDataDto.getChatId();
        String chatTitle = requestDataDto.getChatTitle();
        String messages = requestDataDto.getMessages();
        String chatList = requestDataDto.getChatList();
        Map<String, Object> map = new HashMap<>();
        Optional<ChatStorage> chatStorageOptional = chatStorageRepository.findByUserIdAndCreateTime(userId, createTime);
        if (chatStorageOptional.isPresent()) {//存在更新
            //更新对话存储
            ChatStorage cs = chatStorageOptional.get();
            cs.setMessage(messages);
            chatStorageRepository.save(cs);
        } else {//不存在，新建
            ChatStorage cs = new ChatStorage(userId, null, createTime, chatTitle, messages);
            chatStorageRepository.save(cs);//保存到数据库
        }
        Map m = updateChatList(requestDataDto, userId);
        map.put("chatList", m.get("chatList"));
        return map;
    }

    public Map<String, Object> getChat(String chatId, Long userId)throws  Exception {
        Map<String, Object> map = new HashMap<>();
        Optional<ChatStorage> chatStorageOptional = chatStorageRepository.findByUserIdAndCreateTime(userId, chatId);
        if (chatStorageOptional.isPresent()) {
            ChatStorage cs = chatStorageOptional.get();//拿到对象；
            map.put("chatTitle", cs.getChatTitle());
            map.put("messages", cs.getMessage());
            map.put("id", cs.getCreateTime());
        } else {
            throw new Exception("对话不存在");
        }
        return map;
    }

    public void deleteChat(Long userId, String chatId) throws Exception {
        chatStorageRepository.deleteByUserIdAndCreateTime(userId, chatId);
    }

    public Map<String, Object> updateChatList(RequestDataDto requestDataDto, Long userId) {
        String chatList = requestDataDto.getChatList();
        Map<String, Object> map = new HashMap<>();
            Optional<ChatList> chatListOptional = chatListRepository.findByUserId(userId);
            if (chatListOptional.isPresent()) {//存在，更新
                ChatList cl = chatListOptional.get();//拿到旧对象；
                cl.setChatList(chatList);
                ChatList result = chatListRepository.save(cl);
                map.put("chatList", result.getChatList());
                return map;
            } else {//不存在，新建
                ChatList cl = new ChatList(userId, null, chatList);//保存到当前用户为外键的数据表
                ChatList result = chatListRepository.save(cl);
                map.put("chatList", result.getChatList());
                return map;
            }
    }

    public Boolean saveTimedata(RequestDataDto requestDataDto, Long userId) {
        String dayRecord = requestDataDto.getDayRecord();
        String weekRecord = requestDataDto.getWeekRecord();
        String monthRecord = requestDataDto.getMonthRecord();
            Optional<UserData> userDataOptional = userDataRepository.findByUserId(userId);
            if (userDataOptional.isPresent()) {//存在，更新
                UserData ud = userDataOptional.get();//拿到旧对象；
                ud.setDayRecord(dayRecord);
                ud.setWeekRecord(weekRecord);
                ud.setMonthRecord(monthRecord);
                userDataRepository.save(ud);
            } else {//不存在，新建
                UserData ud = new UserData(userId, dayRecord, weekRecord, monthRecord);//保存到当前用户为外键的数据表
                userDataRepository.save(ud);
            }
            return true;
    }

    public Boolean updateTodo(RequestDataDto requestDataDto, Long userId) {
        String toDoList = requestDataDto.getToDoList();
            Optional<UserData> userDataOptional = userDataRepository.findByUserId(userId);
            if (userDataOptional.isPresent()) {//存在，更新
                UserData ud = userDataOptional.get();//拿到旧对象；
                ud.setToDoList(toDoList);
                userDataRepository.save(ud);
            } else {//不存在，新建
                UserData ud = new UserData(userId, toDoList);//保存到当前用户为外键的数据表
                userDataRepository.save(ud);
            }
            return true;
    }
}
