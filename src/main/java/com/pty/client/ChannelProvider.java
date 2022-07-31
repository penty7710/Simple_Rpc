package com.pty.client;

import com.pty.customizeProtocol.MessageCodec;
import com.pty.customizeProtocol.ProtocolFramDecoder;
import com.pty.handler.HeartClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 负责服务端的初始化和建立连接
 * @author : pety
 * @date : 2022/7/18 15:33
 */
@Slf4j
public class ChannelProvider {

    //用来映射ip套接字和channel的关系，方便下次获取 channel
    private static final Map<String,Channel> channelMap = new ConcurrentHashMap<>();

    private static Bootstrap bootstrap;

    public static NioEventLoopGroup group;


    static{
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        initChannel();
    }


    /**
     * 获取channel，如果没有就创建
     * @param inetSocketAddress
     * @return
     */
    public  static Channel getChannel(InetSocketAddress inetSocketAddress){
        String key = inetSocketAddress.toString();
        if(channelMap.containsKey(key)){
            Channel channel = channelMap.get(key);
            if(channel!=null && channel.isActive()){
                return channel;
            }else{
                channelMap.remove(key);
            }
        }
        try {
            //获取连接
            Channel channel = bootstrap.connect(inetSocketAddress).sync().channel();
            //将channel放入到map中，方便下次调用
            channelMap.put(key,channel);
            return channel;
        } catch (InterruptedException e) {
            log.error("客户端连接失败"+e.getMessage());
           return null;
        }
    }

    /**
     * 初始化 channel
     * @return
     */
    public static void initChannel(){
        //日志处理器
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        //消息编解码处理器
        MessageCodec messageCodec = new MessageCodec();
        //心跳处理器
        HeartClientHandler heartClientHandler = new HeartClientHandler();
        //消息处理器
        RpcResponseMessageHandler rpcResponseMessageHandler = new RpcResponseMessageHandler();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                //设置连接超时时间：10s，如果10秒未连接抛出异常
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //3秒未向服务器发送数据触发
                        socketChannel.pipeline().addLast(new IdleStateHandler(0,3,0, TimeUnit.SECONDS));
                        //自定义的帧解码器
                        socketChannel.pipeline().addLast(new ProtocolFramDecoder());
                        //自定义的编解码器
                        socketChannel.pipeline().addLast(messageCodec);
                        //日志打印
                        socketChannel.pipeline().addLast(loggingHandler);
                        //心跳处理器
                        socketChannel.pipeline().addLast(heartClientHandler);
                        //消息处理器
                        socketChannel.pipeline().addLast(rpcResponseMessageHandler);
                    }
                });
    }
}
