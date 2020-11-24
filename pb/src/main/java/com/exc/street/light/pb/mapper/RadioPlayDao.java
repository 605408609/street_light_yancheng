/**
 * @filename:RadioPlayDao 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.pb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.pb.RadioPlay;
import com.exc.street.light.resource.qo.PbRadioPlayQueryObject;
import com.exc.street.light.resource.vo.pb.PbRespRadioPlayPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Repository
@Mapper
public interface RadioPlayDao extends BaseMapper<RadioPlay> {
    /**
     * 分页查询播放中的节目列表
     *
     * @param page
     * @param qo
     * @return
     */
    IPage<RadioPlay> query(IPage<RadioPlay> page, @Param("qo") PbRadioPlayQueryObject qo);

    IPage<PbRespRadioPlayPageVO> getPageList(IPage<PbRespRadioPlayPageVO> page, @Param("qo") PbRadioPlayQueryObject qo);


    /**
     * 根据灯杆获取关联定时广播id
     * @param lampPostIds
     * @return
     */
    List<Integer> getPlayIdListByLampPostId(@Param("lampPostIds")List<Integer> lampPostIds);
}
