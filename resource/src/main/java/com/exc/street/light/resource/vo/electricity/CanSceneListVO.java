package com.exc.street.light.resource.vo.electricity;

import com.exc.street.light.resource.utils.ConstantUtil;
import com.exc.street.light.resource.utils.ElectricityConstantUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 节点选择场景列表视图
 *
 * @author Also
 */
@Setter
@Getter
@ToString
public class CanSceneListVO {

    private Integer id;
    private String name;
    private Integer tagId;
    /**
     * 外键:设备类型编码
     */
    private Integer dsn = ElectricityConstantUtil.DEVICE_TYPE_1;
    /**
     * 是否编辑过 0:未编辑 1:已编辑
     */
    private Integer isEdit;

    /**
     * 按时执行的定时器数量
     */
    private Integer timingNum;

    /**
     * 周期执行的定时器数量
     */
    private Integer cycleNum;

}
