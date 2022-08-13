package com.pty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端心跳处理机制
 * 超过5s没有收到对面的消息则认为对方已经宕机，关闭连接。
 * @author : pety
 * @date : 2022/8/13 23:51
 */

@Slf4j
@ChannelHandler.Sharable
public class HeartServerHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            //如果是读空闲事件
            if(event.state() == IdleState.READER_IDLE){
                log.info("已经超过5s没有收到客户端信息，关闭连接");
                ctx.close();
            }
        }
    }
}
