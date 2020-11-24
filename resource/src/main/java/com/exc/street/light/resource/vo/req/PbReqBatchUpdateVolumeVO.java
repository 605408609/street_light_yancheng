package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 公共广播设备批量调节音量接收参数
 *
 * @author LeiJing
 * @Date 2020/04/27
 */
@Getter
@Setter
@ToString
public class PbReqBatchUpdateVolumeVO {
    /**
     * 需要调节音量的设备id集合
     */
    @ApiModelProperty(name = "ids" , value = "需要调节音量的设备id集合")
    private String ids;

    /**
     * 音量
     */
    @ApiModelProperty(name = "volume" , value = "需要调节音量的设备id集合")
    private Integer volume;
}
