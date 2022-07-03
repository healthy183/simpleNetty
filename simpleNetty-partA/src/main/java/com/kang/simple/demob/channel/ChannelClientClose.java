package com.kang.simple.demob.channel;

import com.kang.simple.common.util.NetUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * User:
 * Description:
 * Date: 2022-05-21
 * Time: 21:50
 */
public class ChannelClientClose {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootStrap = new Bootstrap();
        bootStrap.group(group);
        bootStrap.channel(NioSocketChannel.class);
        bootStrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new StringEncoder());
            }
        });
        ChannelFuture channelFuture = bootStrap.connect(new InetSocketAddress(NetUtils.PORT));
        channelFuture.sync();
        Channel channel = channelFuture.channel();
        /*channel.writeAndFlush("hello world");
        System.out.println("发送成功");*/
       Scanner scanner = new Scanner(System.in);
        new Thread(()->{
            while(true){
                System.out.print("请输入");
                String msg = scanner.next();
                if("q".equals(msg)){
                    //关闭通道该方法是异步方法，真正关闭是在NIO线程中执行
                    channel.close();
                    break;
                }
                System.out.println("输入的msg是:"+msg);
                channel.writeAndFlush(msg);
            }
        },"inputThead").start();

        ChannelFuture closeFuture = channel.closeFuture();
        System.out.println("waiting close");

         /*如需要在close()后执行后置方法则需要以下两个方法
         **方法1  closeFuture.sync(); //同步等待NIO线程执行完close
         **/
        /*closeFuture.sync();
        //关闭后执行一些其他操作,可以保证执行的操作一定是在channel关闭以后执行
        System.out.println("关闭后执行一些其他操作");
        group.shutdownGracefully();*/

        /**方法2，添加监听器
         *channel.addListener()；
         **/
        closeFuture.addListener((ChannelFutureListener) future -> {
            System.out.println("关闭后执行一些其他操作");
            group.shutdownGracefully();
        });
    }
}
