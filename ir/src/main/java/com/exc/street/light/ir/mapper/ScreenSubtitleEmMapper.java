/**
 * @filename:ScreenSubtitleEmDao 2020-11-10
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ir.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.qo.IrScreenDeviceQuery;
import com.exc.street.light.resource.qo.IrScreenSubtitleEmQuery;
import com.exc.street.light.resource.vo.resp.IrRespScreenSubtitleEmVO;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.ir.ScreenSubtitleEm;
import org.apache.ibatis.annotations.Param;

/**   
 * @Description:TODO(传感器关联显示屏显示数据设置表数据访问层)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Mapper
public interface ScreenSubtitleEmMapper extends BaseMapper<ScreenSubtitleEm> {

    /**
     * 获取修改传感器关联显示屏显示数据设置列表
     * @param page
     * @param irScreenSubtitleEmQuery
     * @return
     */
    IPage<IrRespScreenSubtitleEmVO> query(IPage<IrScreenDeviceQuery> page,@Param("irScreenSubtitleEmQuery") IrScreenSubtitleEmQuery irScreenSubtitleEmQuery);
}
