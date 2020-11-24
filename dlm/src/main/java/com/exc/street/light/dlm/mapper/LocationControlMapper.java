/**
 * @filename:LocationControlDao 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import com.exc.street.light.resource.qo.DlmLocationControlQuery;
import com.exc.street.light.resource.vo.resp.DlmRespLocationControlMixVO;
import com.exc.street.light.resource.vo.resp.DlmRespLocationControlVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface LocationControlMapper extends BaseMapper<LocationControl> {

    /**
     * 集中控制器详情
     * @param controlId
     * @return
     */
    DlmRespLocationControlVO selectLocationControlByControlId(@Param("controlId") Integer controlId);

    /**
     *  集中控制器混合信息
     * @param controlId
     * @return
     */
    List<DlmRespLocationControlMixVO> selectMixLocationControlByControlId(@Param("controlId") Integer controlId);

    /**
     * 分页查询集中控制器列表
     * @param page
     * @param controlQuery
     * @return
     */
    IPage<DlmRespLocationControlVO> selectLocationControlWithPageByControlQuery(Page<DlmRespLocationControlVO> page, @Param("controlQuery") DlmLocationControlQuery controlQuery);

    /**
     * 集中控制器下拉列表
     * @param areaId
     * @return
     */
    List<LocationControl> selectLocationControlWithOption(@Param("areaId") Integer areaId);

    /**
     * 根据分区过滤出相应的集控信息
     * @param locationControlTypeIdList
     * @param areaId
     * @return
     */
    List<LocationControl> selectControlListByArea(@Param("typeIdList") List<Integer> locationControlTypeIdList, @Param("areaId") Integer areaId);

    /**
     * 根据条件查询集控器列表
     * @param controlQuery
     * @return
     */
    List<DlmRespLocationControlVO> selectLocationControlListByControlQuery(@Param("controlQuery") DlmLocationControlQuery controlQuery);

    /**
     * 集中控制器下拉列表(EXC)
     * @param areaId
     * @param typeId
     * @return
     */
    List<LocationControl> selectListLocationControlOfExcWithOptionQuery(@Param("areaId") Integer areaId, @Param("typeId") int typeId);
}
