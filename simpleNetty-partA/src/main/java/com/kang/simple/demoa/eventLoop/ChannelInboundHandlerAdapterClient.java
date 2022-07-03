package com.kang.simple.demoa.eventLoop;
import com.kang.simple.common.util.NetUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * User:
 * Description:
 * Date: 2022-05-21
 * Time: 11:31
 */
public class ChannelInboundHandlerAdapterClient {

    public static void main(String[] args) throws InterruptedException, IOException {
        Bootstrap bootstrap = new Bootstrap();
        //创建NioEventLoopGroup 可以简单理解为“线程池+Selector"
        bootstrap.group(new NioEventLoopGroup());
        //选择客户端SocketChannel实现类，NioSocketChannel表示基于NIO的客户端实现
        bootstrap.channel(NioSocketChannel.class);
        //SocketChannel建立连接后，执行initChannel加载更加多ChannelHandler(处理器)；
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                //使用StringEncoder编码   String=>ByteBuf
                channel.pipeline().addLast(new StringEncoder());
            }
        });
        //执行连接，netty很多方法是异步，例如connect();
        ChannelFuture channelFuture = bootstrap.connect
                (new InetSocketAddress(NetUtils.LOCAL_HOST, NetUtils.PORT));
        channelFuture.sync();//同步等待connect()建立连接完毕
        //获取Channel对象，它即为通道抽象，可以进行数据读写操作
        Channel channel = channelFuture.channel();
        //写入消息并清空缓冲区
       while(true){
           System.out.print("请输入:");
           Scanner scanner = new Scanner(System.in);
           String read = scanner.next();
           String msg = "hello "+read;
           channel.writeAndFlush(msg);
           System.out.println(msg+" 发送成功");
       }
    }
}
