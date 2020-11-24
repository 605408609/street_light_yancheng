package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class SlRespDeviceUpgradeLogVO {

    @ApiModelProperty(name = "id" , value = "升级记录表")
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "creator" , value = "操作人")
    private Integer creator;

    @ApiModelProperty(name = "fileName" , value = "文件名称")
    private String fileName;

    @ApiModelProperty(name = "preserveName" , value = "文件保存名称")
    private String preserveName;

    @ApiModelProperty(name = "editionNew" , value = "目标版本号")
    private String editionNew;

    @ApiModelProperty(name = "slRespDeviceUpgradeLogStatusVOList" , value = "设备升级状态集合")
    private List<SlRespDeviceUpgradeLogStatusVO> slRespDeviceUpgradeLogStatusVOList;
}
