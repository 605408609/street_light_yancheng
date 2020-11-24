package com.exc.street.light.resource.dto.electricity;

import com.exc.street.light.resource.entity.electricity.CanTiming;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 定时器
 *
 * @author LinShiWen
 * @date 2018/4/21
 */
@Setter
@Getter
@Accessors(chain = true)
public class Timer {

    List<CanTiming> canTimings = new ArrayList<>();
}
