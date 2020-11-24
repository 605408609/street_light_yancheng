/**
 * @filename:ScreenSubtitleEmScreenDao 2020-11-16
 * @project em  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.em.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.em.ScreenSubtitleEmScreen;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(传感器关联显示屏中间表数据访问层)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Mapper
public interface ScreenSubtitleEmScreenMapper extends BaseMapper<ScreenSubtitleEmScreen> {

    /**
     * 根据显示屏的id集合查询显示屏的编号集合
     * @param list
     * @return
     */
    List<String> selectScreenDeviceNumList(@Param("list") List<Integer> list);
}
