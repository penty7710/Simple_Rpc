package com.pty.client;

import com.pty.message.RpcRequestMessage;
import com.pty.registry.ServerDiscovery;
import com.pty.registry.impl.NacosServerDiscoveryImpl;
import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 发送请求，同时获取响应
 * @author : pety
 * @date : 2022/7/18 23:10
 */

@Slf4j
public class RpcClient {

    //用来保存 请求id和
    public static final Map<Integer, Promise<Object>> PROMISE;

    //服务发现
    private final ServerDiscovery  serverDiscovery;

    static{
        PROMISE = new ConcurrentHashMap<>();
    }

    public RpcClient(){
        serverDiscovery = new NacosServerDiscoveryImpl();
    }


    /**
     * 向服务端发送请求
     * @param message
     */
    public Object sendRequest(RpcRequestMessage message){
        //通过服务发现找到服务信息
        InetSocketAddress address = serverDiscovery.getService(message.getInterfaceName());
        Channel channel = ChannelProvider.getChannel(address);
        DefaultPromise<Object> defaultPromise = new DefaultPromise<>(ChannelProvider.group.next());
        //如果channel不存在或者没有连接
        if(channel == null || !channel.isActive()){
            log.error("channel创建失败");
            close();
            return null;
        }
        //发送数据，发送数据完成后，将当前promise和消息id加入到集合中
        channel.writeAndFlush(message).addListener(future -> {
            if(future.isSuccess()){
                log.info("消息发送成功");
                PROMISE.put(message.getMessageId(),defaultPromise);
            }else{
                log.error("消息发送失败：{}",future.cause());
            }
        });

        //阻塞等待服务端的响应
        try {
            defaultPromise.await();
            //成功则返回服务端的响应结果
            if(defaultPromise.isSuccess()){
                return defaultPromise.getNow();
            }else{
                log.error("远程调用失败：{}"+defaultPromise.cause());
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    //关闭连接
    public void close(){
        ChannelProvider.group.shutdownGracefully();
    }
}
