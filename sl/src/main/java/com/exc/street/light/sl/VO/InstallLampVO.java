package com.exc.street.light.sl.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 中科智联-灯具安装对象
 *
 * @author LeiJing
 * @date 2020/08/26
 */
@Setter
@Getter
@ToString
public class InstallLampVO {
    /**
     * 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     */
    private String concentratorId;

    /**
     * 配置数量： 01 00---配置一个灯控器
     */
    private Integer installNum;

    /**
     * 灯装置序号集合： 37 00---十六进制数 0x0037， 装置序号为最小为 55，依次累加
     */
    private List<Integer> lampNoList;

    /**
     * 灯具地址集合： 34 88 50 00 00 00---（灯控器 ID： 000000508834）
     */
    private List<String> lampAdressList;

    /**
     * 所属组： 01---安装至 1 组
     */
    private int groupNo;
}
