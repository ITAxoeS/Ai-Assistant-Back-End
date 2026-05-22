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

    private  final  UserRepository userRepository;
    private final ChatListRepository chatListRepository;
    private  final ChatStorageRepository chatStorageRepository;
    private  final UserDataRepository userDataRepository;

    // 通过构造函数注入
    @Autowired // Spring 4.3 以后，如果类只有一个构造函数，这个注解甚至可以省略
    public DataService(UserRepository userRepository,ChatListRepository chatListRepository, ChatStorageRepository chatStorageRepository,UserDataRepository userDataRepository) {
        this.userRepository = userRepository;
        this.chatListRepository = chatListRepository;
        this.chatStorageRepository = chatStorageRepository;
        this.userDataRepository = userDataRepository;
    }

    public Map<String,Object> saveChat(RequestDataDto requestDataDto,String account){
//        String account = requestDataDto.getUserAccount();
        String createTime = requestDataDto.getChatId();
        String chatTitle = requestDataDto.getChatTitle();
        String messages = requestDataDto.getMessages();
        String chatList = requestDataDto.getChatList();
        Optional<User> userOptional = userRepository.findByUserAccount(account);
        if (userOptional.isPresent()) {
            Map<String, Object> map = new HashMap<>();
            Optional<ChatList> chatListOptional = chatListRepository.findByUserId(userOptional.get().getId());
            Optional<ChatStorage> chatStorageOptional = chatStorageRepository.findByUserId(userOptional.get().getId());
            if(chatListOptional.isPresent()&& chatStorageOptional.isPresent()) {//存在更新
                //更新对话列表
                ChatList cl = chatListOptional.get();
                cl.setChatList(chatList);
                ChatList updatedCl = chatListRepository.save(cl);
                //更新对话存储
                ChatStorage cs = chatStorageOptional.get();
                cs.setMessage(messages);
                chatStorageRepository.save(cs);
                map.put("code", "200");
                map.put("chatList", updatedCl.getChatList());
            }else{//不存在，新建
                User user = userOptional.get();
                ChatStorage cs = new ChatStorage(user, null, createTime, chatTitle, messages);
                ChatList cl = new ChatList(user, null, chatList);
                chatStorageRepository.save(cs);//保存到数据库
                ChatList newCl = chatListRepository.save(cl);
                map.put("code", "200");
                map.put("chatList", newCl.getChatList());
            }
            return map;
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("code","404");
            return map;
        }
    }

    public  Map<String,Object> getChat(String chatId, String account){
        Optional<User> userOptional = userRepository.findByUserAccount(account);//获取当前用户
        Map<String,Object> map = new HashMap<>();
        if (userOptional.isPresent()) {
            Optional<ChatStorage> chatStorageOptional = chatStorageRepository.findByUserIdAndCreateTime(userOptional.get().getId(),chatId);
            if (chatStorageOptional.isPresent()) {
                ChatStorage cs = chatStorageOptional.get();//拿到对象；
                map.put("code","200");
                map.put("chatTitle",cs.getChatTitle());
                map.put("messages",cs.getMessage());
                map.put("id",cs.getCreateTime());
            }else{
                map.put("code","404");
            }
            return  map;
        }else{
            map.put("code","404");
            return  map;
        }
    }

    public  Boolean updateChatList(RequestDataDto requestDataDto,String account){
        String chatList = requestDataDto.getChatList();
        Optional<User> userOptional = userRepository.findByUserAccount(account);//获取当前用户
        if (userOptional.isPresent()) {
            Optional<ChatList> chatListOptional = chatListRepository.findByUserId(userOptional.get().getId());
            if ( chatListOptional.isPresent()) {//存在，更新
                ChatList cl =  chatListOptional.get();//拿到旧对象；
                cl.setChatList(chatList);
                chatListRepository.save(cl);
            }else{//不存在，新建
                ChatList cl = new ChatList(userOptional.get(), null,chatList);//保存到当前用户为外键的数据表
                chatListRepository.save(cl);
            }
            return  true;
        }else{
            return  false;
        }
    }

    public  Boolean saveTimedata(RequestDataDto requestDataDto,String account){
        String dayRecord = requestDataDto.getDayRecord();
        String weekRecord = requestDataDto.getWeekRecord();
        String monthRecord = requestDataDto.getMonthRecord();
        Optional<User> userOptional = userRepository.findByUserAccount(account);//获取当前用户
        if (userOptional.isPresent()) {
            Optional<UserData> userDataOptional = userDataRepository.findByUserId(userOptional.get().getId());
            if (userDataOptional.isPresent()) {//存在，更新
              UserData ud = userDataOptional.get();//拿到旧对象；
                ud.setDayRecord(dayRecord);
                ud.setWeekRecord(weekRecord);
                ud.setMonthRecord(monthRecord);
                userDataRepository.save(ud);
            }else{//不存在，新建
                UserData ud = new UserData(userOptional.get(), dayRecord, weekRecord, monthRecord);//保存到当前用户为外键的数据表
                userDataRepository.save(ud);
            }
            return  true;
        }else{
            return  false;
        }
    }

    public  Boolean updateTodo(RequestDataDto requestDataDto,String account){
        String toDoList = requestDataDto.getToDoList();
        Optional<User> userOptional = userRepository.findByUserAccount(account);//获取当前用户
        if (userOptional.isPresent()) {
            Optional<UserData> userDataOptional = userDataRepository.findByUserId(userOptional.get().getId());
            if (userDataOptional.isPresent()) {//存在，更新
                UserData ud = userDataOptional.get();//拿到旧对象；
                ud.setToDoList(toDoList);
                userDataRepository.save(ud);
            }else{//不存在，新建
                UserData ud = new UserData(userOptional.get(), toDoList);//保存到当前用户为外键的数据表
                userDataRepository.save(ud);
            }
            return  true;
        }else{
            return  false;
        }
    }
}
