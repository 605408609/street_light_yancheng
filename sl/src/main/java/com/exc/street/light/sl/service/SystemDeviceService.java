/**
 * @filename:SystemDeviceService 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.resource.qo.SlLampDeviceQuery;
import com.exc.street.light.resource.vo.req.*;
import com.exc.street.light.resource.vo.sl.SingleLampParamReqVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @Description:TODO(设备表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 */
public interface SystemDeviceService extends IService<SystemDevice> {

    /**
     * 修改设备在线状态
     *
     * @param deviceId     设备id
     * @param onlineStatus 在线状态 0离线 1在线
     * @return 是否修改成功
     */
    boolean updateDeviceOnlineStatus(Integer deviceId, Integer onlineStatus);

    /**
     * 设备 数据/信息 变化接口订阅
     * @param type
     * @param url
     * @return
     */
    /*Result subscription(Integer type,String url,HttpServletRequest request);

    *//**
     * 根据编号获取灯控器
     * @param num
     * @return
     *//*
    Result<LampDevice> getByNum(String num, String model, String factory);

    *//**
     * @explain 查询所有灯具
     * @author Longshuangyang
     * @time 2020-03-16
     *//*
    Result<List<LampDevice>> getList();

    *//**
     * @explain 根据id集合查询所有灯具
     * @author Longshuangyang
     * @time 2020-03-16
     *//*
    Result<List<LampDevice>> getListByIdList(List<Integer> idList);

    */

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
     *//*
    Result controlTemp(HttpServletRequest request, SlReqLightControlVO vo);

    */

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
     * 查询设备集合(根据灯杆id集合)
     *
     * @param lampPostIdList
     * @param request
     * @return
     */
    List<SystemDevice> getListByLampPost(List<Integer> lampPostIdList, HttpServletRequest request);

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
     * 添加灯控设备
     *
     * @param singleLampParamReqVO
     * @param request
     * @return
     */
    Result addZkzl(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request);

    /**
     * 添加灯控设备
     *
     * @param singleLampParamReqVO
     * @param request
     * @return
     */
    Result addNh(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request);

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
     * 灯控设备验证唯一性（农化）
     *
     * @param singleLampParamReqVO
     * @param request
     * @return
     */
    Result uniqueNh(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request);

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
     *
     * @param id
     * @param request
     * @return
     */
    Result delete(Integer id, HttpServletRequest request);

    /**
     * 批量删除设备
     *
     * @param ids
     * @param request
     * @return
     */
    Result delete(List<Integer> ids, HttpServletRequest request);


    /**
     * 查询区域灯杆id集合
     *
     * @param areaId
     * @return
     */
    List<Integer> areaLampPostIdList(Integer areaId);

    /**
     * 根据指定条件筛选设备
     *
     * @param deviceId
     * @param name
     * @param num
     * @param deviceTypeId
     * @return
     */
    Result getSystemDeviceById(Integer deviceId, String name, String num, Integer deviceTypeId);

    /**
     * 根据设备唯一标识筛选设备
     * @param flag
     * @param deviceTypeFlag
     * @return
     */
    List<SystemDevice> getByFlag(String flag, Integer deviceTypeFlag);

    /**
     * 根据设备编号筛选设备
     *
     * @param num
     * @param deviceTypeIdList
     * @return
     */
    Integer selectCountByNum(String num, List<Integer> deviceTypeIdList);

    /**
     * 根据设备id集合查询对象列表
     *
     * @param deviceIdList
     * @param request
     * @return
     */
    Result getDeviceListByIdList(List<Integer> deviceIdList, HttpServletRequest request);

    /**
     * 单灯控制
     *
     * @param slControlSystemDeviceVOList
     * @return
     */
    Result singleControl(List<SlControlSystemDeviceVO> slControlSystemDeviceVOList);

    /**
     * 按组控制灯具
     *
     * @param request
     * @param vo
     * @return
     */
    Result controlByGroup(HttpServletRequest request, SlReqLightControlVO vo);

    /**
     * 获取设备对应的集中控制器编号
     *
     * @param deviceIdList
     * @return
     */
    List<SlDeviceLocationControlVO> getLocationControlNums(List<Integer> deviceIdList);

    /**
     * 修改灯具的状态及亮度
     *
     * @param successLampDeviceList
     * @return
     */
    boolean updateByControlParam(List<SlControlSystemDeviceVO> successLampDeviceList);

    /**
     * ZKZL设备注册
     *
     * @param request
     * @param slReqInstallLampZkzlVO
     * @return
     */
    Result registerDevice(HttpServletRequest request, SlReqInstallLampZkzlVO slReqInstallLampZkzlVO);

    /**
     * ZKZL设备解除绑定
     *
     * @param request
     * @param slReqInstallLampZkzlVOList
     * @return
     */
    Result relieveRegisterDevice(HttpServletRequest request, List<SlReqInstallLampZkzlVO> slReqInstallLampZkzlVOList);

    /**
     * 根据灯杆id查询灯杆
     * @param deviceId
     * @return
     */
    SlLampPost selectLampPostByDeviceId(Integer deviceId);

    /**
     * 批量修改灯具
     * @param singleLampParamReqVOS
     * @param request
     * @return
     */
    Result updateDeviceBatch(List<SingleLampParamReqVO> singleLampParamReqVOS, HttpServletRequest request);

    /**
     * 批量校验灯控设备唯一性
     * @param singleLampParamReqVOS
     * @param request
     * @return
     */
    Result uniqueBatch(List<SingleLampParamReqVO> singleLampParamReqVOS, HttpServletRequest request);
}