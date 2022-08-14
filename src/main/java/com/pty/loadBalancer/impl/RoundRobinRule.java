package com.pty.loadBalancer.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.pty.loadBalancer.LoadBalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询的负载均衡算法
 * @author : pety
 * @date : 2022/8/14 10:43
 */
public class RoundRobinRule implements LoadBalancer {

    //计数器，使用原子类，防止发生线程安全
    private AtomicInteger atomicInteger = new AtomicInteger(-1);

    /**
     * 当计数器增加到Integer.MAX_VALUE时，让他重新从0开始计数，否则默认是变为Integer.MIN_VALUE
     * @return
     */
    private final int getAndIncrement(){
        int current;
        int next;
        do {
            current = atomicInteger.get();
            next = current >=Integer.MAX_VALUE?0:current+1;
            atomicInteger.compareAndSet(current,next);
        }while (!atomicInteger.compareAndSet(current,next));
        return next;
    }

    /**
     * 轮询取出服务实例
     * @param list
     * @return
     */
    @Override
    public Instance getInstance(List<Instance> list) {
        int index = getAndIncrement();
        index = index%list.size();
        return list.get(index);
    }
}
