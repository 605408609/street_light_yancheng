package com.exc.street.light.sl.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 中科智联-单灯控制对象
 *
 * @author LeiJing
 * @date 2020/08/26
 */
@Setter
@Getter
@ToString
public class OneLampControlVO {
    /**
     * 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     */
    private String concentratorId;

    /**
     * 灯装置序号： 37 00---十六进制数 0x0037， 装置序号为最小为 55，依次累加
     */
    private Integer lampNo;

    /**
     * 调光值： 0-100
     */
    private Integer value;
}
