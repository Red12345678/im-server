package com.yk.frames;

import com.yk.utils.MessageHandlerUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.logging.Logger;

public class TextWebSocketFrameHandler extends
        SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static Logger logger=Logger.getLogger(TextWebSocketFrameHandler.class.getName());

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) { // (1)
        MessageHandlerUtils.sendMsgHandler(channels,ctx.channel(), msg);

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {  // (2)
        MessageHandlerUtils.loginHander(channels, ctx.channel());
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {  // (3)
        MessageHandlerUtils.logoutHandler(channels, ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        MessageHandlerUtils.logoutHandler(channels, ctx.channel());
        logger.info(cause.getMessage());
        ctx.close();
    }

}