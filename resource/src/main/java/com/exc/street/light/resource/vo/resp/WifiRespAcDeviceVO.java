package com.exc.street.light.resource.vo.resp;

import com.exc.street.light.resource.entity.wifi.WifiAcDevice;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description AC设备
 * @Date 2020/3/27
 */
@Data
public class WifiRespAcDeviceVO extends WifiAcDevice {

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * ap设备集合
     */
    private List<WifiApDevice> apDeviceList;

}
