package com.pty;

import com.pty.annotation.RpcService;
import com.pty.registry.ServerRegistry;
import com.pty.registry.impl.NacosServerRegistryImpl;
import org.reflections.Reflections;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * 启动类，放在根目录下面，用于扫描下面所有的类
 * 主要是为了得到所有添加了RpcService注解的类
 * @author : pety
 * @date : 2022/8/16 23:51
 */
public class Application {

    private static ServerRegistry serverRegistry = new NacosServerRegistryImpl();

    public void autoRegistry(String host,int port){
        //得到当前类的全限定类名
        String canonicalName = this.getClass().getCanonicalName();
        //拼接字符串得到根目录
        int index = canonicalName.lastIndexOf(".");
        String path = canonicalName.substring(0,index);

        //利用Reflections 框架获取所有被 RpcService  注解修饰的类
        //path是扫描哪个包下面的所有类
        Reflections reflections = new Reflections(path);
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(RpcService.class);
        for(Class clazz : classSet){
            String name = clazz.getCanonicalName();
            //注册到nacos
            serverRegistry.register(name,new InetSocketAddress(host,port));
            //本地注册
            serverRegistry.localRegistry(clazz);
        }
    }
}
