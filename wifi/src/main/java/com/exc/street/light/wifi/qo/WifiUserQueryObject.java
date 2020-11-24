package com.exc.street.light.wifi.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description wifi用户查询参数实体类
 * @Date 2020/3/27
 */
@Data
public class WifiUserQueryObject extends PageParam {

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户Mac")
    private String userMac;

    @ApiModelProperty(value = "区域id")
    private Integer areaId;
}
