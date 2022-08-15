package com.pty.server;

import com.pty.message.RpcRequestMessage;
import com.pty.message.RpcResponseMessage;
import com.pty.registry.impl.NacosServerRegistryImpl;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author : pety
 * @date : 2022/7/20 16:21
 */
@Slf4j
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    //服务注册
    private static NacosServerRegistryImpl serverRegistry;

    static {
        serverRegistry = new NacosServerRegistryImpl();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) throws Exception {
        log.info("服务端接收到请求:{}",msg);
        ChannelFuture future = null;
        try {
            String interfaceName = msg.getInterfaceName();
            //获取到对应的服务
            Object service = serverRegistry.getService(interfaceName);
            //反射调用方法得到返回值
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object result = method.invoke(service, msg.getParameterValue());
            ctx.writeAndFlush(RpcResponseMessage.success(result));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            log.error("远程调用出错："+e);
            ctx.writeAndFlush(RpcResponseMessage.fail(e));
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
