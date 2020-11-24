package com.exc.street.light.resource.vo.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Linshiwen
 * @date 2018/7/26
 */
@Setter
@Getter
@ToString
public class ControlVO {
    List<CanChannelControl> canChannelControls;
}
