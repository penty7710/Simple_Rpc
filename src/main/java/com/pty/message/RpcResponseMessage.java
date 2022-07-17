package com.pty.message;

import lombok.Data;

import java.io.Serializable;

/**
 * RPC 响应消息
 * @author : pety
 * @date : 2022/7/16 16:01
 */
@Data
public class RpcResponseMessage<T>  implements Serializable {

    //状态码
    private Integer statusCode;


    //响应信息
    private String msg;


    //响应数据
    private T data;


    public static <T> RpcResponseMessage<T> success(T Data){
        RpcResponseMessage<T> message = new RpcResponseMessage<>();
        message.statusCode = ResponseResult.SUCCESS.getStatusCode();
        message.msg = ResponseResult.SUCCESS.getMsg();
        message.data = Data;
        return message;
    }

    public static <T> RpcResponseMessage<T> fail(ResponseResult result){
        RpcResponseMessage<T> message = new RpcResponseMessage<>();
        message.statusCode = result.getStatusCode();
        message.msg = result.getMsg();
        return message;
    }
}
