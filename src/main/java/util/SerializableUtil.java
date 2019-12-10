package util;

import constant.ChartConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author yinchao
 * @Date 2019/12/10 00:25
 */
@Slf4j(topic = "SerializableUtil")
public class SerializableUtil {
    public static Object toObject(byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (Serializable) objectInputStream.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            log.error("字节数组转换对象错误");
        }catch(EOFException e){
            log.error("bytes={}",bytes);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(ChartConstant.IO_ERROR);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("Class not found.");
        }
        return null;
    }

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
        return null;
    }
}
