package com.pty.message;

import lombok.Data;

/**
 * @author : pety
 * @date : 2022/7/18 23:37
 */
@Data
public abstract class RpcMessage {

    //消息id
    private int messageId;

    //消息类型 0为rpc请求消息 1为rpc返回消息 2为rpc ping消息
    private int messageType;


    public static final int RPC_MESSAGE_TYPE_REQUEST = 0;
    public static final int RPC_MESSAGE_TYPE_RESPONSE = 1;
    public static final int RPC_MESSAGE_TYPE_PING = 2;

    public abstract int getMessageType();
}
