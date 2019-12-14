package constant;

import java.net.InetSocketAddress;

/**
 * @author yinchao
 * @Date 2019/11/27 19:31
 */
public class ChartConstant {
    /**
     * IO错误
     */
    public static final String IO_ERROR = "IO error.";

    /**
     *
     */
    public static final String FILE_NOT_FOUND = "file not found.";

    /**
     * 端口
     */
    public final static int PORT = 8099;
    /**
     * 文本数据类型
     */
    public final static int DATA_STRING = 1;
    /**
     * 文件数据类型
     */
    public final static int DATA_FILE = 2;
    /**
     * 默认未初始化数据类型
     */
    public final static int DATA_DEFAULT = -1;

//    public static InetSocketAddress inetSocketAddress = new InetSocketAddress("47.100.76.82", PORT);

    public static InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", PORT);

    public static String SENDING_FAILURE = "发送消息失败";

    /**
     * 服务器内存太小了,为保障性能,只设置为3MB左右
     */
//    public static int CAPACITY = 1024 * 1024 * 3;

    public static int CAPACITY = 1024 * 1024 * 512;
}
