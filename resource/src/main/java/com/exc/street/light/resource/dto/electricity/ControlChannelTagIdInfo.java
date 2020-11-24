package com.exc.street.light.resource.dto.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: XuJiaHao
 * @Description: tagId 相关信息
 * @Date: Created in 17:35 2020/3/16
 * @Modified:
 */
@Getter
@Setter
@ToString
public class ControlChannelTagIdInfo {
    /**
     * 回路所在设备地址
     */
    private Integer deviceAddress;

    /**
     * 回路的控制id
     */
    private Integer controlId;
}
