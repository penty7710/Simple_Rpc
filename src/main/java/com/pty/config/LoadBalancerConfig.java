package com.pty.config;

import com.pty.loadBalancer.LoadBalancer;
import com.pty.loadBalancer.impl.RandomRule;
import com.pty.loadBalancer.impl.RoundRobinRule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author : pety
 * @date : 2022/8/14 11:11
 */
@Slf4j
public class LoadBalancerConfig {

    private static String loadBalancer;

    static{
        try {
            InputStream resource = LoadBalancerConfig.class.getResourceAsStream("/application.properties");
            Properties properties = new Properties();
            properties.load(resource);
            loadBalancer = properties.getProperty("loadbalancer");
        } catch (IOException e) {
            loadBalancer = "random";
            log.error("读取负载均衡配置失败："+e);
        }
    }

    /**
     * 读取配置文件获取对应的负载均衡器
     * @return
     */
    public static  LoadBalancer getLoadBalance(){
        LoadBalancer balancer = null;
        switch (loadBalancer){
            case  "random" :
                balancer =  new RandomRule();
            case "roundrobin" :
                balancer = new RoundRobinRule();
        }
        return balancer;
    }
}
