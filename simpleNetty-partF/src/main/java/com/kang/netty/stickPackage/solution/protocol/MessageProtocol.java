package com.kang.netty.stickPackage.solution.protocol;

import lombok.Data;

/**
 * User: 协议包
 * Description:
 * Date: 2022-07-17
 * Time: 22:23
 * @author healthy
 */
@Data
public class MessageProtocol {

    private int len; //关键
    private byte[] content;
}
