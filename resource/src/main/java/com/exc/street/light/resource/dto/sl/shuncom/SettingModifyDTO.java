package com.exc.street.light.resource.dto.sl.shuncom;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.enums.sl.shuncom.SettingOperationTypeEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 修改/查询配置 参数类
 * @Author: Xiaok
 * @Date: 2020/8/24 13:52
 */
@Setter
@Getter
@ToString
public class SettingModifyDTO {

    /**
     * 序列码，网关不需要关心，原样传回即可
     */
    private Long serial;

    /**
     * 查询或设置类型
     */
    private SettingOperationTypeEnums typeEnums;

    /**
     * 回路设备 id
     * 当typeEnums.code()为 0、1时必须包含
     */
    private String id;

    /**
     * 映射关系数组，数组元素为十六进制string类型。 当 当typeEnums.code() 为 0 时必须包含
     * 1、数组元素中每两个字节表示一个端口；
     * 2、数组元素最开始的那 两个字节表示回路的端口号；
     * 3、数组元素第三个字节到最后一个字节，每两个字节表示采集的端口号；
     * 例如： a、数组元素"01020406"表示回路端口号 1 与采集端口号 2、4、6 是映射关系；
     *       b、数组元素"03"表示回路端口号 3 与采集端口没有映射关系；
     */
    private List<String> map;

    /**
     * 回路号数组 当typeEnums.code()为1时必须包含
     */
    private List<Integer> loops;

    /**
     * 应用程序名称。 当typeEnums.code()为2时必须包含
     * gateway：网关；
     * sz_iot：连接；
     * szRules：策略；
     * smarthome：家居；
     */
    private String app;

    /**
     * 设定时间，当typeEnums.code()为4时必须包含
     */
    private Date setDate;

    /**
     * 网关需要连接的ap信息，当typeEnums.code()为7时必须包含
     */
    private JSONObject apInfo;

    /**
     * 网关的经纬度，格式：111.222:143.222
     */
    private String lal;
}
