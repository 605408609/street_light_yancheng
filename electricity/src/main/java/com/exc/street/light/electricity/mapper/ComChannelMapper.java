package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.electricity.ComChannel;
import org.apache.ibatis.annotations.Param;

/**
 * 串口回路mapper
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
public interface ComChannelMapper extends BaseMapper<ComChannel> {

    /**
     * 根据tagID和nid查询
     *
     * @param nid
     * @param tagId
     * @return
     */
    int getByTagIdAndNid(@Param("nid") Integer nid, @Param("tagId") Integer tagId);
}