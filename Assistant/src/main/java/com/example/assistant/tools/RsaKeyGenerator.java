package com.example.assistant.tools;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Component
public class RsaKeyGenerator {

//    @PostConstruct
    // 生成密钥对(公钥和私钥)
    public KeyPair generateKeyPair() throws Exception {
        //  创建密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //  初始化参数
        keyPairGenerator.initialize(2048);  // 密钥长度2048位
        // 生成密钥对
        return keyPairGenerator.generateKeyPair();
    }

    // 查看密钥的各个组成部分
    public void printKeyDetails(KeyPair keyPair) {
        // 获取公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        System.out.println("======= RSA 公钥信息 =======");
        System.out.println("模数 (n): " + publicKey.getModulus());
        System.out.println("指数 (e): " + publicKey.getPublicExponent());
        System.out.println("算法: " + publicKey.getAlgorithm());
        System.out.println("格式: " + publicKey.getFormat());
        System.out.println("编码长度: " + publicKey.getEncoded().length + " 字节");

        System.out.println("\n======= RSA 私钥信息 =======");
        System.out.println("模数 (n): " + privateKey.getModulus());
        System.out.println("指数 (d): " + privateKey.getPrivateExponent());
        System.out.println("算法: " + privateKey.getAlgorithm());
        System.out.println("格式: " + privateKey.getFormat());
    }

    // 生成PEM格式的密钥
    public String[] generatePemKeys() throws Exception {
        KeyPair keyPair = generateKeyPair();

        // 获取原始二进制编码
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

        // 转换为Base64
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKeyBytes);

        // 格式化为PEM
        String publicKeyPem = formatAsPem(publicKeyBase64, "PUBLIC KEY");
        String privateKeyPem = formatAsPem(privateKeyBase64, "PRIVATE KEY");

        return new String[]{publicKeyPem, privateKeyPem};
    }

    // PEM格式化：广泛使用的密钥存储格式，以-----BEGIN/END-----包裹Base64编码
    private String formatAsPem(String base64, String type) {
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN ").append(type).append("-----\n");

        // 每64个字符加一个换行
        for (int i = 0; i < base64.length(); i += 64) {
            int end = Math.min(base64.length(), i + 64);
            pem.append(base64.substring(i, end)).append("\n");
        }

        pem.append("-----END ").append(type).append("-----");
        return pem.toString();
    }
//    public static void main(String[] args) {
//        RsaKeyGenerator generator = new RsaKeyGenerator();
//        try {
//            System.out.println("正在生成RSA密钥对并保存到E盘...");
//
//            // 生成PEM格式的密钥
//            String[] pemKeys = generator.generatePemKeys();
//            String publicKeyPem = pemKeys[0];
//            String privateKeyPem = pemKeys[1];
//
//            // 输出到控制台
//            System.out.println("公钥：\n" + publicKeyPem);
//            System.out.println("\n私钥：\n" + privateKeyPem);
//
//            // 保存到E盘
//            String eDrivePath = "E:\\";
//
//            // 保存私钥
//            String privateKeyPath = eDrivePath + "private_key.pem";
//            Files.writeString(Path.of(privateKeyPath), privateKeyPem);
//            System.out.println("\n私钥已保存到: " + privateKeyPath);
//
//            // 保存公钥
//            String publicKeyPath = eDrivePath + "public_key.pem";
//            Files.writeString(Path.of(publicKeyPath), publicKeyPem);
//            System.out.println("公钥已保存到: " + publicKeyPath);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
