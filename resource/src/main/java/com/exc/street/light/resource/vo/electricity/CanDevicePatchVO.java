package com.exc.street.light.resource.vo.electricity;

import com.exc.street.light.resource.entity.electricity.CanDevice;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 批量编辑设备接收对象
 *
 * @author Linshiwen
 * @date 2018/5/22
 */
@Setter
@Getter
@ToString
public class CanDevicePatchVO {
    /**
     * 设备集合
     */
    private List<CanDevice> canDevices;
}