/**
 * @filename:LampDeviceService 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.qo.SlLampDeviceQuery;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamReqVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamRespVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface LampDeviceService extends IService<LampDevice> {

    /**
     * 设备 数据/信息 变化接口订阅
     * @param type
     * @param url
     * @return
     */
    Result subscription(Integer type,String url,HttpServletRequest request);

    /**
     * 根据编号获取灯控器
     * @param num
     * @return
     */
    Result<LampDevice> getByNum(String num,String model,String factory);

    /**
     * @explain 查询所有灯具
     * @author Longshuangyang
     * @time 2020-03-16
     */
    Result<List<LampDevice>> getList();

    /**
     * @explain 根据id集合查询所有灯具
     * @author Longshuangyang
     * @time 2020-03-16
     */
    Result<List<LampDevice>> getListByIdList(List<Integer> idList);

    /**
     * 单灯控制
     *
     * @param singleLampParamList
     * @param lampDevice
     * @return
     */
    Result singleControl(List<SingleLampParam> singleLampParamList, LampDevice lampDevice);

    /**
     * 单灯控制(电信平台)
     *
     * @param lampDevice
     * @return
     */
    Result singleControl(LampDevice lampDevice);

    /**
     * 下发控制
     *
     * @param request
     * @param vo
     * @return
     */
    Result control(HttpServletRequest request, SlReqLightControlVO vo);

    /**
     * 临时控制
     * @param request
     * @return
     */
    Result controlTemp(HttpServletRequest request, SlReqLightControlVO vo);

    /**
     * 获取路灯控制结果
     *
     * @param tasks
     * @param executorService
     * @return
     */
    Result getResult(Collection tasks, ExecutorService executorService);

    /**
     * 查询灯具(根据灯杆id集合)
     *
     * @param lampPostIdList
     * @param request
     * @return
     */
    Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request);

    /**
     * 查询灯控器(根据灯杆id集合)
     *
     * @param lampPostIdList
     * @param request
     * @return
     */
    Result getListByLampPost(List<Integer> lampPostIdList, HttpServletRequest request);

    /**
     * 灯控设备分页条件查询
     *
     * @param slLampDeviceQuery
     * @param request
     * @return
     */
    Result getPage(SlLampDeviceQuery slLampDeviceQuery, HttpServletRequest request);

    /**
     * 添加灯控设备
     *
     * @param singleLampParamReqVO
     * @return
     */
    Result add(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request);

    /**
     * 修改灯控设备
     *
     * @param singleLampParamReqVO
     * @param request
     * @return
     */
    Result updateDevice(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request);

    /**
     * 灯控设备验证唯一性
     *
     * @param singleLampParamReqVO
     * @param request
     * @return
     */
    Result unique(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request);

    /**
     * 灯控设备详情
     *
     * @param id
     * @param request
     * @return
     */
    Result detail(Integer id, HttpServletRequest request);

    /**
     * 删除设备
     * @param id
     * @param request
     * @return
     */
    Result delete(Integer id, HttpServletRequest request);

    /**
     * 批量删除设备
     * @param ids
     * @param request
     * @return
     */
    Result delete(List<Integer> ids, HttpServletRequest request);


    /**
     * 查询区域灯杆id集合
     * @param areaId
     * @return
     */
    List<Integer> areaLampPostIdList(Integer areaId);
}