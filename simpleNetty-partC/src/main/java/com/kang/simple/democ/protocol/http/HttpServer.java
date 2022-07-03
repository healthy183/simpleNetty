package com.kang.simple.democ.protocol.http;

import com.kang.simple.democ.protocol.common.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * User:
 * Description:
 * Date: 2022-06-21
 * Time: 20:47
 */
public class HttpServer {

    static final Logger log = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                //使用HttpServerCodec作为服务器编码器、解码器
                ch.pipeline().addLast(new HttpServerCodec());
                //服务器只处理HTTPRequest
                ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                        log.info(msg.uri());
                        //
                        HttpVersion protocolVersion = msg.getProtocolVersion();
                        //获得完整响应，设置版本号、状态码
                        DefaultFullHttpResponse response = new
                                DefaultFullHttpResponse(protocolVersion, HttpResponseStatus.OK);
                        //设置响应内容
                        byte[] bytes = "<h1>Hello, World!</h1>".getBytes(StandardCharsets.UTF_8);
                        // 设置响应体长度，避免浏览器一直接收响应内容
                        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
                        // 设置响应体
                        response.content().writeBytes(bytes);
                        // 写回响应
                        ctx.writeAndFlush(response);
                    }
                });
            }
        });
        //http://localhost:9998
        serverBootstrap.bind(NetUtils.PORT);
    }
}
