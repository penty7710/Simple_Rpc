package com.pty.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.pty.config.LoadBalancerConfig;
import com.pty.loadBalancer.LoadBalancer;
import com.pty.registry.ServerRegistry;
import com.pty.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : pety
 * @date : 2022/8/14 0:56
 */
@Slf4j
public class NacosServerRegistryImpl implements ServerRegistry {

    //本地缓存
    private static final Map<String,Object> SERVICEMAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTEREDSERVICE = ConcurrentHashMap.newKeySet();


    /**
     * 将服务注册到nacos
     * @param serviceName
     * @param inetSocketAddress
     */
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerServer(serviceName,inetSocketAddress);
        } catch (NacosException e) {
            log.error("服务注册失败");
            e.printStackTrace();
        }
    }


    /**
     * 将服务注册到本地缓存
     * @param service
     * @param <T>
     */
    @Override
    public <T> void getService(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if(REGISTEREDSERVICE.contains(serviceName)){
            return;
        }
        REGISTEREDSERVICE.add(serviceName);

        //将接口名称注册到本地缓存
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for(Class temp : interfaces){
            String name = temp.getCanonicalName();
            SERVICEMAP.put(name,service);
        }
    }

    /**
     * 获取对应的服务
     * @param serviceName
     * @return
     */
    public Object getService(String serviceName){
        Object service = SERVICEMAP.get(serviceName);
        if(service == null){
            log.info("服务为找到");
            throw new RuntimeException();
        }
        return service;
    }
}
