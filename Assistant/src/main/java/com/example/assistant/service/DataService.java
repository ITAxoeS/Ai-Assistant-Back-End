package com.example.assistant.service;

import com.example.assistant.dao.ChatListRepository;
import com.example.assistant.dao.ChatStorageRepository;
import com.example.assistant.dao.UserRepository;
import com.example.assistant.dto.RequestDataDto;
import com.example.assistant.entity.ChatList;
import com.example.assistant.entity.ChatStorage;
import com.example.assistant.entity.User;
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

    // 通过构造函数注入
    @Autowired // Spring 4.3 以后，如果类只有一个构造函数，这个注解甚至可以省略
    public DataService(UserRepository userRepository,ChatListRepository chatListRepository, ChatStorageRepository chatStorageRepository) {
        this.userRepository = userRepository;
        this.chatListRepository = chatListRepository;
        this.chatStorageRepository = chatStorageRepository;
    }

    public Map<String,Object> saveChat(RequestDataDto requestDataDto){
        String account = requestDataDto.getUserAccount();
        String createTime = requestDataDto.getChatId();
        String chatTitle = requestDataDto.getChatTitle();
        String messages = requestDataDto.getMessages();
        String chatList = requestDataDto.getChatList();
        Optional<User> userOptional = userRepository.findByUserAccount(account);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ChatStorage cs = new ChatStorage(user,null,createTime,chatTitle,messages);
            ChatList cl = new ChatList(user,null,chatList);
            chatStorageRepository.save(cs);//保存到数据库
            ChatList newCl = chatListRepository.save(cl);
            Map<String, Object> map = new HashMap<>();
            map.put("code","200");
            map.put("chatList", newCl.getChatList());
            return map;
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("code","404");
            return map;
        }
    }
}
