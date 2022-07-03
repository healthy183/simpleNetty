package com.kang.simple.demoa.eventLoop;

import com.kang.simple.common.util.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * User:
 * Description: 自定义EventLoopGroup
 * 并不是所有任务都需要NioEventLoopGroup处理，特别是耗时的任务，可以自定义EventLoopGroup
 * 同时可以避免同一个NioEventLoop中其他Channel在较长时间内无法得到处理
 * Date: 2022-05-21
 * Time: 20:58
 */
public class EventLoopGroupUserDefinedServer {

    public static void main(String[] args) {
        //增加自定义非NioEventLoopGroup
        DefaultEventLoopGroup group = new DefaultEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group
                (new NioEventLoopGroup(1),  //boss负责accept
                        new NioEventLoopGroup(2));  //worker负责读写
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast("nioHandler",new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf byteBuf = (ByteBuf)msg;
                        System.out.println(Thread.currentThread().getName()
                                +" "+byteBuf.toString(StandardCharsets.UTF_8));
                        // 调用下一个handler
                        ctx.fireChannelRead(msg);
                    }
                });
                socketChannel.pipeline().addLast(group,"myHandler",new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf byteBuf = (ByteBuf)msg;
                        System.out.println(Thread.currentThread().getName()
                                +" "+byteBuf.toString(StandardCharsets.UTF_8));
                    }
                });
            }
        });
        serverBootstrap.bind(NetUtils.PORT);
        System.out.println("服务器启动成功");
    }
}
