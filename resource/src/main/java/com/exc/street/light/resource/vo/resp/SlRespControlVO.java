package com.exc.street.light.resource.vo.resp;

import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.resource.vo.req.SlControlSystemDeviceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 灯具控制返回信息
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class SlRespControlVO {

    @ApiModelProperty(name = "successNum" , value = "下发成功数量")
    private Integer successNum;

    @ApiModelProperty(name = "successLampDeviceList" , value = "下发成功灯控设备对象")
    private List<SlControlSystemDeviceVO> successLampDeviceList;

    @ApiModelProperty(name = "defaultNum" , value = "下发失败数量")
    private Integer defaultNum;

    @ApiModelProperty(name = "defaultLampDeviceList" , value = "下发失败灯控设备对象")
    private List<SlControlSystemDeviceVO> defaultLampDeviceList;
}
