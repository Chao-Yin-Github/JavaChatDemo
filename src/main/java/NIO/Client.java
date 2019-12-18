package NIO;

import constant.ChartConstant;
import gui.GUI;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import util.*;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

/**
 * @author yinchao
 * @Date 2019/11/27 20:35
 */
@Slf4j(topic = "Client")
public class Client {
    /**
     * 本机client编号
     */
    private int thisClientNumber = -1;

    /**
     * 客户端多路选择器
     */
    private Selector selector;

    /**
     * 客户端和服务端连接channel
     */
    private SocketChannel socketChannel;

    /**
     * 聊天工具类
     */
    private ChartUtil chartUtil;

    public static void main(String[] args) {
        Client client = new Client();
        client.initial();
        client.inputThread();
        client.start();
    }

    private void inputThread() {
        new Thread(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("输入对方的id");
                String id = scanner.nextLine();
                System.out.println("文本按1,传输文件按2");
                int dataType;
                int clientNumber;
                try {
                    dataType = Integer.parseInt(scanner.nextLine());
                    clientNumber = Integer.parseInt(id);
                } catch (NumberFormatException e) {
                    log.error("数据格式错误");
                    continue;
                }
                Transmission transmission;
                if (dataType == ChartConstant.DATA_STRING) {
                    log.info("发送字符串");
                    String in = scanner.nextLine();
                    System.out.println("我:" + in);
                    transmission = new Transmission(in, TransmissionType.STRING, clientNumber, thisClientNumber);
                    sendToSocketChannel(transmission);
                } else if (dataType == ChartConstant.DATA_FILE) {
                    log.info("请输出本地文件路径");
                    String in = scanner.nextLine();
                    transmission = new Transmission(FileAndStringTransformUtil.fileToString(in), TransmissionType.FILE, clientNumber, thisClientNumber);
                    if (transmission.getContent() == null || transmission.getContent().isEmpty()) {
                        log.info("transmission content is null, interrupt");
                        continue;
                    }
//                    log.info("文件内容{}", transmission);
                    sendToSocketChannel(transmission);
                } else {
                    System.out.println("操作终止");
                }
            }
        }).start();
    }

    /**
     * 把transmission对象传到socketChannel里面
     */
    private void sendToSocketChannel(Transmission transmission) {
        byte[] bytes = SerializableUtil.toBytes(transmission);
//        log.info("sending bytes = {}, bytes length is {}", bytes, Objects.requireNonNull(bytes).length);
        int byteLength = Objects.requireNonNull(bytes).length;
//        log.info("sending bytes length is {}", byteLength);
        ByteBuffer byteBuffer = ByteBuffer.allocate(Objects.requireNonNull(bytes).length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
//        ByteBuffer[] byteBufferArray = new ByteBuffer[10];
//        int offset = 0;
//        for (int i = 0; i < byteBufferArray.length -1; i++) {
//            byteBufferArray[i] = ByteBuffer.allocate(byteLength/10);
//            byteBufferArray[i].put(bytes, offset, byteLength / 10);
//            offset += byteLength / 10;
//            byteBufferArray[i].flip();
//        }
//        log.info("offset = {}, byteLength = {}, allocate capacity = {}",offset,byteLength,byteLength/10+byteLength%10);
//        byteBufferArray[9] = ByteBuffer.allocate(byteLength-offset);
//        byteBufferArray[9].put(bytes,offset,byteBufferArray[9].capacity());
//        offset+= byteBufferArray[9].capacity();
//        log.info("offset = {}, bytesLength = {}",offset,byteLength);
        try {
//            socketChannel.write(byteBufferArray);
//            socketChannel.shutdownOutput();
            while (byteBuffer.hasRemaining()) {
//                log.info("sent:{}, remain: {}.",byteBuffer.capacity()-byteBuffer.remaining(),byteBuffer.remaining());
                socketChannel.write(byteBuffer);
            }
//            log.info("sent:{}, remain: {}.", byteBuffer.capacity() - byteBuffer.remaining(), byteBuffer.remaining());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(ChartConstant.IO_ERROR);
        }
    }

    @SneakyThrows
    private void initial() {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        chartUtil = new ChartUtil();
    }

    @SneakyThrows
    private void start() {
//        if (socketChannel.connect(new InetSocketAddress("127.0.0.1",ChartConstant.PORT))) {
        if (socketChannel.connect(ChartConstant.inetSocketAddress)) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            log.info("OP_READ");
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            log.info("CONNECT_SUCCESSFUL");
        }

        while (true) {
            selector.select(1000);
            Set<SelectionKey> keySet = selector.selectedKeys();
            keySet.forEach(this::handleInput);
            keySet.clear();
        }
    }

    private void handleInput(SelectionKey key) {
        if (!key.isValid()) {
            log.error("key {} is invalid", key);
            return;
        }
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (socketChannel.finishConnect()) {
                    socketChannel.register(selector, SelectionKey.OP_READ, "client");
                } else {
                    log.error("key not finishing connect .");
                }
            }
            if (key.isReadable()) {
                byte[] bytes = chartUtil.readBytesFromChannel(key);
                if (bytes.length == 0) {
                    log.error("bytes not available");
                    return;
                }
                Transmission transmission = (Transmission) SerializableUtil.toObject(bytes);
                if (TransmissionType.STRING.equals(Objects.requireNonNull(transmission).getTransmissionType())) {
                    System.out.println("[client " + transmission.getSourceNumber() + "]: " + transmission.getContent());
                } else if (TransmissionType.FILE.equals(transmission.getTransmissionType())) {
//                        log.info("handle file {}",transmission.getContent());
                    new Thread(() -> {
                        log.info("请输入文件保存路径");
                        GUI gui = new GUI();
                        gui.init(transmission);
                    }).start();
                } else if (TransmissionType.MESSAGE.equals(transmission.getTransmissionType())) {
                    log.info("server message:{}", transmission.getContent());
                } else if (TransmissionType.UUID.equals(transmission.getTransmissionType())) {
                    thisClientNumber = Integer.parseInt(transmission.getContent());
                    log.info("set clientNumber :{}", thisClientNumber);
                } else if (transmission.getTransmissionType() == null) {
                    log.info("no action in inputHandler");
                } else {
                    log.error("invalid transmission, no action");
                }
            }
        } catch (ConnectException e) {
            log.error("连接失败,请稍后重试");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(ChartConstant.IO_ERROR);
            System.exit(1);
        }
    }
}
