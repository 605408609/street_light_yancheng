package com.exc.street.light.resource.dto.sl.shuncom;

import com.alibaba.fastjson.annotation.JSONField;
import com.exc.street.light.resource.enums.sl.shuncom.DeviceStInfoEnums;
import lombok.Data;

/**
 * 顺舟云盒 策略条件类
 *
 * @Author: Xiaok
 * @Date: 2020/8/25 17:42
 */
@Data
public class StrategyConditionDTO {

    /**
     * 条件序号 非空
     */
    @JSONField(name = "idx")
    private Integer index;

    /**
     * 条件类型：  非空
     * 1：定时（格林威治时间，只支持逻辑运算符”==”）
     * 2：触发
     * 3：周期轮询
     */
    @JSONField(name = "type")
    private Integer conditionType;

    /**
     * 策略执行时间，在conditionType是定时策略为非空
     * 支持通配符： xxxx-xx-xx-xx-xx-xx-x(年-月-日-时-分-秒-星期)
     * 例如：2016-6-21-9-12- 0-#表示执行一次；
     * #-#-#-15-56-0-1,4,7 表示周一周四周日执行。
     */
    @JSONField(name = "time")
    private String timeCorn;

    /**
     * 设备ID,当conditionType为触发时非空
     */
    @JSONField(name = "id")
    private String deviceId;

    /**
     * 端口号,当conditionType为触发时非空
     */
    @JSONField(name = "ep")
    private String ep;

    /**
     * 操作类型
     * 1:小于；
     * 2:等于；
     * 3:大于；
     * 4:变化；
     * null:无任何作用
     */
    @JSONField(name = "op")
    private Integer operationType;

    /**
     * 操作命令类型，当conditionType为触发时非空
     * com.exc.street.light.resource.enums.sl.shuncom.DeviceStInfoEnums.code()
     */
    private String cmd;

    /**
     * 对应cmd的状态值，当conditionType为触发时非空
     */
    @JSONField(name = "val")
    private Object value;
}
