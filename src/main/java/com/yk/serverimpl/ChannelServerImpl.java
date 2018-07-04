package com.yk.serverimpl;

import com.yk.entities.Message;
import com.yk.im.ImServer;
import com.yk.servers.ChannelServer;
import com.yk.utils.CommonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [com.yk.serverimpl desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/7/2
 */
public class ChannelServerImpl implements ChannelServer {
    private static Logger                    logger = Logger.getLogger(ChannelServer.class);
    private static Map<String, ChannelGroup> ucMap  = new ConcurrentHashMap<>();

    @Override
    public void roomDelete(Message m) { }

    /**
     * [描述： add  channel relation with user to channelGroup when login]
     *
     * @param ctx
     * @param m
     * @author yangkun[Email:vectormail@163.com] 2018/7/2
     */
    @Override
    public void userJoin(ChannelHandlerContext ctx, Message m) {
        String       key = CommonUtils.getKey(m.getUid(), m.getRole(), true);
        ChannelGroup cg  = ucMap.get(key);
        int          s   = Integer.valueOf(ImServer.conf.getProperty("im.client.num", "3"));
        if (cg != null) {
            if (cg.size() > s)
                ucMap.get(key).forEach(c -> { if (!c.isActive()) ucMap.get(key).remove(c); });
            if (ucMap.get(key).size() < s)
                ucMap.get(key).add(ctx.channel());
        } else {
            cg = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            cg.add(ctx.channel());
            ucMap.put(key, cg);
        }
        logger.info("ChannelServer userJoin current Channel Group is =>" + ucMap);
    }

    /**
     * [描述： remove  channel relation with user to channelGroup when login]
     *
     * @param ctx
     * @param m
     * @author yangkun[Email:vectormail@163.com] 2018/7/2
     */
    @Override
    public void userLogout(ChannelHandlerContext ctx, Message m) {
        String key = CommonUtils.getKey(m.getUid(), m.getRole(), true);
        logger.info("ChannelServer userLogout =>" + key);
        if (ucMap.get(key) != null)
            ucMap.get(key).remove(ctx.channel());
        logger.info("ChannelServer userLogout current Channel Group is =>" + ucMap);

    }


    /**
     * [描述： desc]
     *
     * @param uid
     * @param role
     * @param from
     * @return
     * @author yangkun[Email:vectormail@163.com] 2018/7/2
     */
    @Override
    public ChannelGroup getChannelGroup(int uid, String role, boolean from) {
        ChannelGroup ch;
        ch = ucMap.get(CommonUtils.getKey(uid, role, from));
        if(null != ch)
            ch.forEach(c -> {if (!c.isActive()) ch.remove(c);});
        logger.info("ChannelServer getchannelGroup=>" + ch);
        return ch;
    }
}
