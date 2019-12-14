package NIO;

import constant.ChartConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import util.ChartUtil;
import util.SerializableUtil;
import util.Transmission;
import util.TransmissionType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

/**
 * @author yinchao
 * @Date 2019/11/27 19:28
 */
@Slf4j(topic = "聊天器服务端")
public class Server {
    /**
     * 保存所有客户端的socketChannel信息
     */
    private ArrayList<SocketChannel> socketChannelArrayList = new ArrayList<>(1000);

    /**
     * 主选择器
     */
    private Selector selector;

    /**
     * 聊天的工具类
     */
    private ChartUtil chartUtil;

    public static void main(String[] args) {
        Server server = new Server();
        server.initial();
        server.start();
    }

    /**
     * 初始化
     */
    private void initial() {
        try {
            // 开启选择器
            selector = Selector.open();

            /*
             * TCP服务端连接通道
             */
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 配置 非阻塞
            serverSocketChannel.configureBlocking(false);

            // 配置监听端口
            serverSocketChannel.socket().bind(new InetSocketAddress(ChartConstant.PORT), 1024);

            // 配置触发条件:当有新的连接时进行注册
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, "server");

            chartUtil = new ChartUtil();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ChartConstant.IO_ERROR);
        }
    }

    /**
     * 主要逻辑部分
     */
    @SneakyThrows
    private void start() {
        while (true) {
            // 阻塞，等待有期望新建的连接
            selector.select(1000);
            // 有新建连接时
            Set<SelectionKey> keySet = selector.selectedKeys();
            if (keySet.isEmpty()) {
                continue;
            }
            // 一层遍历有数据交换的key，但是这个是部分有数据交换的key，而不是所有保存下来的所有的key
            keySet.forEach(this::handleInput);
            // 一定要clear不然会报错
            keySet.clear();
        }
    }

    /**
     * 处理一个有数据交换的key
     *
     * @param key 有数据交换的key
     */
    @SneakyThrows
    private void handleInput(SelectionKey key) {
//        log.info("in handleInput");
        // key 非法或已过期
        if (!key.isValid()) {
            return;
        }

        // 将刚刚连接的key的channel保存下来
        if (key.isAcceptable()) {
            // 获取到这个有消息响应的key的channel
            ServerSocketChannel serverSocketChannelOther = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannelOther.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            socketChannelArrayList.add(socketChannel);
            int size = socketChannelArrayList.size() - 1;
            Transmission transmission = new Transmission();
            transmission.setContent(String.valueOf(size));
            transmission.setDestinationNumber(size);
            transmission.setTransmissionType(TransmissionType.UUID);
            transmission.setSourceNumber(-1);
            send(transmission);
            return;
        }

        if (key.isReadable()) {
            byte[] bytes = chartUtil.readBytesFromChannel(key);
            if (bytes.length == 0) {
                log.info("one socketChannel is closed.");
                return;
            }
            Transmission transmission = (Transmission) SerializableUtil.toObject(bytes);
            if (!TransmissionType.FILE.equals(transmission.getTransmissionType())) {
                log.info("invoke the method handleInput, the parameter is {}", transmission);
            }
            send(transmission);
        }
    }

    /**
     * 发送聊天器协议给客户端
     *
     * @param transmission 自定义聊天器协议
     */
    @SneakyThrows
    public void send(Transmission transmission) {
        if (transmission == null) {
            sendErrorMessage(transmission);
            return;
        }
        byte[] bytes = SerializableUtil.toBytes(transmission);
        ByteBuffer byteBuffer = ByteBuffer.allocate(Objects.requireNonNull(bytes).length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        // 协议中的clientNumber合法
        if (transmission.getDestinationNumber() >= 0 && transmission.getDestinationNumber() < socketChannelArrayList.size() || transmission.getContent() == null) {
            try {
                while (byteBuffer.hasRemaining()) {
                    log.info("remaining length = {}", byteBuffer.remaining());
                    socketChannelArrayList.get(transmission.getDestinationNumber()).write(byteBuffer);
                }
            } catch (Exception e) {
                sendErrorMessage(transmission);
            }
        } else {
            sendErrorMessage(transmission);
        }
    }

    /**
     * @param transmission 一个传输可能有问题的协议, 对它进行处理
     */
    @SneakyThrows
    public void sendErrorMessage(Transmission transmission) {
        if (transmission == null) {
            log.error("transmission is null");
            return;
        }
        log.error("error, the parameter in send is {}", transmission);
        Transmission errorTransmission = new Transmission(ChartConstant.SENDING_FAILURE, TransmissionType.MESSAGE, transmission.getSourceNumber(), -1);
        byte[] bytes = SerializableUtil.toBytes(errorTransmission);
        ByteBuffer byteBuffer = ByteBuffer.allocate(Objects.requireNonNull(bytes).length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        try {
            while (byteBuffer.hasRemaining()) {
                log.info("remaining length = {}", byteBuffer.remaining());
                socketChannelArrayList.get(errorTransmission.getDestinationNumber()).write(byteBuffer);
            }
        } catch (Exception ex) {
            log.error("错误信息发送失败");
        }
    }

}
