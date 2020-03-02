# JavaChatDemo

<p align="center">
<a href="https://img.shields.io/badge/Java-v1.8.0-red?style=plastic"><img src="https://img.shields.io/badge/Java-v1.8.0-red?sytle=plastic"></a>
<a href="https://img.shields.io/badge/Platform-Manjaro 19.0.1 Kyria-blue?sytle=plastic"><img src="https://img.shields.io/badge/Platform-Manjaro 19.0.1 Kyria-blue?sytle=plastic"></a>
<a href="http://47.100.76.82"><img src="https://img.shields.io/badge/blog-yc-yellow?sytle=plastic"></a>
<a href=""><img src="https://img.shields.io/badge/contact me-yinchao.mail@foxmail.com-brightgreen?sytle=plastic"></a>
</p>


## Feature

- 使用Java NIO完成

- 自建聊天器协议(Transmission.java)完成数据内容的传输

## Usage

1. 在IDE中运行：

    1. 先运行Server.java开启服务端
    
    2. 再运行Client.java开启客户端
    
2. 在命令行运行：

    1. 在pom.xml中:
    
        ```
        <!--将程序主清单入口改为服务端程序-->
        <mainClass>main.Server</mainClass>
        ```
        
        ```shell script
        # 生成homework-1.0-RELEASE.jar文件(这是服务端jar包)
        mvn package
        
        # 改名为Server.jar
        mv target/homework-1.0-RELEASE.jar target/Server.jar
        ```
        
        同理，生成客户端jar包
        ```xml
        <!--将程序主清单入口改为客户端程序-->
        <mainClass>main.Client</mainClass>
        ```
        
        ```shell script
        mvn package
        mv target/homework-1.0-RELEASE.jar target/Client.jar
        ```
        
        当服务器和客户端jar包都生成好之后，使用java -jar 命令运行即可
        ```shell script
        nohup java -jar Server.jar > Server.log &
        nohup java -jar Client.jar
        ```
3. 程序说明
    - 当客户端log INFO 显示CONNECT_SUCCESSFUL时表示已和服务器建立连接，显示信息:
        
        set clientNumber: Integer 即为本地客户端编号
            
    - 通过先输入想要交谈的客户端编号和发送类型(1是文本字符串，2是文件)，进行数据传输
        
    - 如果是文件，则接收端需要在GUI界面填写文件保存路径

## 协议
<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="知识共享许可协议" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">JShell</span> 由 <a xmlns:cc="http://creativecommons.org/ns#" href="https://github.com/Chao-Yin-Github/JavaChatDemo" property="cc:attributionName" rel="cc:attributionURL">https://github.com/Chao-Yin-Github/JavaChatDemo</a> 采用 <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">知识共享 署名-非商业性使用-相同方式共享 4.0 国际 许可协议</a>进行许可。
