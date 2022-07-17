package com.pty.config;



import com.pty.SerializerType.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author : pety
 * @date : 2022/7/17 23:41
 */
@Slf4j
public class SerializerConfig {

    private static String algorithm;

    private static String className = algorithm+"Serializer";

    private static List<String> list = new ArrayList<>();

    /**
     * 初始化序列化方法列表
     * 读取配置文件
     */
    static {
        list.add("Java");
        list.add("Json");

        try {
            InputStream in = SerializerConfig.class.getResourceAsStream("/application.properties");
            Properties properties = new Properties();
            properties.load(in);
            algorithm = properties.getProperty("serializer.algorithm");
        } catch (IOException e) {
            log.error("读取配置文件失败"+e.getMessage());
        }
    }


    /**
     * 返回选定的序列化类型。默认是Java的序列化方式
     * Java：0   Json：1
     * @return
     */
    public static int getSerializerType(){
        if(algorithm == null){
            return 0;
        }
        for(int i=0;i<list.size();i++){
            if(algorithm.equals(list.get(i))){
                return i;
            }
        }
        return 0;
    }


    public static Serializer getSerializer(){
        try {
            Class<?> clazz = Class.forName(className);
            Serializer serializer = (Serializer) clazz.newInstance();
            return serializer;
        } catch (Exception e) {
            log.error("创建实例失败");
            e.printStackTrace();
        }
        return null;
    }
}
