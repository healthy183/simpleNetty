package com.kang.netty.stickPackage.server;
import com.kang.netty.common.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * User:
 * Description:
 * Date: 2022-07-17
 * Time: 21:38
 */
public class StickPackageServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer()); //自定义一个初始化类
                ChannelFuture  channelFuture = serverBootstrap.bind(NetUtils.PORT).sync();
                channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

