package com.yk.utils;

import com.yk.entities.Message;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * [com.yk.utils desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/28
 */
public class CommonUtils {

    private static Logger logger = Logger.getLogger(CommonUtils.class);

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
        return hash("MD5", password);
    }

    /**
     * [描述： desc]
     *
     * @param algo 要使用的哈希算法，例如："md5"，"sha256"，"haval160,4" 等。
     * @param data 要进行哈希运算的消息。
     * @param hex  输出进制字符串。
     * @return String
     * @author yangkun[Email:vectormail@163.com] 2018/6/25
     */
    public static String hash(String algo, String data, int hex) {

        try {
            MessageDigest md = MessageDigest.getInstance(algo);
            String        hx = new BigInteger(1, md.digest(data.getBytes())).toString(hex);
            logger.info(data + "====encryption to ====>" + hx);
            return hx;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String hash(String algo, String data) {
        return hash(algo, data, 16);
    }
}
