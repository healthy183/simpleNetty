package com.kang.simple.democ.protocol.userdefined;

import com.kang.simple.democ.protocol.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * User:
 * Description:
 * Date: 2022-06-21
 * Time: 21:48
 */
@Slf4j
public class MessageCodecTest {

    public static void main(String[] args) throws Exception {

        EmbeddedChannel channel = new EmbeddedChannel();
        //使用LengthFieldBasedFrameDecoder，避免粘包、半包
        channel.pipeline().addLast(new LengthFieldBasedFrameDecoder
                (1024,12,4,0,0));
        channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        //MessageCodec.encode方法将附加信息与正文写入到ByteBuf中，通过channel入站操作
        //入站时会调用decode方法进行解码
        channel.pipeline().addLast(new MessageCodec());
        LoginRequestMessage user = new LoginRequestMessage("Nyima", "123");

        // 测试编码与解码
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, user, byteBuf);
        channel.writeInbound(byteBuf);

    }
}
