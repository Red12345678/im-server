package com.yk.servers;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class ImServer {

    private int port;
    private static Logger logger = Logger.getLogger(ImServer.class);
    public static Properties conf = new Properties();
    public ImServer(int port) {
        this.port = port;
    }
    public void imServerStart() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new WebsocketChatServerInitializer())  //(4)
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
            logger.info("WebsocketServer has started and listen in port " + port);
            ChannelFuture f = b.bind(port).sync(); // (7)
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            logger.info("ImServer stopped");
        }
    }

    public static void initConfFile(String file) {

        try {
            conf.load(new FileInputStream(new File(file)));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

    }

    public static void main(String[] args) throws Exception {
        int port = 7272;
        String file = "conf.properties";
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
            file = args[1];
        }
        initConfFile(file);
        new ImServer(port).imServerStart();

    }
}