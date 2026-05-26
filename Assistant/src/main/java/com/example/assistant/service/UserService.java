package com.example.assistant.service;

import com.example.assistant.dao.ChatListRepository;
import com.example.assistant.dao.UserDataRepository;
import com.example.assistant.dao.UserRepository;
import com.example.assistant.dto.RequestUserDto;
import com.example.assistant.entity.ChatList;
import com.example.assistant.entity.User;
import com.example.assistant.entity.UserData;
import com.example.assistant.tools.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.crypto.Cipher;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChatListRepository chatListRepository;


    // 通过构造函数注入
    @Autowired // Spring 4.3 以后，如果类只有一个构造函数，这个注解甚至可以省略
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserDataRepository userDataRepository, ChatListRepository chatListRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDataRepository = userDataRepository;
        this.chatListRepository = chatListRepository;
    }

    public String getPublicKeyContent() throws Exception {
        ClassPathResource resource = new ClassPathResource("keys/public_key.pem");
        if (!resource.exists()) {
            throw new FileNotFoundException("公钥文件未找到");
        }
        return StreamUtils.copyToString(
                resource.getInputStream(),
                StandardCharsets.UTF_8
        );
    }

    public void registerUser(RequestUserDto requestUserDto) throws Exception {
        String account = requestUserDto.getUserAccount();
        String password = requestUserDto.getUserPassWord();
        String registerTime = String.valueOf(Instant.now().toEpochMilli());
        //进行解密
        String passWord = decrypt(password);//解密(已验证成功)
        String encodedPassword = passwordEncoder.encode(passWord);
        User user = new User(account, encodedPassword, registerTime);
        userRepository.save(user);
    }

    public Map<String, Object> loginUser(RequestUserDto requestUserDto) throws Exception {
        String account = requestUserDto.getUserAccount();
        String password = requestUserDto.getUserPassWord();
        //进行解密
        String passWord = decrypt(password);//RSA解密(已验证成功)
        //对比
        Optional<User> userOptional = userRepository.findByUserAccount(account);
        Map<String, Object> map = new HashMap<>();
        if (userOptional.isPresent()) {//如果盒子内存在内容
            User user = userOptional.get();//获取，代表当前账户的对象
            if (passwordEncoder.matches(passWord, user.getUserPassword())) {//如果密码相同
                //封装数据
                map.put("code", "200");
                map.put("account", user.getUserAccount());
                map.put("avatar", user.getUserAvatar());
                map.put("name", user.getUserName());
                map.put("sex", user.getUserSex());
                map.put("birth", user.getUserBirth());
                map.put("area", user.getUserArea());
                map.put("register", user.getUserRegisterTime());
                Optional<UserData> userDataOptional = userDataRepository.findByUserId(user.getId());
                Optional<ChatList> chatListOptional = chatListRepository.findByUserId(user.getId());
                if (userDataOptional.isPresent()) {
                    UserData userData = userDataOptional.get();
                    map.put("dayRecord", userData.getDayRecord());
                    map.put("weekRecord", userData.getWeekRecord());
                    map.put("monthRecord", userData.getMonthRecord());
                    map.put("toDoList", userData.getToDoList());
                } else {
                    map.put("dayRecord", "");
                    map.put("weekRecord", "");
                    map.put("monthRecord", "");
                    map.put("toDoList", "");
                }
                if (chatListOptional.isPresent()) {
                    ChatList chatList = chatListOptional.get();
                    map.put("chatList", chatList.getChatList());
                } else {
                    map.put("chatList", "");
                }
                String token = new JwtTool().generateToken(user.getUserAccount(), user.getId());
                map.put("token", token);
//                System.out.println(map);
                return map;
            }
        }
        map.put("code", "401");
        return map;
    }

    public Map<String, Object> updateUser(RequestUserDto requestUserDto, String account) {
//        String account = requestUserDto.getUserAccount();
        String avatar = requestUserDto.getUserAvatar();
        String name = requestUserDto.getUserName();
        String sex = requestUserDto.getUserSex();
        String birth = requestUserDto.getUserBirth();
        String area = requestUserDto.getUserArea();
        Optional<User> userOptional = userRepository.findByUserAccount(account);//通过账号查询
        if (userOptional.isPresent()) {
            User user = userOptional.get();//获取代表当前账户的对象
            user.setUserAvatar(avatar);
            user.setUserName(name);
            user.setUserSex(sex);
            user.setUserBirth(birth);
            user.setUserArea(area);
            User resultUser = userRepository.save(user);
            Map<String, Object> map = new HashMap<>();
            map.put("code", "200");
            map.put("avatar", resultUser.getUserAvatar());
            map.put("name", resultUser.getUserName());
            map.put("sex", resultUser.getUserSex());
            map.put("birth", resultUser.getUserBirth());
            map.put("area", resultUser.getUserArea());
            map.put("register",resultUser.getUserRegisterTime());
//        System.out.println(ResponseData.success("登录成功", null, map));
            return map;
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("code", "404");
            return map;
        }
    }

    public Map<String, Object> checkUser(String account) throws Exception {
        Optional<User> userOptional = userRepository.findByUserAccount(account);
        if (userOptional.isPresent()) {//如果盒子内存在内容
            User user = userOptional.get();//获取，代表当前账户的对象
            //封装数据
            Map<String, Object> map = new HashMap<>();
            map.put("code", "200");
            map.put("account", user.getUserAccount());
            map.put("avatar", user.getUserAvatar());
            map.put("name", user.getUserName());
            map.put("sex", user.getUserSex());
            map.put("birth", user.getUserBirth());
            map.put("area", user.getUserArea());
            map.put("register", user.getUserRegisterTime());
            Optional<UserData> userDataOptional = userDataRepository.findByUserId(user.getId());
            Optional<ChatList> chatListOptional = chatListRepository.findByUserId(user.getId());
            if (userDataOptional.isPresent()) {
                UserData userData = userDataOptional.get();
                map.put("dayRecord", userData.getDayRecord());
                map.put("weekRecord", userData.getWeekRecord());
                map.put("monthRecord", userData.getMonthRecord());
                map.put("toDoList", userData.getToDoList());
            } else {
                map.put("dayRecord", "");
                map.put("weekRecord", "");
                map.put("monthRecord", "");
                map.put("toDoList", "");
            }
            if (chatListOptional.isPresent()) {
                ChatList chatList = chatListOptional.get();
                map.put("chatList", chatList.getChatList());
//                System.out.println(chatList.getChatList());
            } else {
                map.put("chatList", "");
            }
            return map;
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("code", "401");
            return map;
        }
    }

    public String decrypt(String encryptedData) throws Exception {
        //加载私钥
        PrivateKey privateKey = loadPrivateKey();
//        System.out.println("解密密码");
        //  创建解密器
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        System.out.println("解密密码0.5");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        System.out.println("解密密码1");
        // Base64解码前端传来的加密数据
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
//        System.out.println("解密密码2");
        try {
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
//            System.out.println("解密密码3");
            String result = new String(decryptedBytes, StandardCharsets.UTF_8);
//            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //  加载私钥
    private PrivateKey loadPrivateKey() throws Exception {
        //读取私钥文件
        ClassPathResource resource = new ClassPathResource("keys/private_key.pem");
        String privateKeyPem = StreamUtils.copyToString(
                resource.getInputStream(),
                StandardCharsets.UTF_8
        );

        //  清理PEM格式（移除头尾和换行符）
        privateKeyPem = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");  // 移除所有空白字符（空格、换行等）

        //  Base64解码
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);

        // 生成私钥对象
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
}
