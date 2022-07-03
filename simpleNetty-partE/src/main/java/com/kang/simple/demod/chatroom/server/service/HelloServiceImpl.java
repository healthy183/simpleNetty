package com.kang.simple.demod.chatroom.server.service;

import lombok.extern.slf4j.Slf4j;

/**
 * User:
 * Description:
 * Date: 2022-07-03
 * Time: 14:51
 */
@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        //int i = 1 / 0;
        log.info("你好, " + name);
        return "你好, " + name;
    }
}
