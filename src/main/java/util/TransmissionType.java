package util;

import java.io.Serializable;

/**
 * 传输类型
 *
 * @author yinchao
 * @Date 2019/12/9 13:00
 */
public enum TransmissionType implements Serializable {
    /**
     * 文件类型
     */
    FILE,
    /**
     * 文本字符串
     */
    STRING,

    /**
     * 图片
     */
    PIC,

    /**
     * 信息
     */
    MESSAGE,

    /**
     * id标识
     */
    UUID
}
