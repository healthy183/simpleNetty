package com.kang.simple.democ.futurePromise;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;

import java.util.concurrent.ExecutionException;

/**
 * User:
 * Description:
 * Date: 2022-05-28
 * Time: 17:42
 */
public class NettyFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();
        Future<Integer> future = eventLoop.submit(() -> 50);

        System.out.println("threadName:"+Thread.currentThread().getName());
        System.out.println("马上获取结果"+future.getNow());//无法结果则返回null
        //System.out.println("阻塞获取结果"+future.get());

        // NIO线程中异步获取结果
        future.addListener(future1 -> {
            System.out.println("threadName:"+Thread.currentThread().getName());
            System.out.println("getNow " + future1.getNow());
        });
        group.shutdownGracefully();
    }
}
