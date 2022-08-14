package com.pty.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * nacos的工具类
 * @author : pety
 * @date : 2022/8/14 0:49
 */
@Slf4j
public class NacosUtil {

    //nacos服务的地址
    private static  final String SERVER_ADDR = "127.0.0.1:8848";

    private static  NamingService namingservice = null;

    //初始化
    static {
        try {
            namingservice = NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("nacos连接失败:"+e);
            e.printStackTrace();
        }
    }

    /**
     * 注册服务到nacos
     * @param serverName
     * @param address
     */
    public static void registerServer(String serverName, InetSocketAddress address) throws NacosException {
        namingservice.registerInstance(serverName,address.getHostName(),address.getPort());
    }


    /**
     * 返回当前服务名的所有实例
     * @param serverName
     * @return
     * @throws NacosException
     */
    public static List<Instance> getAllInstance(String serverName) throws NacosException {
        return  namingservice.getAllInstances(serverName);
    }
}
