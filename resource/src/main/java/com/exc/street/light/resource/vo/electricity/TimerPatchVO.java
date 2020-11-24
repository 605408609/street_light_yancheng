package com.exc.street.light.resource.vo.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 定时器接收对象
 *
 * @author LinShiWen
 * @date 2018/4/21
 */
@Setter
@Getter
@ToString
public class TimerPatchVO {
    List<TimerVO> timerVOS;
}
