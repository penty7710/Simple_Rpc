package com.pty.customizeProtocol;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 基于长度的帧解码器
 * 避免黏包和半包的问题
 * 封装了帧解码器，方便复用
 * @author : pety
 * @date : 2022/7/18 15:15
 */
public class ProtocolFramDecoder extends LengthFieldBasedFrameDecoder {

    public ProtocolFramDecoder(){
        this(1024,12,4,0,0);
    }

    /**
     *
     * @param maxFrameLength：帧的最大长度
     * @param lengthFieldOffset：字段长度的偏移量
     * @param lengthFieldLength：字段长度的长度
     * @param lengthAdjustment：字段长度后几个字节是真的数据
     * @param initialBytesToStrip：从第几个字节开始开始返回第一个数据
     */
    public ProtocolFramDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
