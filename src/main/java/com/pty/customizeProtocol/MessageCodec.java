package com.pty.customizeProtocol;

import com.pty.SerializerType.Serializer;
import com.pty.config.SerializerConfig;
import com.pty.message.RpcRequestMessage;
import com.pty.message.RpcResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
     *    |                                                                                               |
     *    |                                         body                                                  |
     *    |                                                                                               |
     *    |                                        ... ...                                               |
     *    +----------------------------------------------------------------------------------------------+
     *  4B  magic code（魔数）   1B version（版本）   1B serializerType（序列化类型）    1B messageType（消息类型）
     *  4B  requestId（请求的Id） 1B fill(填充，无意义） 4B legth(消息长度)
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ByteBuf buffer = channelHandlerContext.alloc().buffer();
        //4字节的魔数
        buffer.writeBytes("pety".getBytes(StandardCharsets.UTF_8));

        //1字节的版本
        buffer.writeByte(1);

        //1字节的序列化类型
        //0：Java 1：Json 默认是Java
        buffer.writeByte(SerializerConfig.getSerializerType());

        //1字节的消息类型
        //0：请求消息  1：响应消息
        int messageType = o instanceof RpcRequestMessage?0:1;
        buffer.writeByte(messageType);

        //4字节的请求id
        //使用原子类进行递增
        buffer.writeInt(atomicInteger.getAndAdd(1));

        //1字节的填充字符
        buffer.writeByte(0XFF);

        //获取序列化器
        Serializer algorithm = SerializerConfig.getSerializer();
        byte[] bytes = algorithm.serialize(o);

        //4字节的消息长度
        buffer.writeInt(bytes.length);

        //写入消息
        buffer.writeBytes(bytes);
        list.add(buffer);
    }


    /**
     * 解码，按照自定义的传输协议，将数据转为java对象
     * 自定义传输协议
     *
     *    0   1   2   3   4         5                6            7   8   9   10  11    12    13  14   15  16
     *    +---+---+---+---+--------+----------------+-------------+---+---+---+---+-----+-----+-----+--+----+
     *    |  magic code  |version | serializerType | messageType | RequestId     | Fill |    length        |
     *    +-------------+--------+----------------+--------------+--------------+-------+-----------------+
     *    |                                                                                               |
     *    |                                         body                                                  |
     *    |                                                                                               |
     *    |                                        ... ...                                               |
     *    +----------------------------------------------------------------------------------------------+
     *  4B  magic code（魔数）   1B version（版本）   1B serializerType（序列化类型）    1B messageType（消息类型）
     *  4B  requestId（请求的Id） 1B fill(填充，无意义） 4B legth(消息长度)
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        ByteBuf in = (ByteBuf) o;
        //4字节的魔数
        int magicCode = in.readInt();
        
        //1字节的版本
        byte version = in.readByte();
        
        //1字节的序列化类型
        byte serializerType = in.readByte();
        
        //1字节的消息类型
        byte messageType = in.readByte();

        //4字节的请求id
        int requestId = in.readInt();

        //1字节的填充字段
        byte fill = in.readByte();

        //4字节的消息长度
        int length = in.readInt();

        //从ByteBuf里面读取数据
        byte[] bytes = new byte[length];
        in.readBytes(bytes,0,length);
        //获取消息的类型
        Class<?> message = messageType == 0?RpcRequestMessage.class: RpcResponseMessage.class;
        //将消息字节数组转变为java对象
        Object rpcmessage = SerializerConfig.getSerializer().deserialize(bytes, message);
        list.add(rpcmessage);
    }
}
