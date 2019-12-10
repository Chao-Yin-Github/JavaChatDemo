package NIO;

import constant.ChartConstant;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j(topic = "server")
public class Server {
    /**
     * 保存所有客户端的socketChannel信息
     */
    private ArrayList<SocketChannel> socketChannelArrayList = new ArrayList<>(1000);

    /**
     * 主选择器
     */
    private Selector selector;

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

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ChartConstant.IO_ERROR);
        }
    }

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

    private void handleInput(SelectionKey key) {
        log.info("in handleInput");
        // key 非法或已过期
        if (!key.isValid()) {
            return;
        }

        // 将刚刚连接的key的channel保存下来
        if (key.isAcceptable()) {
            // 获取到这个有消息响应的key的channel
            try {
                ServerSocketChannel serverSocketChannelOther = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverSocketChannelOther.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
                socketChannelArrayList.add(socketChannel);
                int size = socketChannelArrayList.size() - 1;
                log.info("size = {}.", size + 1);
                Transmission transmission = new Transmission();
                transmission.setContent(String.valueOf(size));
                transmission.setDestinationNumber(size);
                transmission.setTransmissionType(TransmissionType.UUID);
                transmission.setSourceNumber(-1);
                send(transmission);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(ChartConstant.IO_ERROR);
            }
            return;
        }

        if (key.isReadable()) {
            try {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                // 最坑的一点是这个地方,必须把ByteBuffer设置大一点,这样最大才10k
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024*10);
                int channelRead = socketChannel.read(byteBuffer);
                if (channelRead < 0) {
                    key.cancel();
                    socketChannel.close();
                    // todo 移除可能会有并发的问题,暂时还是把没有用到的socketChannel先保存下来
//                    socketChannelArrayList.remove(socketChannel);
//                    log.info("after remove one socketChannel, the size is {}", socketChannelArrayList.size());
                    log.info("one socketChannel is closed.");
                    return;
                }
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                log.info("bytes={}",bytes);
                Transmission transmission;
                transmission = (Transmission) SerializableUtil.toObject(bytes);
                log.info("invoke the method handleInput, the parameter is {}", transmission);
                send(transmission);
            } catch (IOException e) {
                e.printStackTrace();
                log.error(ChartConstant.IO_ERROR);
            }
        }
    }

    /**
     * @param echo 把客户端发送的传给群聊中的每一个人
     */
    private void sendStringToAll(String echo) {
        System.out.printf("ALL Client:%s", echo);
        byte[] bytes = (echo).getBytes();

        // 二层遍历
        socketChannelArrayList.forEach(item -> {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
                    byteBuffer.put(bytes);
                    byteBuffer.flip();
                    try {
                        item.write(byteBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    /**
     * 发送聊天器协议给客户端
     *
     * @param transmission 自定义聊天器协议
     */
    @SneakyThrows
    public void send(@NonNull Transmission transmission) {
        byte[] bytes = SerializableUtil.toBytes(transmission);
        ByteBuffer byteBuffer = ByteBuffer.allocate(Objects.requireNonNull(bytes).length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        // 协议中的clientNumber合法
        if (0 <= transmission.getDestinationNumber() && transmission.getDestinationNumber() < socketChannelArrayList.size()) {
            socketChannelArrayList.get(transmission.getDestinationNumber()).write(byteBuffer);
        } else {
            log.error("发送失败, the parameter in send is {}", transmission);
        }
    }
}
