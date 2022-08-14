package com.pty.config;



import com.pty.SerializerType.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 序列化配置
 * @author : pety
 * @date : 2022/7/17 23:41
 */
@Slf4j
public class SerializerConfig {

    private static String algorithm;

    private static final String PATH = "com.pty.SerializerType.";

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
            log.error("读取序列化配置文件失败"+e.getMessage());
        }
    }

    //由于static修饰的代码块，变量在类加载的时候是从上到下依次执行的。
    // 所以如果将这代码块放在静态代码块上面，会导致这里的 algorithm 为nul
    private static String className = PATH+algorithm+"Serializer";


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


    //利用反射创建出序列化器实例
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
