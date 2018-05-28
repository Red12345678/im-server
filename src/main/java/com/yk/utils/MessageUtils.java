package com.yk.utils;

import com.alibaba.fastjson.JSONObject;
import com.yk.entities.Message;
import com.yk.entities.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * [com.yk.utils desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/4/16
 */
public class MessageUtils {
    private static Logger logger = Logger.getLogger(MessageUtils.class);
    private static Map<String, Channel> ucMap = new ConcurrentHashMap<>();

    //login
    public static void loginHander(ChannelHandlerContext ctx, Message m) {
        ucMap.put(CommonUtils.getUserChannelUK(m), ctx.channel());
    }

    /**
     * [描述： deal request]
     *
     * @param ctx
     * @param msg
     * @author yangkun[Email:vectormail@163.com] 2018/5/28
     */
    public static void msgHandler(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        logger.info("loginHander => " + msg.text());
        Message m = JSONObject.parseObject(msg.text(), Message.class);
        if (null == m) return;
        switch (m.getType().toLowerCase()) {
            case "login":
                loginHander(ctx, m);
                break;
            case "say":
                sayHandler(ctx, m);
                break;
            case "logout":
                logoutHandler(ctx, m);
                break;
            case "ping":
                pingHandler(ctx, m);
                break;
        }
    }

    public static void logoutHandler(ChannelHandlerContext ctx, Message m) {
        ucMap.remove(CommonUtils.getUserChannelUK(m));
    }

    /**
     * [描述： save to DB]
     * @author yangkun[Email:vectormail@163.com] 2018/5/28
     * @param m
     */
    public static void saveMsgToDB(Message m) {
        MysqlUtils.insertMsgToDB(m);
    }

    /**
     * [描述： 发送消息处理]
     * @author yangkun[Email:vectormail@163.com] 2018/5/28
     * @param ctx
     * @param m
     */
    public static void sayHandler(ChannelHandlerContext ctx, Message m) {
        User u = MysqlUtils.getUserInfo(m);
        Channel c = ucMap.get(CommonUtils.getToUserChannelUK(m));
        m.setUser_name(u.getUsername());
        m.setHead_img(u.getFace());
        m.setContent(CommonUtils.htmlspecialchars(m.getContent()));
        if (null == c || !c.isActive()) {
            m.setStatus(MessageStatus.FAIL);
            saveMsgToDB(m);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(m)));
            return;
        }
        m.setStatus(MessageStatus.SUCCESS);
        c.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(m)));
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(m)));
        saveMsgToDB(m);
    }

    public static void pingHandler(ChannelHandlerContext ctx, Message m) {

    }

}
