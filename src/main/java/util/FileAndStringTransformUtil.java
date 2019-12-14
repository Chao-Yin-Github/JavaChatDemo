package util;

import constant.ChartConstant;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @author yinchao
 * @Date 2019/12/10 12:39
 */
@Slf4j(topic = "FileAndStringTransformUtil")
public class FileAndStringTransformUtil {

    /**
     * 文件转字符串
     * 先将文件转化成字节数组,
     * 然后采用Base64加密,防止数据乱码
     *
     * @param filePath 文件路径
     */
    public static String fileToString(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            byte[] bytes = streamToByte(fileInputStream);
            return new BASE64Encoder().encodeBuffer(bytes);
        } catch (FileNotFoundException e) {
            log.error("file {} not found.", filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把流转换成字节数组
     *
     * @param inputStream input流
     * @return 转化的字节数组
     */
    private static byte[] streamToByte(InputStream inputStream) {
        byte[] result = new byte[0];
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int n = 0;
            while ((n = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, n);
            }
            result = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(ChartConstant.IO_ERROR);
        }
        return result;
    }

    /**
     * 把字符串转化为文件:
     * 先把字符串转化为字节数组
     * 再用Base64解密
     * 最后把字节数组保存到文件
     *
     * @param content  字符串
     * @param filePath 本地文件保存路径
     */
    public static void stringToFile(String content, String filePath) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             InputStream inputStream = new ByteArrayInputStream(content.getBytes())) {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(inputStream);
            fileOutputStream.write(bytes);
            log.info("文件保存成功");
        } catch (FileNotFoundException e) {
            log.error("file {} not found.", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(ChartConstant.IO_ERROR);
        }
    }
}
