package com.kang.netty.rpc.service;

import lombok.extern.slf4j.Slf4j;

/**
 * User:
 * Description:
 * Date: 2022-07-18
 * Time: 1:01
 */
@Slf4j
public class HelloServiceImpl implements HelloService {

    private static int count = 0;

    @Override
    public String hello(String msg) {
        log.info("收到客户端消息=" + msg);
        //根据mes 返回不同的结果
        if(msg != null) {
            return "你好客户端, 我已经收到你的消息。消息为：[" + msg + "] ，第" + (++count) + " 次 \n";
        } else {
            return "你好客户端, 我已经收到你的消息 ";
        }
    }
}
