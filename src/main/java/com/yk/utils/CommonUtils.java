package com.yk.utils;

import com.yk.entities.Message;

import java.security.MessageDigest;

/**
 * [com.yk.utils desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/28
 */
public class CommonUtils {


    public static String getUserChannelUK(Message m) {
        return md5(m.getRole() + "_" + m.getUid());
    }

    public static String getToUserChannelUK(Message m) {
        String prefix = "s_";
        if (m.getRole().toLowerCase().equals("s")) prefix = "t_";
        return md5(prefix + m.getTouid());
    }

    public static String htmlspecialchars(String s) {

        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll("<", "&lt;");
        return s;
    }

    public static String md5(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[]        result = digest.digest(password.getBytes());
            StringBuilder buffer = new StringBuilder();
            for (byte b : result) {
                int    number = b & 0xff;// 加盐
                String str    = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
