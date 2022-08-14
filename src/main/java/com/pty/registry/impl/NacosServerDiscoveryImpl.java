package com.pty.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.pty.config.LoadBalancerConfig;
import com.pty.loadBalancer.LoadBalancer;
import com.pty.registry.ServerDiscovery;
import com.pty.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 服务发现实现类
 * @author : pety
 * @date : 2022/8/14 0:55
 */
@Slf4j
public class NacosServerDiscoveryImpl implements ServerDiscovery {

    //负载均衡器
    private static LoadBalancer loadBalancer ;

    static {
        loadBalancer = LoadBalancerConfig.getLoadBalance();
    }

    /**
     * 客户端从Nacos获取可用服务
     * @param serviceName
     * @return
     */
    @Override
    public InetSocketAddress getService(String serviceName) {
        try {
            List<Instance> list = NacosUtil.getAllInstance(serviceName);
            if(list.size() == 0){
                throw new RuntimeException("找不到对应服务");
            }
            Instance instance = loadBalancer.getInstance(list);
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务实例失败："+e);
            throw new RuntimeException("获取服务实例失败");
        }
    }
}
