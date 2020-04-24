package com.kingyee.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * AES位加密算法
 * 加密模式：AES/CBC/PKCS5Padding
 */
public class AesUtil {

    private static Charset CHARSET = Charset.forName("utf-8");

    /**
     * AES 128位加密
     * 加密方法：密钥为16的字符串。IV为随机的16位字符串。
     * 对加密，将IV和密文拼接起来，再进行base64 encode后输出。与decode128方法配套使用
     *
     * @param text 明文
     * @param key  密钥（16位）
     * @return
     * @throws Exception
     */
    public static String encode128(String text, String key) throws Exception {
        if (key == null || key.length() != 16) {
            throw new Exception("秘钥长度必须为16个字符。");
        }

        byte[] aeskey = key.getBytes(CHARSET);
        byte[] ivArray = RandomUtil.generateString(16).getBytes(CHARSET);

        // 设置加密模式为AES的CBC模式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(aeskey, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivArray, 0, 16);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        // 加密
        byte[] encrypted = cipher.doFinal(text.getBytes(CHARSET));

        // 使用BASE64对加密后的字符串进行编码
        encrypted = ArrayUtils.addAll(iv.getIV(), encrypted);
        String base64Encrypted = Base64.encodeBase64String(encrypted);

        return base64Encrypted;
    }

    /**
     * AES 128位解密
     * 解密方法：密钥为16位的字符串。IV为密文的前16位字符串，先将decodeBase64(text)的前16位作为IV，后面作为真正的密文。
     * 与encode128方法配套使用
     *
     * @param text 明文
     * @param key  密钥（16位）
     * @return
     * @throws Exception
     */
    public static String decode128(String text, String key) throws Exception {

        if (key == null || key.length() != 16) {
            throw new Exception("秘钥长度必须为16个字符。");
        }

        // 使用BASE64对密文进行解码
        byte[] encrypted = Base64.decodeBase64(text);

        byte[] aeskey = key.getBytes(CHARSET);
        byte[] ivArray = ArrayUtils.subarray(encrypted, 0, 16);
        encrypted = ArrayUtils.subarray(encrypted, 16, encrypted.length);

        // 设置解密模式为AES的CBC模式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key_spec = new SecretKeySpec(aeskey, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivArray, 0, 16);
        cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

        // 解密
        byte[] original = cipher.doFinal(encrypted);
        String decode = new String(original, CHARSET);
        return decode;
    }

    /**
     * 加密
     *
     * @param text
     * @return
     */
    public static String encode(String text) {
        try {
            return encode128(text, "8eHuQRBtPyNssYkl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void main(String[] args) throws Exception {
        String params = AesUtil.encode128("123456", "8eHuQRBtPyNssYkl");
        System.out.println("加密:" + params);
        System.out.println("解密:" + AesUtil.decode128(params, "8eHuQRBtPyNssYkl"));
    }
}
