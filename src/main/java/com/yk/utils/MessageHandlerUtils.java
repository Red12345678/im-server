package com.yk.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yk.entities.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * [com.yk.utils desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/4/16
 */
public class MessageHandlerUtils {
    private static Logger logger = Logger.getLogger(MessageHandlerUtils.class.getName());

    private static Jedis jedis = JredisUtils.getRedisInstance();

    private static String channelidObject = "wechat:channelid_object";
    private static String userChannelKey = "wechat:user_channelid_list";

    //login
    public static void loginHander(ChannelGroup channels, Channel channel) {
        try {
            Map<String, String> login = new HashMap<>();
            login.put(channel.id().asLongText(), ObjectSerializeUtils.serializeObject(channel.id()));
            jedis.hmset(channelidObject, login);
            channels.add(channel);
        } catch (Exception e) {
            logger.info("登录失败[" + e.getMessage() + "]");
        }

    }


    //logout
    public static void logoutHandler(ChannelGroup channels, Channel channel) {

        removeChannlid(channel);
        channels.remove(channel);

    }

    public static void removeChannlid(Channel channel) {
        jedis.hdel(channelidObject, channel.id().asLongText());
    }

    public static void initLogin(Channel channel, String fromToken) {
        Map<String, String> map = new HashMap<>();
        map.put(fromToken, channel.id().asLongText());
        jedis.hmset(userChannelKey, map);
    }

    public static void leaveOpation(String fromToken) {

        jedis.hdel(userChannelKey, fromToken);
    }

    public static void send() {

    }

    public static void onLogin(Message message,ChannelGroup channelGroup){
            channelGroup.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        return;
    }
    public static void onLeave(Message message,ChannelGroup channelGroup){
        List<String> channelkeys = jedis.hmget(userChannelKey,message.getFromToken());
        if (channelkeys != null && channelkeys.size()>0){
            for (String channelkey:channelkeys)
                if (channelkey != null ) jedis.hdel(channelidObject, channelkey);
        }
        channelGroup.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        return;
    }

    public static void onSay(Message message,ChannelGroup channelGroup) throws Exception{
        List<String> channelidList = jedis.hmget(userChannelKey, message.getToToken().split(","));
        List<String> tokenList = jedis.hmget(channelidObject, channelidList.toArray(new String[0]));
        if (tokenList != null && tokenList.size() > 0) {
            for (String toToken : tokenList) {
                if (toToken == null || toToken.trim().equals("")) continue;
                ChannelId channelId = (ChannelId) (ObjectSerializeUtils.stringSerializeObject(toToken));
                channelGroup.find(channelId).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
            }
        }
        return;
    }
    public static void sendMsgHandler(ChannelGroup channels, Channel channel, TextWebSocketFrame msg) {
        try {
            if (msg.text() != null && !msg.text().equals("")) {
                JSONObject jsonObject = JSON.parseObject(msg.text());
                String type = jsonObject.getString("type");
                if (type != null) {
                    Message message;
                    message = new Message();
                    message.setFromToken(jsonObject.getString("fromToken"));
                    message.setFromUser(jsonObject.getString("fromUser"));
                    message.setMessage(jsonObject.getString("message"));
                    message.setToUser(jsonObject.getString("toUser"));
                    message.setToToken(jsonObject.getString("toToken"));
                    switch (type){
                        case "LOGIN":
                            initLogin(channel, jsonObject.getString("fromToken"));
                            message.setType("LOGIN");
                            onLogin(message,channels);
                            break;
                        case "LEAVE":
                        case "LOGOUT":
                        case "OFFLINE":
                            leaveOpation(message.getFromToken());
                            message.setType("LEAVE");
                            onLeave(message,channels);
                            break;
                        case "SAY":
                            message.setType("SAY");
                            onSay(message,channels);
                            break;
                    }
                }
                return;
            }
            throw new RuntimeException(msg.text() + "[消息发失败]");
        } catch (Exception e) {
            logger.info("["+e.getCause()+"]发送失败[" + e.getMessage() + "]");
        }

    }
}
