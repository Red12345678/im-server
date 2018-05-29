package com.yk.utils;

import com.alibaba.fastjson.JSONObject;
import com.yk.entities.Message;
import com.yk.entities.User;
import com.yk.servers.ChannelServer;
import com.yk.servers.MessageServer;
import com.yk.servers.UserServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.log4j.Logger;


/**
 * [com.yk.utils desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/4/16
 */
public class MessageUtils {
    private static Logger logger = Logger.getLogger(MessageUtils.class);

    private static UserServer    us = new UserServer();
    private static MessageServer ms = new MessageServer();
    private static ChannelServer cs = new ChannelServer();

    //login
    public static void loginHander(ChannelHandlerContext ctx, Message m) {
        logger.info("loginHander => " + m);
        cs.userJoin(ctx, m);
    }

    /**
     * [描述： deal request]
     *
     * @param ctx
     * @param msg
     * @author yangkun[Email:vectormail@163.com] 2018/5/28
     */
    public static void msgHandler(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
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

    public static void groupChatHandler(ChannelHandlerContext ctx, Message m) {
        logger.info("sayHandler => " + m);
        Channel c = cs.roomChannel(ctx, m);
        if (null == c || !c.isActive()) {
            m.setStatus(MessageStatus.FAIL);

        } else {
            m.setStatus(MessageStatus.SUCCESS);
        }
        saveMsgToDB(m);
        return;
    }

    public static void logoutHandler(ChannelHandlerContext ctx, Message m) {
        cs.userLogout(m);
        logger.info("logoutHandler => " + m);
    }

    /**
     * [描述： save to DB]
     *
     * @param m
     * @author yangkun[Email:vectormail@163.com] 2018/5/28
     */
    public static void saveMsgToDB(Message m) {
        ms.insertMsgToDB(m);
    }

    /**
     * [描述： 发送消息处理]
     *
     * @param ctx
     * @param m
     * @author yangkun[Email:vectormail@163.com] 2018/5/28
     */
    public static void sayHandler(ChannelHandlerContext ctx, Message m) {
        logger.info("sayHandler => " + m);
        User u = us.getUserInfo(m);
        m.setUser_name(u.getUsername());
        m.setHead_img(u.getFace());
        m.setContent(CommonUtils.htmlspecialchars(m.getContent()));
        if (null != m.getRoom_id()) {
            groupChatHandler(ctx, m);
            return;
        }
        Channel c = cs.getChannel(m);
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
