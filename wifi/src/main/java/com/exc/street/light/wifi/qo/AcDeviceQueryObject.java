package com.exc.street.light.wifi.qo;

import com.exc.street.light.resource.core.PageParam;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description ac设备参数查询类
 * @Date 2020/3/27
 */
@Data
public class AcDeviceQueryObject extends PageParam {

    /**
     * ac名称
     */
    private String name;

    /**
     * 网络状态
     */
    private Integer networkState;

    /**
     * 区域id
     */
    private Integer areaId;
}
