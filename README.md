
###序列化
目前实现了jdk和json的序列化机制，json序列化选用了阿里巴巴的fastjson框架。
采用了配置的方式来修改序列化，通过读取配置文件中设定的序列化的类型来调用对应的实例来执行序列化和反序列化。

### 获取channel
使用ConcurrentHashMap 将 目的ip、端口号和channel映射起来，==使用ConcurrentHashMap是保证线程安全==方便下次获取channel，如果没有相关映射或者channel已经关闭，那么就会重新创建一个channel，并建立新的映射。