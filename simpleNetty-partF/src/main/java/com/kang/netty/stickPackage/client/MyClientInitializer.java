package com.kang.netty.stickPackage.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * User:
 * Description:
 * Date: 2022-07-17
 * Time: 21:50
 */
public class MyClientInitializer  extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyClientHandler());
    }
}
