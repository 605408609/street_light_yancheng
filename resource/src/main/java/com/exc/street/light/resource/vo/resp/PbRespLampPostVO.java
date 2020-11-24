package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 灯杆信息及是否绑定广播设备
 * @author xiaok
 */
@Getter
@Setter
@ToString
public class PbRespLampPostVO {

    /**
     * 灯杆id
     */
    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    /**
     * 灯杆名称
     */
    @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;

    /**
     *
     */
    @ApiModelProperty(name = "isBindRadioDevice" , value = "是否绑定广播设备,null-未绑定,1-已绑定")
    private Integer isBindRadioDevice;

}
