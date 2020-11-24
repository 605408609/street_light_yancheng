package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 第三方接口单灯控制命令参数
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class MapReqDeviceNumberVO {

    @ApiModelProperty(name = "number" , value = "设备个数")
    private Integer number;

    @ApiModelProperty(name = "deviceType" , value = "设备类型编号（1：灯具，2：WIFI，3：广播，4：监控，5：显示屏，6：一键呼叫，7：气象）")
    private Integer deviceType;

    @ApiModelProperty(name = "onlineRate" , value = "在线率")
    private Integer onlineRate;

}
