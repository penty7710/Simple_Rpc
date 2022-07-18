package com.pty.client;

import com.pty.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.CompletableFuture;

/**
 * responsemessage 处理器
 * @author : pety
 * @date : 2022/7/18 23:09
 */
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    /**
     * 处理接收到的响应消息
     * promise异步获取结果，其他线程会阻塞等待，直到promise设置完值。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        try {
            int requestId = msg.getMessageId();
            Promise<Object> promise = RpcClient.PROMISE.remove(requestId);
            if(promise !=null){
                if (msg.getStatusCode()==200) {
                    promise.setSuccess(msg.getData());
                }else{
                    promise.setFailure(new Exception(msg.getMsg()));
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
