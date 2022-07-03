package com.kang.simple.demoe.byteBuf;

import com.kang.simple.common.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * ByteBuf切片是【零拷贝】的体验之一，对原始ByteBuf切成多个ByteBuf
 *多个ByteBuf并没有发生内存复制，还是使用原始ByteBuf的内存，切后的ByteBuf维护的独立read\write指针
 *得到分片后的buffer，需要调用retain，使其内部引用计数加一
 * 避免原ByteBuf释放导致切片buffer无法使用
 * 注意：修改原ByteBuf中的值，也会影响切片后得到的byteBuf
 */
public class ByteBufSlice {

    public static void main(String[] args) {

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(16, 20);
        buffer.writeBytes(new byte[]{1,2,3,4,5,6,7,8,9,10});
        //切分
        ByteBuf slice1 = buffer.slice(0, 5);
        ByteBuf slice2 = buffer.slice(5, 5);

        //得到分片后的buffer，需要调用retain，使其内部引用计数加一
        //避免原ByteBuf释放导致切片buffer无法使用
        slice1.retain();
        slice2.retain();

        /*ByteBufUtil.log(slice1);
        ByteBufUtil.log(slice2);*/

        //更改原始buffer的值
        buffer.setByte(0,5);
        //slice1也变了
        ByteBufUtil.log(slice1);
    }

}
