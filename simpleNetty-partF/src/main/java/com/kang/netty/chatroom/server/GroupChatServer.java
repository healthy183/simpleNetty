package com.kang.netty.chatroom.server;

import com.kang.netty.common.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * User:
 * Description:
 * Date: 2022-07-17
 * Time: 15:34
 */
@Slf4j
public class GroupChatServer {

    private int port;

    public GroupChatServer(int port){
        this.port = port;
    }

    public void run(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                    //SO_BACKLOG对应tcp/ip协议listen函数中的backlog参数，初始化队列的大小
                .option(ChannelOption.SO_BACKLOG, 128)
                    //心跳检测，默认两小时一次
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", new StringDecoder());
                        pipeline.addLast("encoder", new StringEncoder());
                        //加入自定义handler
                        pipeline.addLast(new GroupChatServerHandler());
                    }
                });
            log.info("netty 服务器启动");
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            //监听关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new GroupChatServer(NetUtils.PORT).run();
    }
}
