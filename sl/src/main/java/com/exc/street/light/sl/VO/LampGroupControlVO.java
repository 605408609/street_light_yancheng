package com.exc.street.light.sl.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 中科智联-灯具分组控制对象
 *
 * @author LeiJing
 * @date 2020/08/31
 */
@Setter
@Getter
@ToString
public class LampGroupControlVO {
    /**
     * 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     */
    private String concentratorId;

    /**
     * 控制的组数量
     */
    private Integer groupNum;

    /**
     * 控制的组号集合
     */
    private List<Integer> groupNoList;

    /**
     * 控制的调光值集合, 调光值： 0-100
     */
    private List<Integer> valueList;
}
