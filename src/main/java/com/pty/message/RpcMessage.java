package com.pty.message;

import lombok.Data;

/**
 * @author : pety
 * @date : 2022/7/18 23:37
 */
@Data
public abstract class RpcMessage {

    private int messageId;

    private int messageType;


    public static final int RPC_MESSAGE_TYPE_REQUEST = 0;
    public static final int RPC_MESSAGE_TYPE_RESPONSE = 1;
    public static final int RPC_MESSAGE_TYPE_PING = 2;

    public abstract int getMessageType();
}
