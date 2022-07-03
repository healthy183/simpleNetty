package com.kang.simple.demoa.sticky.packages.solution.method4;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.nio.charset.StandardCharsets;

/**
 * User:
 * Description:
 * Date: 2022-06-06
 * Time: 23:27
 */
public class LengthFieldBasedFrameDecoderDemo {

    public static void main(String[] args) {
        //数据最大长度为1KB，长度标识前后各有1个字节的附加信息，长度标识长度为4个字节（int）
        LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder =
                new LengthFieldBasedFrameDecoder(1024, 1,
                        4, 1, 0);
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        EmbeddedChannel channel = new EmbeddedChannel
                (lengthFieldBasedFrameDecoder, loggingHandler);

        // 模拟客户端，写入数据
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "Hello");
        channel.writeInbound(buffer);
        send(buffer, "World");
        channel.writeInbound(buffer);
    }
    private static void send(ByteBuf buf, String msg) {
        // 得到数据的长度
        int length = msg.length();
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        // 将数据信息写入buf
        // 写入长度标识前的其他信息
        buf.writeByte(0xCA);
        // 写入数据长度标识
        buf.writeInt(length);
        // 写入长度标识后的其他信息
        buf.writeByte(0xFE);
        // 写入具体的数据
        buf.writeBytes(bytes);
    }
}
