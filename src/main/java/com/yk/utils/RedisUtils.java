package com.yk.utils;


import com.yk.im.ImServer;
import redis.clients.jedis.Jedis;

public class RedisUtils {
    private static String  hostRedis;
    private static String  auth;
    private static Integer port;
    private static Integer db;
    private static Jedis   jedis;

    static {
        hostRedis = ImServer.conf.getProperty("im.redis.host", null);
        port = Integer.valueOf(ImServer.conf.getProperty("im.redis.port", "6379"));
        auth = ImServer.conf.getProperty("im.redis.auth", null);
        db = Integer.valueOf(ImServer.conf.getProperty("im.redis.default.db", "1"));
    }

    public static Jedis instance() {
        if (null == hostRedis || hostRedis.equals("")) return null;
        if (jedis != null) {
            return jedis;
        }
        jedis = new Jedis(hostRedis, port);
        jedis.select(db);
        if (auth != null && !auth.equals("")) jedis.auth(auth);
        return jedis;
    }

    public static Jedis instance(int dbIndex) {
        setDb(dbIndex);
        jedis = instance();
        if (jedis == null) return null;
        jedis.select(db);
        return jedis;
    }

    public static String getHostRedis() {
        return hostRedis;
    }

    public static void setHostRedis(String hostRedis) {
        RedisUtils.hostRedis = hostRedis;
    }

    public static String getAuth() {
        return auth;
    }

    public static void setAuth(String auth) {
        RedisUtils.auth = auth;
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        RedisUtils.port = port;
    }

    public static Integer getDb() {
        return db;
    }

    public static void setDb(Integer db) {
        RedisUtils.db = db;
    }
}