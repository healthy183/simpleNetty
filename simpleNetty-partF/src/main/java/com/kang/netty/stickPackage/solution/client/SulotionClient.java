package com.kang.netty.stickPackage.solution.client;

import com.kang.netty.common.NetUtils;
import com.kang.netty.stickPackage.client.MyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * User:
 * Description:
 * Date: 2022-07-18
 * Time: 0:38
 */
public class SulotionClient {

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SulotionClientInitializer()); //自定义一个初始化类
            ChannelFuture channelFuture = bootstrap.connect
                    (NetUtils.LOCAL_HOST, NetUtils.PORT).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException ex){
            ex.printStackTrace();;
        }finally {
            group.shutdownGracefully();
        }

    }
}
