package com.pty.server;

import com.pty.Application;
import com.pty.annotation.RpcService;
import com.pty.customizeProtocol.MessageCodec;
import com.pty.customizeProtocol.ProtocolFramDecoder;
import com.pty.handler.HeartServerHandler;
import com.pty.handler.PingMessageHandler;
import com.pty.registry.ServerRegistry;
import com.pty.registry.impl.NacosServerRegistryImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Set;
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
    //服务注册
    private final ServerRegistry registry;

    //初始化
    public RpcServer(String host,int port){
        this.host=host;
        this.port = port;
        registry = new NacosServerRegistryImpl();
        new Application().autoRegistry(host, port);
    }

    /**
     * 开启服务
     */
    public void start(){
        //日志处理
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        //编解码器
        MessageCodec messageCodec = new MessageCodec();
        //心跳处理
        HeartServerHandler heartServerHandler = new HeartServerHandler();
        //rpc请求消息处理器
        RpcRequestMessageHandler rpcRequestMessageHandler = new RpcRequestMessageHandler();
        //rpc心跳消息处理
        PingMessageHandler pingMessageHandler = new PingMessageHandler();
        //bossGroup专门负责处理连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //workerGroup专门负责处理读写事务
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //设置全连接队列大小
                    .option(ChannelOption.SO_BACKLOG,256)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //5s没有收到客户端发送的消息就断开连接
                            ch.pipeline().addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
                            //自定义的帧解码器
                            ch.pipeline().addLast(new ProtocolFramDecoder());
                            ch.pipeline().addLast(messageCodec);
                            ch.pipeline().addLast(loggingHandler);
                            ch.pipeline().addLast(heartServerHandler);
                            ch.pipeline().addLast(pingMessageHandler);
                            ch.pipeline().addLast(rpcRequestMessageHandler);
                        }
                    });
            //阻塞直到建立连接
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().addListener((ChannelFutureListener) channelFuture1->{
                //关闭连接
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            log.info("服务端启动出错");
            e.printStackTrace();
        }
    }

    //todo 将服务注册到nacos 想法是弄一个注解，然后获取所有添加注解的服务，获取他们的类名称，注册到nacos
}
