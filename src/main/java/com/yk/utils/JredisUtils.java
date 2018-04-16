package com.yk.utils;

import redis.clients.jedis.Jedis;

/**
 * [com.yk desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/4/16
 */
public class JredisUtils {
    private static String hostRedis = "192.168.1.223";
    private static String auth = null;
    private static Jedis jedis;

    public static Jedis getRedisInstance(String auth) {
        setAuth(auth);
        return getRedisInstance();
    }

    public static Jedis getRedisInstance() {
        if (jedis != null) {
            return jedis;
        }
        jedis = new Jedis(hostRedis);
        if (auth != null) jedis.auth(auth);
        return jedis;
    }

    public static String getHostRedis() {
        return hostRedis;
    }

    public static void setHostRedis(String hostRedis) {
        JredisUtils.hostRedis = hostRedis;
    }

    public static String getAuth() {
        return auth;
    }

    public static void setAuth(String auth) {
        JredisUtils.auth = auth;
    }
}
