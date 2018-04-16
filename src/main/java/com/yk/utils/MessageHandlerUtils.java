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

    private static String userLoginKey = "wechat:user_list";
    private static String userChannelKey = "wechat:user_channelid_list";

    //login
    public static void loginHander(ChannelGroup channels, Channel channel) {
        try {
            Map<String, String> login = new HashMap<>();
            login.put(channel.id().asLongText(), ObjectSerializeUtils.serializeObject(channel.id()));
            jedis.hmset(userLoginKey, login);
            channels.add(channel);
            //infromAllInleave(channels, channel, MsgType.LOGIN);
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
        jedis.hdel(userLoginKey, channel.id().asLongText());
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
                    if (type.equals("LOGIN")) {
                        initLogin(channel, jsonObject.getString("fromToken"));
                        message.setType("LOGIN");
                        channels.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                    }
                    if (type.equals("LEAVE") || type.equals("LOGOUT") || type.equals("OFFLINE")) {
                        leaveOpation(jsonObject.getString("fromToken"));
                        message.setType("LEAVE");
                        channels.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                    }
                    if (type.equals("MSGBODY")) {
                        message.setType("MSGBODY");
                        List<String> channelidList = jedis.hmget(userChannelKey, message.getToToken().split(","));
                        List<String> tokenList = jedis.hmget(userLoginKey, channelidList.toArray(new String[0]));
                        if (tokenList != null && tokenList.size() > 0) {
                            for (String toToken : tokenList) {
                                ChannelId channelId = (ChannelId) (ObjectSerializeUtils.stringSerializeObject(toToken));
                                channels.find(channelId).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                            }
                        }
                    }
                }
                return;
            }
            throw new RuntimeException(msg.text() + "[消息发失败]");
        } catch (Exception e) {
            logger.info("发送失败[" + e.getMessage() + "]");
        }

    }
}
