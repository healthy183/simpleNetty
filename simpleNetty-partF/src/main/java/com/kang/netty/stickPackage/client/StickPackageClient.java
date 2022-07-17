package com.kang.netty.stickPackage.client;

import com.kang.netty.common.NetUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * User:
 * Description:
 * Date: 2022-07-17
 * Time: 21:38
 */
public class StickPackageClient {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new MyClientInitializer()); //自定义一个初始化类
            ChannelFuture channelFuture = bootstrap.connect(NetUtils.LOCAL_HOST, NetUtils.PORT).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }

    }
}
