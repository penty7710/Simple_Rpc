package com.pty.handler;

import com.pty.message.RpcPingMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : pety
 * @date : 2022/8/15 22:29
 */

@Slf4j
@ChannelHandler.Sharable
public class PingMessageHandler extends SimpleChannelInboundHandler<RpcPingMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcPingMessage msg) throws Exception {
        log.info("收到了心跳包:"+msg.getMessageType());
    }
}
