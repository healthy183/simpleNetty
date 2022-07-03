package com.kang.simple.demod.chatroom.server.handler;

import com.kang.simple.demod.chatroom.message.LoginRequestMessage;
import com.kang.simple.demod.chatroom.message.LoginResponseMessage;
import com.kang.simple.demod.chatroom.server.service.UserServiceFactory;
import com.kang.simple.demod.chatroom.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * User:
 * Description:
 * Date: 2022-06-30
 * Time: 20:22
 */
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
            String username = msg.getUsername();
            String password = msg.getPassword();
            // 校验登录信息
            boolean login = UserServiceFactory.getUserService().login(username, password);
            LoginResponseMessage message;
            if (login) {
                //绑定channel与username
                SessionFactory.getSession().bind(ctx.channel(), username);
                message = new LoginResponseMessage(true, "登录成功");
            } else {
                message = new LoginResponseMessage(false, "用户名或密码不正确");
            }
            ctx.writeAndFlush(message);
    }
}
