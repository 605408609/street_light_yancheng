package com.exc.street.light.resource.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 根据区域id及其他id集合查询灯杆列表使用参数对象
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class SingleLampByAreaIdVO {

    private List<Integer> lampPostIdList;

    private List<Integer> siteIdList;

    private List<Integer> streetIdList;

    private List<Integer> singleIdList;

    private Integer areaId;
}
