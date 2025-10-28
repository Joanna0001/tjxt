###  JVM 远程调试的命令行参数
```
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
```
-agentlib:jdwp - 启用 Java Debug Wire Protocol (JDWP) 调试代理
transport=dt_socket - 使用 socket 传输方式进行调试通信
server=y - JVM 作为调试服务器等待调试器连接
suspend=n - JVM 启动时不暂停，直接运行（如果是 suspend=y 则会等待调试器连接后才运行）
address=*:5005 - 监听所有网络接口的 5005 端口，等待调试器连接