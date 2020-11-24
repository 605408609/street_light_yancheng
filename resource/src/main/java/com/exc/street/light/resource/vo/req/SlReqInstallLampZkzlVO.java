package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SlReqInstallLampZkzlVO {

    @ApiModelProperty(name = "deviceIdList", value = "设备id集合")
    private List<Integer> deviceIdList;

    @ApiModelProperty(name = "deviceId", value = "设备id")
    private Integer deviceId;

    @ApiModelProperty(name = "concentratorId", value = "设备地址： 04 19 11 00---（集中器 ID： 19040011）")
    private String concentratorId;

    @ApiModelProperty(name = "installNum", value = "配置数量： 01 00---配置一个灯控器")
    private Integer installNum;

    @ApiModelProperty(name = "lampNoList", value = "灯装置序号集合： 37 00---十六进制数 0x0037， 装置序号为最小为 55，依次累加")
    private List<Integer> lampNoList;

    @ApiModelProperty(name = "addOrDelete", value = "灯具安装或者删除，1 安装，0 删除。 测试点号： 01 00---0x0001 安装灯具， 0x0000 删除灯具")
    private Integer addOrDelete;

    @ApiModelProperty(name = "lampAdressList", value = "灯具地址集合： 34 88 50 00 00 00---（灯控器 ID： 000000508834）")
    private List<String> lampAdressList;

    @ApiModelProperty(name = "groupNo", value = "所属组： 01---安装至 1 组")
    private Integer groupNo;

    @ApiModelProperty(name = "groupNoList", value = "分组集合")
    private List<Integer> groupNoList;

}
