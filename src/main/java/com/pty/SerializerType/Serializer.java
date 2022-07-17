package com.pty.SerializerType;

/**
 * 序列化、反序列化接口
 * @author : pety
 * @date : 2022/7/16 16:50
 */
public interface Serializer {

    //序列化，将java对象转为字节数组，可以在网络上进行传输
   <T> byte [] serialize(T object);


   //反序列化：将字节数组转为java对象
   <T> T deserialize (byte [] bytes,Class<T> clazz);
}
