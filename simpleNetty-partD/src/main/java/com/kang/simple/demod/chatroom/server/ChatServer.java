package com.kang.simple.demod.chatroom.server;

import com.kang.simple.demod.chatroom.client.MessageCodecSharable;
import com.kang.simple.demod.chatroom.common.NetUtils;
import com.kang.simple.demod.chatroom.protocol.ProcotolFrameDecoder;
import com.kang.simple.demod.chatroom.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * User:
 * Description:
 * Date: 2022-06-29
 * Time: 23:08
 */
@Slf4j
public class ChatServer {

    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        LoginRequestMessageHandler LOGIN_HANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHAT_HANDLER = new ChatRequestMessageHandler();
        GroupCreateRequestMessageHandler GROUP_CREATE_HANDLER = new GroupCreateRequestMessageHandler();
        GroupChatRequestMessageHandler GROUP_CHAT_HANDLER = new GroupChatRequestMessageHandler();
        GroupJoinRequestMessageHandler  GROUP_JOIN_HANDLER = new GroupJoinRequestMessageHandler();
        GroupQuitRequestMessageHandler GROUP_QUIT = new  GroupQuitRequestMessageHandler();
        GroupMembersRequestMessageHandler GROUP_MEMBERS_HANDLER = new GroupMembersRequestMessageHandler();
        QuitHandler QUIT_HANDLER = new QuitHandler();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(loggingHandler);
                    ch.pipeline().addLast(messageCodecSharable);

                    // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
                    // 5s 内如果没有收到 channel 的数据，会触发一个 IdleState#READER_IDLE 事件
                   ch.pipeline().addLast(new IdleStateHandler(10, 0, 0));
                    // ChannelDuplexHandler 可以同时作为入站和出站处理器
                    ch.pipeline().addLast(new ChannelDuplexHandler() {
                        // 用来触发特殊事件
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
                            IdleStateEvent event = (IdleStateEvent) evt;
                            // 触发了读空闲事件
                            if (event.state() == IdleState.READER_IDLE) {
                                log.info("已经 5s 没有读到数据了");
                                ctx.channel().close();
                            }
                        }
                    });

                    ch.pipeline().addLast(LOGIN_HANDLER);//该handler处理登录请求
                    ch.pipeline().addLast(CHAT_HANDLER);//处理私聊
                    ch.pipeline().addLast(GROUP_CREATE_HANDLER);//创建群聊
                    ch.pipeline().addLast(GROUP_CHAT_HANDLER);//群聊发信息
                    ch.pipeline().addLast(GROUP_JOIN_HANDLER);//加入群聊
                    ch.pipeline().addLast(GROUP_QUIT);//退群
                    ch.pipeline().addLast(GROUP_MEMBERS_HANDLER);//查看群成员
                    ch.pipeline().addLast(QUIT_HANDLER);//查看群成员
                }
            });
            Channel channel = bootstrap.bind(NetUtils.PORT).sync().channel();
            log.info("服务启动成功");
            channel.closeFuture().sync();
        } catch (Exception e) {
           e.printStackTrace();;
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
