package util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用于聊天器传输数据的自定义协议
 *
 * @author yinchao
 * @Date 2019/12/9 12:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transmission implements Serializable {

    /**
     * 数据内容
     */
    private String content;

    /**
     * 文件类型
     */
    private TransmissionType transmissionType;

    /**
     * 发送目的客户编号
     */
    private int destinationNumber;

    /**
     * 原客户编号
     */
    private int sourceNumber;
}
