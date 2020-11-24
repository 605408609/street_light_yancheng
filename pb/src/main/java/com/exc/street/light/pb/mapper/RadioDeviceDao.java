/**
 * @filename:RadioDeviceDao 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.qo.PbRadioDeviceQueryObject;
import com.exc.street.light.resource.vo.resp.PbRespDeviceAndLampPostVO;
import com.exc.street.light.resource.vo.resp.PbRespLampPostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: LeiJing
 */
@Repository
@Mapper
public interface RadioDeviceDao extends BaseMapper<RadioDevice> {

    /**
     * 根据雷拓终端id修改在线状态
     *
     * @param networkState
     * @param termId
     * @return
     */
    int updateStatusByTermId(@Param("networkState") Integer networkState, @Param("termId") Integer termId);

    /**
     * 修改id集合的在线状态
     *
     * @param networkState
     * @param idList
     * @return
     */
    int updateStatusById(@Param("networkState") Integer networkState, @Param("idList") List<Integer> idList);

    /**
     * 广播设备列表
     *
     * @param page 分页对象
     * @param queryObject 查询条件和权限过滤
     * @return
     */
    IPage<PbRespDeviceAndLampPostVO> getPageList(Page<PbRespDeviceAndLampPostVO> page, @Param("queryObject") PbRadioDeviceQueryObject queryObject);


    /**
     * 根据查询条件获取设备列表
     *
     * @param qo 查询对象
     * @return
     */
    List<PbRespDeviceAndLampPostVO> getList(@Param("qo") PbRadioDeviceQueryObject qo);

    /**
     * 根据灯杆分组id获取广播设备id
     *
     * @param groupIdList 灯杆分组集合
     * @return
     */
    List<Integer> getIdsByGroupIds(@Param("groupIdList") List<Integer> groupIdList);

    /**
     * 根据区域id获取灯杆信息
     *
     * @param areaId 区域id
     * @return
     */
    List<PbRespLampPostVO> getLampPostInfoByAreaId(@Param("areaId") Integer areaId);

}
