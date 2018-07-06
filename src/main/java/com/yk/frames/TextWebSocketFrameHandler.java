package com.yk.frames;

import com.yk.utils.MessageUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.log4j.Logger;


public class TextWebSocketFrameHandler extends
        SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static Logger logger = Logger.getLogger(TextWebSocketFrameHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) { // (1)

        MessageUtils.msgHandler(ctx, msg);
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.info("exceptionCaught" + cause.getCause().getMessage());
        ctx.close();
    }

}
