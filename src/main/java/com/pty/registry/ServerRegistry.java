package com.pty.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 * @author : pety
 * @date : 2022/8/14 0:36
 */
public interface ServerRegistry {

    /**
     * 将服务的名称和地址注册到服务中心
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);


    /**
     * 将服务注册到本地缓存
     * @param service
     * @param <T>
     */
    <T> void getService(T service);

}
