package com.kang.simple.demod.chatroom.server.handler;

import com.kang.simple.demod.chatroom.message.GroupJoinRequestMessage;
import com.kang.simple.demod.chatroom.message.GroupJoinResponseMessage;
import com.kang.simple.demod.chatroom.server.session.Group;
import com.kang.simple.demod.chatroom.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * User:
 * Description:
 * Date: 2022-07-02
 * Time: 14:19
 */
@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler  extends SimpleChannelInboundHandler<GroupJoinRequestMessage>  {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().joinMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getGroupName() + "群加入成功"));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getGroupName() + "群不存在"));
        }
    }
}
