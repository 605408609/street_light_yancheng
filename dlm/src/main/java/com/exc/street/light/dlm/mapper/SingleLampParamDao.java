package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.sl.LampLoopType;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: HuangJinHao
 *
 */
@Repository
@Mapper
public interface SingleLampParamDao extends BaseMapper<SingleLampParam> {

    /**
     * 根据灯杆id集合获取灯具返回对象集合
     * @param list
     * @return
     */
    List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(@Param("list") List<Integer> list);

    /**
     * 根据灯杆id集合获取灯具返回对象集合
     * @param lampPostIdList
     * @return
     */
    List<SlRespSystemDeviceVO> getSystemDeviceList(@Param("lampPostIdList") List<Integer> lampPostIdList);

    /**
     * 获取所有回路类型
     * @return
     */
    List<LampLoopType> loopTypeList();
}
