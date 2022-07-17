package com.kang.netty.stickPackage.solution.client;

import com.kang.netty.stickPackage.client.MyClientHandler;
import com.kang.netty.stickPackage.solution.convent.SulotionMessageDecoder;
import com.kang.netty.stickPackage.solution.convent.SulotionMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * User:
 * Description:
 * Date: 2022-07-18
 * Time: 0:40
 */
public class SulotionClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SulotionMessageEncoder()); //加入编码器
        pipeline.addLast(new SulotionMessageDecoder()); //加入解码器
        pipeline.addLast(new SulotionClientHandler());
    }
}
