package com.exc.street.light.resource.vo.electricity;

import com.exc.street.light.resource.dto.electricity.TimingParameter;
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
public class TimerVO {
    List<TimingParameter> timingParameters;
    /**
     * 强电节点
     */
    private Integer nid;
}
