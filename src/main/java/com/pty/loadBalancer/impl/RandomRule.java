package com.pty.loadBalancer.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.pty.loadBalancer.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * 随机的负载均衡算法
 * @author : pety
 * @date : 2022/8/14 10:38
 */
public class RandomRule implements LoadBalancer {

    private final Random random = new Random();

    /**
     * 随机获取一个实例
     * 利用随机数，随机选择一个实例
     * @param list
     * @return
     */
    @Override
    public Instance getInstance(List<Instance> list) {
        int size = list.size();
        //生成数的范围是[0,size)
        int index = random.nextInt(size);
        return list.get(index);
    }
}
