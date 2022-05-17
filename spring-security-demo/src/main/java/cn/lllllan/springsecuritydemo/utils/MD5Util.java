package cn.lllllan.springsecuritydemo.utils;

import java.security.MessageDigest;

public class MD5Util {

    /**
     * MD5 加密
     *
     * @param str 待加密字符串
     * @return 加密结果
     */
    public static String encrypt(String str) {
        try {
            java.security.MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }

            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}