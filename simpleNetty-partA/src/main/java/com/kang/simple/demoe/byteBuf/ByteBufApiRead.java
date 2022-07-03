package com.kang.simple.demoe.byteBuf;

import com.kang.simple.common.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * User:
 * Description:read开头方法会改变指针，get开头的就不会；
 * Date: 2022-05-29
 * Time: 23:21
 */
public class ByteBufApiRead {

    public static void main(String[] args) {

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16,20);
        byte[] bytes = {1, 2, 3, 4};
        buffer.writeBytes(bytes);
        buffer.writeInt(5);

        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        ByteBufUtil.log(buffer);//read index:4 write index:8 capacity:16

        buffer.markReaderIndex();//标记当前读指针
        System.out.println(buffer.readInt());
        ByteBufUtil.log(buffer);//read index:8 write index:8 capacity:16


        buffer.resetReaderIndex();//恢复到之前标记读指针
        ByteBufUtil.log(buffer);//read index:4 write index:8 capacity:16

    }
}
