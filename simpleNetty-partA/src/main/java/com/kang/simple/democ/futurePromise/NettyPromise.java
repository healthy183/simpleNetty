package com.kang.simple.democ.futurePromise;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * User:
 * Description:
 * Date: 2022-05-28
 * Time: 19:54
 */
public class NettyPromise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();

        DefaultPromise<Object> promise = new DefaultPromise<>(eventLoop);

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //自定义线程睡眠后设置成功后存放结果
            promise.setSuccess(50);
        }).start();

        System.out.println(
                "主线程"+Thread.currentThread().getName()+"中获取结果:"+
                promise.get());

        group.shutdownGracefully();
    }
}
