package com.pty.server;

import com.pty.message.RpcRequestMessage;
import com.pty.message.RpcResponseMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : pety
 * @date : 2022/7/20 16:21
 */
@Slf4j
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) throws Exception {
        log.info("服务端接收到请求:{}",msg);
        String interfaceName = msg.getInterfaceName();
        //通过调用对应的方法得到结果 TODO
        Object result = null;
        ChannelFuture future = ctx.writeAndFlush(RpcResponseMessage.success(result));
        future.addListener(ChannelFutureListener.CLOSE);
    }
}
