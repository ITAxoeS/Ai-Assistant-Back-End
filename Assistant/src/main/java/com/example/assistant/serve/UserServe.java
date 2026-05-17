package com.example.assistant.serve;

import com.example.assistant.dto.RequestUserDto;
import jakarta.validation.Valid;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.Cipher;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;

@Service
public class UserServe {

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

    public String registerUser(String account,String password, String registerTime) {
        //进行解密
        try{
            String passWord = decrypt(password);//已验证成功
            return "11"
        }catch (Exception e){
            return  "错误了"+e.getMessage();
        }
    }

    public String decrypt(String encryptedData) throws Exception {
        //加载私钥
        PrivateKey privateKey = loadPrivateKey();

        //  创建解密器
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // Base64解码前端传来的加密数据
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        //解密
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        //转为字符串返回
        return new String(decryptedBytes, StandardCharsets.UTF_8);
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
