/**
 * @filename:EdAshcanDao 2020-09-28
 * @project ed  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.ed.EdAshcan;
import com.exc.street.light.resource.vo.req.EdReqAshcanPageVO;
import com.exc.street.light.resource.vo.resp.EdRespAlarmVO;
import com.exc.street.light.resource.vo.resp.EdRespAshcanVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(垃圾桶信息表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Repository
@Mapper
public interface EdAshcanMapper extends BaseMapper<EdAshcan> {

    /**
     * 垃圾桶设备条件查询
     * @param edReqAshcanPageVO
     * @return
     */
    List<EdRespAshcanVO> getPage(@Param("edReqAshcanPageVO")EdReqAshcanPageVO edReqAshcanPageVO);

    /**
     * 垃圾桶设备分页条件查询
     * @param edRespAshcanVOIPage
     * @param edReqAshcanPageVO
     * @return
     */
    List<EdRespAshcanVO> getPage(IPage<EdRespAshcanVO> edRespAshcanVOIPage, @Param("edReqAshcanPageVO")EdReqAshcanPageVO edReqAshcanPageVO);


    /**
     * 修改设备在线离线状态
     * @param edAshcan
     */
    void updateStatus(EdAshcan edAshcan);

    /**
     * 根据设备编号查询设备所属街道站点
     * @param num
     * @return
     */
    EdRespAlarmVO selectEdAshcanAlarm(@Param("num")String num);

}
