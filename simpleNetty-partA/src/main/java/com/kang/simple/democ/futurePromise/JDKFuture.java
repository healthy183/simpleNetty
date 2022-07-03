package com.kang.simple.democ.futurePromise;

import java.util.concurrent.*;

/**
 * User:
 * Description:
 * Date: 2022-05-28
 * Time: 17:26
 */
public class JDKFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ThreadFactory threadFactory = r -> new Thread(r,"JdkFuture");

        ThreadPoolExecutor executor = new ThreadPoolExecutor
                (5, 10, 10,
                        TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
                        threadFactory);

        Future<Integer> future = executor.submit(() -> {
            System.out.println("threadName:"+Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(1);
            return 50;
        });
        System.out.println(future.get());
        executor.shutdown();
    }
}
