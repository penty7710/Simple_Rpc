package com.pty.handler;

import com.pty.message.RpcPingMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端心跳处理
 * 3秒未发送数据会自动向服务器发送一个心跳包，防止断开连接
 * @author : pety
 * @date : 2022/7/18 16:19
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartClientHandler extends ChannelDuplexHandler {

    /**
     *  ChannelDuplexHandler 可以处理读写事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       if(evt instanceof IdleStateEvent){
           IdleStateEvent event = (IdleStateEvent) evt;
           //如果当前事件的状态是WRITER_IDLE：一段时间没有发送数据
           if(event.state() == IdleState.WRITER_IDLE){
               log.info("已经3秒没有写入数据");
               //发送心跳消息，如果发送失败则直接关闭连接：
               //ChannelFutureListener.CLOSE_ON_FAILURE：发送失败直接关闭连接
               //TODO
               ctx.writeAndFlush(new RpcPingMessage()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
           }
       }
    }
}
