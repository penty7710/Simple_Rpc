package com.pty.server;

import com.pty.customizeProtocol.MessageCodec;
import com.pty.customizeProtocol.ProtocolFramDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * RPC 服务端
 * @author : pety
 * @date : 2022/7/20 17:43
 */
@Slf4j
public class RpcServer {

    private final String host;
    private final int port;

    //初始化
    public RpcServer(String host,int port){
        this.host=host;
        this.port = port;
    }

    /**
     * 开启服务
     */
    public void start(){
        //日志处理
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        //编解码器
        MessageCodec messageCodec = new MessageCodec();
        //

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //5s没有收到客户端发送的消息就断开连接
                            ch.pipeline().addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
                            //自定义的帧解码器
                            ch.pipeline().addLast(new ProtocolFramDecoder());
                            ch.pipeline().addLast(messageCodec);
                            ch.pipeline().addLast(loggingHandler);
                        }
                    });
            bootstrap.bind(host,port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
