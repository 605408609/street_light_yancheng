/**
 * @filename:AlarmOrderSetDao 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.woa.AlarmOrderSet;
import com.exc.street.light.resource.qo.WoaAlarmOrderSetQuery;
import com.exc.street.light.resource.vo.resp.WoaRespAlarmOrderSetVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Repository
@Mapper
public interface AlarmOrderSetMapper extends BaseMapper<AlarmOrderSet> {

    /**
     * 根据id获取灯杆对象
     *
     * @param lampPostId
     * @return
     */
    SlLampPost getLampPostById(Integer lampPostId);

    /**
     * 条件查询
     *
     * @param page
     * @param woaAlarmOrderSetQuery
     * @return
     */
    IPage<WoaRespAlarmOrderSetVO> query(IPage<WoaAlarmOrderSetQuery> page, @Param("woaAlarmOrderSetQuery")WoaAlarmOrderSetQuery woaAlarmOrderSetQuery);
}
