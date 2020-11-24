package com.exc.street.light.sl.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 中科智联-时间表对象
 *
 * @author LeiJing
 * @Date 2019/5/20
 */
@Getter
@Setter
@ToString
public class TimeTableVO {
    /**
     * 时间表时间集合
     */
    private List<String> timeList;

    /**
     * 时间表亮度集合
     */
    private List<Integer> valueList;
}
