package com.kang.netty.stickPackage.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * User:
 * Description:
 * Date: 2022-07-17
 * Time: 21:45
 */
@Slf4j
public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int count;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];//这里决定了buffer[]的大小
        msg.readBytes(buffer);
        //将buffer转成字符串
        String message = new String(buffer, Charset.forName("utf-8"));
        log.info("服务器接收到数据 " + message);
        log.info("服务器接收到消息量=" + (++this.count));
        //服务器回送数据给客户端, 回送一个随机id ,
        ByteBuf responseByteBuf = Unpooled.copiedBuffer(UUID.randomUUID() + " ", Charset.forName("utf-8"));
        ctx.writeAndFlush(responseByteBuf);
    }
}
