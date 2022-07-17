package com.kang.netty.stickPackage.solution.server;

import com.kang.netty.stickPackage.solution.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * User:
 * Description:
 * Date: 2022-07-18
 * Time: 0:19
 */
@Slf4j
public class SulotionServerHandler  extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();
        log.info("服务器接收到信息如下");
        log.info("长度=" + len);
        log.info("内容=" + new String(content, Charset.forName("utf-8")));
        log.info("服务器接收到消息包数量=" + (++this.count));

        //回复消息
        log.info("服务端开始回复消息------");
        String responseContent = UUID.randomUUID().toString();
        int responseLen = responseContent.getBytes("utf-8").length;
        byte[]  responseContent2 = responseContent.getBytes("utf-8");

        //构建一个协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(responseLen);
        messageProtocol.setContent(responseContent2);
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
