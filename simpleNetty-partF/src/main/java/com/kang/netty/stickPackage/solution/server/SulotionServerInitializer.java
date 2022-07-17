package com.kang.netty.stickPackage.solution.server;

import com.kang.netty.stickPackage.server.MyServerHandler;
import com.kang.netty.stickPackage.solution.convent.SulotionMessageDecoder;
import com.kang.netty.stickPackage.solution.convent.SulotionMessageEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * User:
 * Description:
 * Date: 2022-07-17
 * Time: 22:29
 */
public class SulotionServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SulotionMessageDecoder());//解码器
        pipeline.addLast(new SulotionMessageEncoder());//编码器
        pipeline.addLast(new SulotionServerHandler());
    }
}
