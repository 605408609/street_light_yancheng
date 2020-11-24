package com.exc.street.light.resource.vo.electricity;

import com.exc.street.light.resource.entity.electricity.CanTiming;
import com.exc.street.light.resource.utils.ElectricityConstantUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 根据场景名称查询场景列表视图
 *
 * @author Also
 */
@Setter
@Getter
@ToString
public class CanSceneNameVO {
    private Integer tagId;
    private String name;
    private String nodeName;
    private Integer nid;

    @ApiModelProperty(name = "areaId",value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "areaName",value = "区域名称")
    private String areaName;
    /**
     * 外键:设备类型编码
     */
    private Integer dsn = ElectricityConstantUtil.DEVICE_TYPE_1;
    /**
     * 按时执行的定时器数量
     */
    private Integer timingNum;

    /**
     * 周期执行的定时器数量
     */
    private Integer cycleNum;

    private List<CanTiming> canTimings;

}
