package com.pty.registry;

import java.net.InetSocketAddress;

/**
 * 服务发现接口
 * @author : pety
 * @date : 2022/8/14 0:33
 */
public interface ServerDiscovery {

    //获取服务信息接口
    InetSocketAddress getService(String serviceName);
}
