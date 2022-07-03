package com.kang.simple.demoa.eventLoop;

import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.TimeUnit;

/**
 * User:
 * Description:
 * Date: 2022-05-21
 * Time: 17:35
 */
public class EventLoopDemo {

    public static void main(String[] args) throws InterruptedException {
        //创建一个包含两个EventLoop的NioEventLoopGroup，包含两个线程
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        System.out.println(group.next());
        System.out.println(group.next());
        //普通任务
        group.next().execute(()->{
            System.out.println(Thread.currentThread().getName()+" hello");
        });
        //定时任务
        group.next().scheduleAtFixedRate(()->{
            System.out.println(Thread.currentThread().getName()+" hello schedule");
        },0,1, TimeUnit.SECONDS);

        Thread.sleep(2000);
        //优雅关闭
        group.shutdownGracefully();
        System.out.println("shutdownGracefully");
    }

}
