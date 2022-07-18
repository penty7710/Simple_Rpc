package com.pty.proxy;

import com.pty.client.RpcClient;
import com.pty.message.RpcRequestMessage;
import com.pty.message.RpcResponseMessage;
import com.pty.util.MessageIdGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端代理对象
 * @author : pety
 * @date : 2022/7/19 0:31
 */
public class ClientProxy implements InvocationHandler {

    private RpcClient rpcClient;

    public ClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    /**
     * 创建代理对象
     */
    public Object getProxy(Object target) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequestMessage rpcRequestMessage = RpcRequestMessage.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterValue(args)
                .parameterTypes(method.getParameterTypes())
                .returnTypes(method.getReturnType())
                .build();
        rpcRequestMessage.setMessageId(MessageIdGenerator.nextId());
        RpcResponseMessage message = (RpcResponseMessage) rpcClient.sendRequest(rpcRequestMessage);
        return message;
    }
}
