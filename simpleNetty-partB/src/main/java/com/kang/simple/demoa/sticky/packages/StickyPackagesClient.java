package com.kang.simple.demoa.sticky.packages;

import com.kang.simple.common.util.NetUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User:
 * Description:
 * Date: 2022-06-03
 * Time: 23:09
 */
public class StickyPackagesClient {

    static final Logger log = LoggerFactory.getLogger(StickyPackagesClient.class);

    public static void main(String[] args) {

        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    log.info("connected....");
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            log.info("sending....");
                            //每次发送16个字节
                            //服务端只收到一次数据量为160byte的数据，
                            /***
                            如果服务端通过serverBootstrap.option(ChannelOption.SO_RCVBUF,16)设置固定读取容量，
                            但是客户端每次发送数量不一则会导致服务端接受数据不完整，这就是半包现象
                             **/
                            for (int i = 0; i < 10; i++) {
                                ByteBuf buffer = ctx.alloc().buffer();
                                buffer.writeBytes(new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
                                ctx.writeAndFlush(buffer);
                            }
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(NetUtils.IP, NetUtils.PORT);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error",e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
