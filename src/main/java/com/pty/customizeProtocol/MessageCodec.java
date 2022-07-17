package com.pty.customizeProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 自定义编、解码器
 * 继承 MessageToMessageCodec  可以使该编解码器共享，多个channel共用同一个编解码器
 * @author : pety
 * @date : 2022/7/16 17:28
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec {

    /**
     * 编码，给数据加上头部信息，同时将数据序列化，让其可以在网络中传输
     * 自定义传输协议
     *
     *    0   1   2   3   4         5                6            7   8   9   10  11    12    13  14   15  16
     *    +---+---+---+---+--------+----------------+-------------+---+---+---+---+-----+-----+-----+--+----+
     *    |  magic code  |version | serializerType | messageType | RequestId     | Fill |    length        |
     *    +-------------+--------+----------------+--------------+--------------+-------+-----------------+
     *    |                                                                                                |
     *    |                                         body                                                   |
     *    |                                                                                               |
     *    |                                        ... ...                                               |
     *    +----------------------------------------------------------------------------------------------+
     *  4B  magic code（魔数）   1B version（版本）   1B serializerType（序列化类型）    1B messageType（消息类型）
     *  4B  requestId（请求的Id） 1B fill(填充，无意义） 4B legth(消息长度)
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        ByteBuf buffer = channelHandlerContext.alloc().buffer();

        //4字节的魔数
        buffer.writeBytes("pety".getBytes(StandardCharsets.UTF_8));

        //1字节的版本
        buffer.writeByte(1);

        //1字节的序列化类型
        buffer.writeByte(0);


        //1字节的消息类型
        buffer.writeByte(0);

        //4字节的请求id TODO
        buffer.writeInt(1);

        buffer.writeByte(0XFF);


    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {

    }
}
