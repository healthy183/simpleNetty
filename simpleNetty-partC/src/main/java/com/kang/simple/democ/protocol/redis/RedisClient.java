package com.kang.simple.democ.protocol.redis;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * User:
 * Description: 模拟代码，本机没有安装redis所有无法测试
 * 运行完以后，在redis的客户端输入：
 *  redis 127.0.0.1:6379 >get name
 *  "Nyima"  //证明设置成功
 * Date: 2022-06-21
 * Time: 20:23
 */
public class RedisClient {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>(){
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        // 回车与换行符
                        final byte[] LINE = {'\r','\n'};
                        // 获得ByteBuf
                        ByteBuf buffer = ctx.alloc().buffer();
                        // 连接建立后，向Redis中发送一条指令，注意添加回车与换行
                        // set name Nyima
                        buffer.writeBytes("*3".getBytes());
                        buffer.writeBytes(LINE);
                        buffer.writeBytes("$3".getBytes());
                        buffer.writeBytes(LINE);
                        buffer.writeBytes("set".getBytes());
                        buffer.writeBytes(LINE);
                        buffer.writeBytes("$4".getBytes());
                        buffer.writeBytes(LINE);
                        buffer.writeBytes("name".getBytes());
                        buffer.writeBytes(LINE);
                        buffer.writeBytes("$5".getBytes());
                        buffer.writeBytes(LINE);
                        buffer.writeBytes("Nyima".getBytes());
                        buffer.writeBytes(LINE);
                        ctx.writeAndFlush(buffer);
                    }
                });
            }
        });
        ChannelFuture channelFuture =
                bootstrap.connect(new InetSocketAddress("localhost", 6379));
        try {
            channelFuture.sync();
            // 关闭channel
            channelFuture.channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 关闭group
            group.shutdownGracefully();
        }

    }
}
