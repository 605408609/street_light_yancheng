package com.exc.street.light.resource.dto.sl.shuncom;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 顺舟云盒 策略动作类
 *
 * @Author: Xiaok
 * @Date: 2020/8/25 18:01
 */
@Data
public class StrategyActDTO {

    /**
     * 动作执行的顺序 非空
     */
    @JSONField(name = "idx")
    private Integer index;

    /**
     * 动作延迟执行的时间，单位为秒 非空
     */
    private Integer delay;

    /**
     * 目标类型： 非空
     * 1：设备 ID，160bits
     * 2：group id，16bits
     * 3：scene id，8bits
     * 4：rules
     */
    @JSONField(name = "type")
    private Integer targetType;

    /**
     * 设备ID或scene id，当idType=1或3时非空
     */
    private String id;

    /**
     * rules id，当idType=4时非空
     */
    private Integer rid;

    /**
     * group id，当idType=2时非空
     */
    private Integer gid;

    /**
     * 单设备端口，当idType=1时非空
     */
    private Integer ep;

    /**
     * 操作命令类型，非空
     * com.exc.street.light.resource.enums.sl.shuncom.DeviceStInfoEnums.code()
     */
    private String cmd;

    /**
     * 对应cmd的状态值，非空
     */
    @JSONField(name = "val")
    private Object value;

    /**
     * 日出偏移量
     */
    private Integer rofs;

    /**
     * 日落偏移量
     */
    private Integer sofs;
}
