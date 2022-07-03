package com.kang.simple.democ.protocol.userdefined;

import com.kang.simple.democ.protocol.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * User:
 * Description:
 * Date: 2022-06-21
 * Time: 21:48
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {
    /**
     *
     * 编码器负责将附加信息与正文信息写入到ByteBuf中，
     * 其中附加信息总字节数最好为2的N次方不足需要补齐。
     * 正文内容如果为对象，需要通过序列化将其放入到ByteBuf中
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        //设置魔数 4个字节
        out.writeBytes(new byte[]{'N','Y','I','M'});
        //设置版本号1个字节
        out.writeByte(1);
        //设置序列化方式1个字节
        out.writeByte(1);
        // 设置指令类型 1个字节
        out.writeByte(msg.getMessageType());
        // 设置请求序号 4个字节
        out.writeInt(msg.getSequenceId());
        // 为了补齐为16个字节，填充1个字节的数据
        out.writeByte(0xff);
        // 获得序列化后的msg
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();
        // 获得并设置正文长度 长度用4个字节标识
        out.writeInt(bytes.length);
        // 设置消息正文
        out.writeBytes(bytes);
    }

    /**
     * 解码器负责将ByteBuf中的信息取出，并放入List中，该List用于将信息传递到下一个handler
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 获取魔数
        int magic = in.readInt();
        // 获取版本号
        byte version = in.readByte();
        // 获得序列化方式
        byte seqType = in.readByte();
        // 获得指令类型
        byte messageType = in.readByte();
        // 获得请求序号
        int sequenceId = in.readInt();
        // 移除补齐字节
        in.readByte();
        // 获得正文长度
        int length = in.readInt();
        // 获得正文
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message = (Message) ois.readObject();
        // 将信息放入List中，传递给下一个handler
        out.add(message);
        //打印获得信息正文
        // 打印获得的信息正文
        log.info("===========魔数===========");
        log.info(""+magic);
        log.info("===========版本号===========");
        log.info(""+version);
        log.info("===========序列化方法===========");
        log.info(""+seqType);
        log.info("===========指令类型===========");
        log.info(""+messageType);
        log.info("===========请求序号===========");
        log.info(""+sequenceId);
        log.info("===========正文长度===========");
        log.info(""+length);
        log.info("===========正文===========");
        log.info(""+message);
    }
}
