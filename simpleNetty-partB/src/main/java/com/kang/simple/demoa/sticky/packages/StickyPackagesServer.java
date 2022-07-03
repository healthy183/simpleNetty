package com.kang.simple.demoa.sticky.packages;

import com.kang.simple.common.util.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User:
 * Description:
 * Date: 2022-06-03
 * Time: 22:02
 */
public class StickyPackagesServer {

    static final Logger log = LoggerFactory.getLogger(StickyPackagesServer.class);
    public static void main(String[] args)  {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置每次读取数据容量
            //serverBootstrap.option(ChannelOption.SO_RCVBUF,16);
            serverBootstrap.group(boss,worker);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel >() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            log.info("connected {}",ctx.channel());
                            super.channelActive(ctx);
                        }
                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            log.info("disconnect {}",ctx.channel());
                            super.channelInactive(ctx);
                        }
                    });
                }
            });
                ChannelFuture channelFuture = serverBootstrap.bind(NetUtils.PORT);
                log.info("{} binding....",channelFuture.channel());
                channelFuture.sync();
                log.info("{} bound....",channelFuture.channel());
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("server error",e);
            }finally {
                boss.shutdownGracefully();
                worker.shutdownGracefully();
                log.info("stopped");
            }
    }
}
