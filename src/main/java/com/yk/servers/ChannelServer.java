package com.yk.servers;

import com.yk.entities.Message;
import com.yk.utils.CommonUtils;
import com.yk.utils.SerializeUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [com.yk.servers desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/29
 */
public interface ChannelServer {
    void roomJoin(ChannelHandlerContext ctx, Message m);

    Channel getRoomChannel(Message m);

    Channel roomChannel(ChannelHandlerContext ctx, Message m);

    void roomDelete(Message m);

    /**
     * [描述： add  channel relation with user to channelGroup when login]
     *
     * @param ctx
     * @param m
     * @author yangkun[Email:vectormail@163.com] 2018/7/2
     */
    void userJoin(ChannelHandlerContext ctx, Message m);

    /**
     * [描述： remove  channel relation with user to channelGroup when login]
     *
     * @param ctx
     * @param m
     * @author yangkun[Email:vectormail@163.com] 2018/7/2
     */
    void userLogout(ChannelHandlerContext ctx, Message m);


    /**
     * [描述： desc]
     *
     * @param uid int
     * @param from boolean
     * @return
     * @author yangkun[Email:vectormail@163.com] 2018/7/2
     */
    ChannelGroup getChannelGroup(int uid,String role,boolean from);


}
