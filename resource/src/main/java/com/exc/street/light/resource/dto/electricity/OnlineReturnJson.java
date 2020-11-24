package com.exc.street.light.resource.dto.electricity;

import java.util.Arrays;

/**
 * @Author:xujiahao
 * @Description 在线状态的返回JSON
 * @Data:Created in 9:41 2018/2/2
 * @Modified By:
 */
public class OnlineReturnJson {
    private int type; //返回类型
    private OnlineStr[] data; //返回在线集合数组

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public OnlineStr[] getData() {
        return data;
    }

    public void setData(OnlineStr[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OnlineReturnJson{" +
                "type=" + type +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
