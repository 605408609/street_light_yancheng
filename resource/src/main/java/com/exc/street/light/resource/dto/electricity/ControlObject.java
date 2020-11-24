package com.exc.street.light.resource.dto.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 控制对象
 *
 * @author LinShiWen
 * @date 2018/4/20
 */
@Setter
@Getter
@ToString
public class ControlObject {
    /**
     * 控制对象编码
     */
    private int tagId;
    /**
     * 控制对象类型
     */
    private int tagType = 4;
    /**
     * 控制值类型
     */
    private int tagValueType = 1;
    /**
     * 控制值
     */
    private double tagValue;
    /**
     * 延时时间
     */
    private int time = 0;
    /**
     * 模块地址
     */
    private int deviceAddress;
    /**
     * 控制id
     */
    private int controlId;
}
