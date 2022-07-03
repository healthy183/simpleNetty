package com.kang.simple.demo.hello;

import com.kang.simple.common.util.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
/**
 * User:
 * Description:
 * Date: 2022-05-18
 * Time: 23:24
 */
public class HelloServer {

    public static void main(String[] args) {
        //1,启动器，负责装配netty组件，启动服务器
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //2,创建NioEventLoopGroup 可以简单理解为“线程池+Selector"
        serverBootstrap.group(new NioEventLoopGroup());
        //3,选择服务器的SocketChannel实现
        serverBootstrap.channel(NioServerSocketChannel.class);
        /**
         * 4,child负责处理读写，该方法决定了child执行哪些操作
         * ChannelInitializer处理器(仅执行一次)
         * 他的作用是待客户端SocketChannel建立连接后，执行initChannel()以便添加更多处理器
         */
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel){
                //5,SocketChannel的处理器，使用StringDecoder解码  ByteBuf=>String
                nioSocketChannel.pipeline().addLast(new StringDecoder());
                //6,SocketChannel的业务处理，使用上一个处理器的结果
                nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                        System.out.println(msg);
                    }
                });
            }
        });
        //7,ServerSocketChannel绑定端口
        serverBootstrap.bind(NetUtils.PORT);
        System.out.println("服务器启动成功");
    }
}
