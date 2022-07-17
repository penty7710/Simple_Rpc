package com.pty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC 请求消息
 * @author : pety
 * @date : 2022/7/16 15:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequestMessage implements Serializable {

    //调用接口的全限定类名，服务器根据他找到对应的实现
    private String interfaceName;


    //调用的方法名
    private String methodName;


    //调用方法的请求参数
    private Object[] parameterValue;


    //调用方法请求的参数的类型
    private Class<?> [] parameterTypes;


    //方法的返回值参数类型
    private Class<?> [] returnTypes;

}
