package com.kang.simple.demod.handlerPipeline;

import com.kang.simple.common.util.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * User:
 * Description:
 * Date: 2022-05-28
 * Time: 20:03
 */
public class PipeLineServer {

    public static void main(String[] args) {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(new NioEventLoopGroup());
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //SocketChannel的pipeline中添加handler
                //pipeline的handler是带有head和tail节点的双向链表，实际结构为
                //head<-> handler1<-> ... <->handler4 <-> tail
                //inbound主要处理入站操作，一般为读操作
                //入站时，handler是从head向后调用
                socketChannel.pipeline().addLast("handler1",
                        new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(Thread.currentThread().getName()+" Channel Inbound  handler1");
                                //父类该方法内部会调用fireChannelRead
                                //将数据传输到下一个handler
                                super.channelRead(ctx, msg);
                            }
                        });
                socketChannel.pipeline().addLast("handler2", new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        System.out.println(Thread.currentThread().getName()+" Channel Inbound  handler2");
                        ByteBuf byteBuf = ctx.alloc().buffer().writeBytes("server...".getBytes(StandardCharsets.UTF_8));
                        socketChannel.writeAndFlush(byteBuf);
                        //调用下一个handler
                        super.channelRead(ctx, msg);
                    }
                });

                socketChannel.pipeline().addLast("handler3",new ChannelOutboundHandlerAdapter(){
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        System.out.println(Thread.currentThread().getName()+" Channel outbound  handler1");
                        super.write(ctx, msg, promise);
                    }
                });

                socketChannel.pipeline().addLast("handler4",new ChannelOutboundHandlerAdapter(){
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        System.out.println(Thread.currentThread().getName()+" Channel outbound  handler2");
                        super.write(ctx, msg, promise);
                    }
                });
            }
        });
        ChannelFuture channelFuture = serverBootstrap.bind(NetUtils.PORT);
    }
}
