package com.yk.utils;

import com.alibaba.fastjson.JSONObject;
import com.yk.entities.Message;
import com.yk.entities.RoomUser;
import com.yk.entities.User;
import com.yk.serverimpl.ChannelServerImpl;
import com.yk.serverimpl.MessageServerImpl;
import com.yk.serverimpl.RoomServerImpl;
import com.yk.serverimpl.UserServerImpl;
import com.yk.servers.ChannelServer;
import com.yk.servers.MessageServer;
import com.yk.servers.RoomServer;
import com.yk.servers.UserServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.log4j.Logger;

import java.util.List;


/**
 * [com.yk.utils desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/4/16
 */
public class MessageUtils {
    private static Logger        logger = Logger.getLogger(MessageUtils.class);
    private static UserServer    us     = new UserServerImpl();
    private static MessageServer ms     = new MessageServerImpl();
    private static ChannelServer cs     = new ChannelServerImpl();
    private static RoomServer    rs     = new RoomServerImpl();

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
        logger.info("msgHandler => " + m);
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
            case "group":
                groupChatHandler(ctx, m);
                break;
            case "ping":
                pingHandler(ctx, m);
                break;
        }
    }

    /**
     * [描述： Group chat]
     *
     * @param ctx
     * @param m
     * @author yangkun[Email:vectormail@163.com] 2018/7/2
     */
    public static void groupChatHandler(ChannelHandlerContext ctx, Message m) {
        if (null == m.getRoom_id() || m.getRoom_id().equals("")) return;
        List<RoomUser> l = rs.findRoomById(m.getRoom_id());
        l.forEach(r -> send(m,
                cs.getChannelGroup(m.getUid(), m.getRole(), true),
                cs.getChannelGroup(r.getUserId(), r.getRole(), false)));
        logger.info("groupChatHandler => " + m);
    }

    /**
     * [描述： logout]
     *
     * @param ctx
     * @param m
     * @author yangkun[Email:vectormail@163.com] 2018/7/2
     */
    public static void logoutHandler(ChannelHandlerContext ctx, Message m) {
        cs.userLogout(ctx, m);
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
        ChannelGroup cgTo   = cs.getChannelGroup(m.getTouid(), m.getRole(), false);
        ChannelGroup cgFrom = cs.getChannelGroup(m.getUid(), m.getRole(), true);
        send(m, cgFrom, cgTo);

    }


    /**
     * [描述： start send messages]
     *
     * @param m
     * @param cgFrom
     * @param cgTo
     * @author yangkun[Email:vectormail@163.com] 2018/7/2
     */
    public static void send(Message m, ChannelGroup cgFrom, ChannelGroup cgTo) {
        User u = us.getUserInfo(m);
        m.setUser_name(u.getUsername());
        m.setHead_img(u.getFace());
        m.setContent(CommonUtils.htmlspecialchars(m.getContent()));
        logger.info("send msg => " + m);
        if (null == cgTo || cgTo.size() <= 0) {
            m.setStatus(MessageStatus.FAIL);
            saveMsgToDB(m);
            cgFrom.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(m)));
            return;
        }
        m.setStatus(MessageStatus.SUCCESS);
        cgTo.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(m)));
        cgFrom.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(m)));
        saveMsgToDB(m);

    }

    public static void pingHandler(ChannelHandlerContext ctx, Message m) { }


}
