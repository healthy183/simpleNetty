package com.kang.simple.demoe.byteBuf;

import com.kang.simple.common.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.StandardCharsets;

/**
 * 直接内存特点
 * 直接内存创建和销毁成本大，读写效率高(少一次内存拷贝)，适合池化功能一起用
 * 直接内存对GC压力小，不受JVM垃圾回收管理，当请注意主动释放
 *
 * 池化说明
 * netty 4.1以后，除了安卓平台，其他都是池化内存；4.1之前都是非池化
 * 可以通过系统变量实现是否池化
 * -Dio.netty.allocator.type={unpooled|pooled}
 * 池化就是可重用ByteBuf，采用了jemalloc类似的内存分配算法提升分配效率
 *
 * 池化优势
 * 可以重用ByteBuf实例，更节约内存，减少内存溢出可能
 * 读写针分离，无需像 ByteBuffer一样切换读写模式
 * 可以自动扩容
 * 直接链式调用，使用更加流畅
 * 很多地方体验零拷贝，例如slice、duplicate、CompositeByteBuf
 */
public class ByteBufCreate {

    public static void main(String[] args) {
        ByteBuf buffer;
        //默认创建使用直接内存(directBuffer)的ByteBuf，默认容量是256，这里指定16
        //最大容量是Integer.MAX_VALUE，超过初始容量会扩容，超过最大容量则抛出IndexOutOfBoundsException
        //ByteBuf class:class io.netty.buffer.PooledUnsafeDirectByteBuf（池化直接内存）
        buffer = ByteBufAllocator.DEFAULT.buffer(16);
        ByteBufUtil.log(buffer);

        //或者以下方式创建直接内存(directBuffer)
        //ByteBuf class:class io.netty.buffer.PooledUnsafeDirectByteBuf（池化直接内存）
        buffer = ByteBufAllocator.DEFAULT.directBuffer(16);
        ByteBufUtil.log(buffer);

        //堆内存创建方法
        //ByteBuf class:class io.netty.buffer.PooledUnsafeHeapByteBuf（池化堆内存）
        buffer = ByteBufAllocator.DEFAULT.heapBuffer(16);
        ByteBufUtil.log(buffer);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append("a");
        }
        //扩容
        buffer.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8));
        ByteBufUtil.log(buffer);
    }
}
