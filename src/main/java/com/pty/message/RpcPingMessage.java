package com.pty.message;

import java.io.Serializable;

/**
 * ping消息
 * @author : pety
 * @date : 2022/7/18 16:28
 */
public class RpcPingMessage extends RpcMessage implements Serializable {


    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_PING;
    }
}
