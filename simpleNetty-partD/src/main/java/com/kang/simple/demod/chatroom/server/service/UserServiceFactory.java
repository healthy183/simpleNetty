package com.kang.simple.demod.chatroom.server.service;

public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService  getUserService() {
        return userService;
    }
}
