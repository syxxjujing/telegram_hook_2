package com.jujing.telehook_2.util;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2018/10/14.
 */

public class Aes {


    private static String AesKey = "12345678910Asdfx";// 密钥
    private static String ivParameter = "12345678910Asdfx";// 密钥默认偏移，可更改
    public static String Jie_Mi(String encData)  throws Exception {
        //获取AESkey
        Key secretKey = new SecretKeySpec(AesKey.getBytes(), "AES"); //密钥
        //获取加密工具
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //初始化加密工具
        cipher.init(Cipher.DECRYPT_MODE,secretKey,new IvParameterSpec(ivParameter.getBytes()));
        //还原文本框的base64文本为密文
        byte[] bytes = Base64.decode(encData, Base64.DEFAULT);
        //把密文解密为明文
        byte[] bytes1  =cipher.doFinal(bytes);
        return new String(bytes1);
    }

    public static String Jia_Mi(String srcData)  throws Exception {
        //获取AESkey
        Key secretKey = new SecretKeySpec(AesKey.getBytes(), "AES"); //密钥
        //获取加密工具
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //初始化加密工具
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,new IvParameterSpec(ivParameter.getBytes()));
        //放入我们要加密的内容 并加密
        byte[] bytes = cipher.doFinal(srcData.getBytes());
        //得到的字节在进行Base64换算
        byte[] base = Base64.encode(bytes,Base64.DEFAULT);
        return new String(base).replaceAll("(\r\n|\r|\n|\n\r)", "");
    }
    public static String buildReqStr(String data) {
        String dataNew = data.replace("\\n", "");
        String origin = "{\"data\":\"" + dataNew + "\"}";
        return origin;
    }

}
