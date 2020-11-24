package com.exc.street.light.resource.vo.sl;

import com.exc.street.light.resource.entity.sl.LampStrategyActionHistory;
import lombok.Data;

import java.util.List;

@Data
public class SlSingleLampControlVO {

    private Integer id;
    private List<LampStrategyActionHistory> lampStrategyActionHistories;

}
