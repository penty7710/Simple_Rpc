package com.pty.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息id生成器
 * @author : pety
 * @date : 2022/7/19 0:58
 */
public class MessageIdGenerator {

    private static AtomicInteger messageId = new AtomicInteger(0);

    public static int  nextId(){
        int id = messageId.getAndIncrement();
        return id;
    }
}
