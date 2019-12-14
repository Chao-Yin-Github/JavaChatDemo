package util;

import constant.ChartConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yinchao
 * @Date 2019/12/12 16:15
 */
@Slf4j(topic = "ChartUtil")
public class ChartUtil {

    /**
     * @param byteBuffer 已经存好一次的byteBuffer
     * @param bytesRead  已经读入的字节数量
     * @return
     */
    @SneakyThrows
    private byte[] readAllFromChannel(ByteBuffer byteBuffer, int bytesRead, SocketChannel socketChannel) {

        List<byte[]> bytesList = new ArrayList<>();
        long sumBytes = 0L;
        byte[] bytes;

        while (bytesRead > 0) {
//            log.info("bytesRead is {}", bytesRead);
            // buffer里面一定是已经存储好的,所以先切换为读状态
            byteBuffer.flip();
            // 把buffer里面的值读到byte[] 数组里面,此时buffer.remaining的值和bytesRead相同
            bytes = new byte[byteBuffer.remaining()];
            // byte[] 数组真正从buffer读入:byteBuffer.get(bytes)
            byteBuffer.get(bytes);
            // 把这个byte数组保存到list里面
            bytesList.add(bytes);
            sumBytes += bytes.length;
            byteBuffer = ByteBuffer.allocate(ChartConstant.CAPACITY);
            bytesRead = socketChannel.read(byteBuffer);
        }
        bytes = new byte[Math.toIntExact(sumBytes)];
        sumBytes = 0L;
        for (byte[] byteArray : bytesList) {
            System.arraycopy(byteArray, 0, bytes, Math.toIntExact(sumBytes), byteArray.length);
            sumBytes += byteArray.length;
        }
        log.info("in readAllFromChannel, bytes length = {}", bytes.length);
        return bytes;
    }

    @SneakyThrows
    public byte[] readBytesFromChannel(SelectionKey key) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(ChartConstant.CAPACITY);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int bytesRead = socketChannel.read(byteBuffer);
        if (bytesRead < 0) {
            key.cancel();
            socketChannel.close();
            log.info("bytesRead<0");
            return new byte[0];
        }
        return readAllFromChannel(byteBuffer, bytesRead, socketChannel);
    }

}
