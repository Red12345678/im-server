package com.yk.servers;

import com.yk.entities.Message;
import com.yk.utils.CommonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [com.yk.servers desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/29
 */
public class ChannelServer {
    private static Logger               logger   = Logger.getLogger(ChannelServer.class);
    private static Map<String, Channel> ucMap    = new ConcurrentHashMap<>();
    private static Map<String, Channel> roomcMap = new ConcurrentHashMap<>();

    public void roomJoin(ChannelHandlerContext ctx, Message m) {
        roomcMap.put(CommonUtils.md5(m.getRoom_id()), ctx.channel());
    }

    public Channel getRoomChannel(Message m) {
        return roomcMap.get(CommonUtils.md5(m.getRoom_id()));
    }

    public Channel roomChannel(ChannelHandlerContext ctx, Message m) {
        Channel c;
        c = getRoomChannel(m);
        if (null == c) roomJoin(ctx, m);
        c = getRoomChannel(m);
        return c;

    }

    public void roomDelete(Message m) {
        roomcMap.remove(CommonUtils.md5(m.getRoom_id()));
    }

    public void userJoin(ChannelHandlerContext ctx, Message m) {
        String key = CommonUtils.getUserChannelUK(m);
        logger.info("ChannelServer userJoin=>" + key);
        ucMap.put(key, ctx.channel());
    }

    public void userLogout(Message m) {
        String key = CommonUtils.getUserChannelUK(m);
        logger.info("ChannelServer userLogout =>" + key);
        ucMap.remove(key);

    }

    public Channel getChannel(Message m) {
        Channel ch;
        String  key = CommonUtils.getToUserChannelUK(m);
        logger.info("ChannelServer getChannel=>" + key);
        ch = ucMap.get(key);
        return ch;
    }

}
