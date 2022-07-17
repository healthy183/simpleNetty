package com.kang.netty.chatroom.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * User:
 * Description:
 * Date: 2022-07-17
 * Time: 16:56
 */
@Slf4j
public class GroupChatClientHandler extends SimpleChannelInboundHandler<String> {
    //从服务器拿到的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info(msg.trim());
    }
}
