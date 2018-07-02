package com.yk.servers;

import com.yk.entities.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;

/**
 * [com.yk.servers desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/29
 */
public interface ChannelServer {
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
