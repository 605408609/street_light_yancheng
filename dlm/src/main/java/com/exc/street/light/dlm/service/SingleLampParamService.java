package com.exc.street.light.dlm.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.sl.LampLoopType;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceVO;

import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: HuangJinHao
 *
 */
public interface SingleLampParamService extends IService<SingleLampParam> {

    /**
     * 根据灯杆id集合获取灯具返回对象集合
     * @param slLampPostIdList
     * @return
     */
    List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList);

    /**
     * 根据灯杆id集合获取灯具返回对象集合
     * @param slLampPostIdList
     * @return
     */
    List<SlRespSystemDeviceVO> getSystemDeviceList(List<Integer> slLampPostIdList);

    /**
     * 获取所有回路类型列表
     * @return
     */
    List<LampLoopType> loopTypeList();
}
