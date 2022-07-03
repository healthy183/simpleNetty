package com.kang.simple.demoa.eventLoop;

import com.kang.simple.common.util.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

/** 
 * User:
 * Description:使用ChannelInboundHandlerAdapterClient服务端
 * 多开查看NioEventLoopGroup的boss和worker是否有效
 * 结论：channel跟EventLoop一旦绑定，EventLoop将一直负责处理该channel事件
 * Date: 2022-05-21
 * Time: 20:24
 */
public class EventLoopGroupServer {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group
                        (new NioEventLoopGroup(1),  //boss负责accept
                         new NioEventLoopGroup(2));  //worker负责读写
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
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
