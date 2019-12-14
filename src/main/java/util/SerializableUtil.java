package util;

import constant.ChartConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 对象序列化和反序列化工具类
 *
 * @author yinchao
 * @Date 2019/12/10 00:25
 */
@Slf4j(topic = "SerializableUtil")
public class SerializableUtil {

    /**
     * 将byte数组的内容转化成transmission对象
     *
     * @param bytes socketChannel传过来的字节数组
     * @return 成功转化返回transmission对象, 不成功返回一个被初始化transmission对象
     */
    public static Object toObject(byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return objectInputStream.readObject();
        } catch (EOFException e) {
            e.printStackTrace();
            log.error("字节数组接收不完整");
        } catch (IOException e) {
            e.printStackTrace();
            log.error(ChartConstant.IO_ERROR);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("Class not found.");
        }
        return new Transmission();
    }

    /**
     * 将一个transmission对象转化成一个字节数组
     *
     * @param object 一个transmission对象
     * @return 成功转化完成对应的字节数组, 不成功转化返回一个空的字节数组
     */
    public static byte[] toBytes(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ChartConstant.IO_ERROR);
        }
        return new byte[0];
    }
}
