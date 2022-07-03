package com.kang.simple.demoe.byteBuf;

import com.kang.simple.common.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * User:
 * Description:
 *  write开头方法会改变指针，set开头的就不会；
 * Date: 2022-05-29
 * Time: 20:16
 */
public class ByteBufApiWrite {

    public static void main(String[] args) {

        ByteBuf buffer = ByteBufAllocator.DEFAULT
                .buffer(16, 20);
        ByteBufUtil.log(buffer);

        byte[] bytes = new byte[]{1,2,3,4};
        buffer.writeBytes(bytes);
        ByteBufUtil.log(buffer);//read index:0 write index:4 capacity:16

        buffer.writeInt(5);
        ByteBufUtil.log(buffer);//read index:0 write index:8 capacity:16

        buffer.writeIntLE(6);
        ByteBufUtil.log(buffer);//read index:0 write index:12 capacity:16
        //容量以免自动扩容
        buffer.writeLong(7);
        ByteBufUtil.log(buffer);//read index:0 write index:20 capacity:20

    }
}
