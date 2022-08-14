package com.pty.loadBalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡算法
 * @author : pety
 * @date : 2022/8/14 10:35
 */
public interface LoadBalancer {

    Instance getInstance(List<Instance> list);

}
