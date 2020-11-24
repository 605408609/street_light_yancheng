package com.exc.street.light.sl.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.LampStrategyAction;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.resource.vo.req.SlControlSystemDeviceVO;
import com.exc.street.light.resource.vo.resp.SlRespLampGroupSingleParamVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamRespVO;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: HuangJinHao
 */
public interface SingleLampParamService extends IService<SingleLampParam> {

    /**
     * 根据id查询创建时间距离当前最近的对象
     * @param deviceId
     * @return
     */
    SingleLampParam getlastTimeByDeviceId(Integer deviceId);

    /**
     * @explain 查询所有灯具
     * @author Longshuangyang
     * @time 2020-03-16
     */
    Result<List<SingleLampParamRespVO>> getList(String deviceName, HttpServletRequest request);

    /**
     * 单灯操作
     *
     * @param slControlSystemDeviceVOList
     * @return
     */
    Result singleLampControl(List<SlControlSystemDeviceVO> slControlSystemDeviceVOList);

    /**
     * 单灯操作（顺舟灯具）
     *
     * @param slControlSystemDeviceVO
     * @return
     */
    Result controlByShuncom(SlControlSystemDeviceVO slControlSystemDeviceVO);

    /**
     * 根据deviceId删除灯具信息
     *
     * @param deviceIdList
     * @return
     */
    Result deleteByDeviceId(List<Integer> deviceIdList);

    /**
     * 修改单个数据
     *
     * @param singleLampParam
     */
    void updateOne(SingleLampParam singleLampParam);

    /**
     * 根据Id集合获取SingleLampParam集合
     *
     * @param SingleLampParamIdList
     * @return
     */
    List<SingleLampParam> getListByIdList(List<Integer> SingleLampParamIdList);

    /**
     * 根据id获取SingleLamp
     *
     * @return
     */
    SingleLampParam getSingleLampById(Integer id);

    /**
     * 根据deviceNum获取SingleLamp
     *
     * @return
     */
    SingleLampParam getSingleLampOne(String deviceNum, Integer routesNum, Integer topicNum);

    /**
     * 根据deviceId获取SingleLamp
     *
     * @return
     */
    List<SingleLampParam> getSingleLampByDeviceId(Integer deviceId, String name);

    /**
     * 根据deviceId获取SingleLamp
     *
     * @return
     */
    List<SingleLampParam> getSingleLampLikeName(Integer deviceId, String name);

    /**
     * 根据deviceId获取SingleLamp集合
     *
     * @return
     */
    List<SingleLampParam> getSingleLampByDeviceIds(List<Integer> deviceIds);


    /**
     * OTA升级准备
     *
     * @param ADR
     * @return
     */
    Result ota(String ADR, String sendMode, String sendId);

    /**
     * 新协议下发策略
     * @param deviceGroupingByFlag
     * @param lampStrategyActionList
     * @param scene
     * @return
     */
    boolean setStrategy(List<List<SlControlSystemDeviceVO>> deviceGroupingByFlag, List<LampStrategyAction> lampStrategyActionList,Integer scene);

    /**
     * 下发策略
     *
     * @param lampStrategyActionList
     * @return
     */
    //boolean setStrategy(List<LampStrategyAction> lampStrategyActionList);


    /**
     * 新增数据
     *
     * @param singleLampParam
     */
    Integer add(SingleLampParam singleLampParam);

    Result getList();


    /**
     * 设置经纬度信息
     * @param longitude
     * @param latitude
     * @param systemDevice
     */
    Boolean longitudeAndLatitudeSetting(String longitude, String latitude,SystemDevice systemDevice);

    /**
     * 设置ip信息
     * @param ip
     * @param systemDevice
     */
    Boolean ipSetting(String ip,SystemDevice systemDevice);

    /**
     * 设置报警电压
     * @param voltage
     * @param systemDevice
     * @return
     */
    Boolean alarmVoltage(int voltage,SystemDevice systemDevice);

    /**
     * 设置报警电流
     * @param current
     * @param systemDevice
     * @return
     */
    Boolean alarmCurrent(int current,SystemDevice systemDevice);

    /**
     * 设置报警温度
     * @param temperature
     * @param systemDevice
     * @return
     */
    Boolean alarmTemperature(int temperature,SystemDevice systemDevice);

    /**
     * 设置主动上报时间间隔
     * @param interval
     * @param systemDevice
     * @return
     */
    Boolean interval(int interval,SystemDevice systemDevice);

    /**
     * 设置漏电告警差值
     * @param leakageCurrent
     * @param systemDevice
     * @return
     */
    Boolean leakageCurrent(int leakageCurrent,SystemDevice systemDevice);

    /**
     * 设置灯杆倾斜角度
     * @param tiltAngle
     * @param systemDevice
     * @return
     */
    Boolean tiltAngle(int tiltAngle,SystemDevice systemDevice);

    /**
     * 所有参数清零
     * @param systemDeviceList
     * @return
     */
    Boolean parameterClear(List<SystemDevice> systemDeviceList);

    /**
     * 设置阈值
     * @param paramName
     * @param paramValue
     * @param systemDevice
     * @return
     */
    Result setParam(String paramName,String paramValue,SystemDevice systemDevice);

    /**
     * 根据分区id查询所有灯具列表
     *
     * @param areaId
     * @return
     */
    List<SingleLampParam> listByAreaId(Integer areaId);

    /**
     * 根据灯具分组id和灯具id集合查询灯具列表
     * @param lampGroupId
     * @param singleLampIdList
     * @return
     */
    List<SlRespLampGroupSingleParamVO> listByLampGroupIdAndSingleIdList(Integer lampGroupId, List<Integer> singleLampIdList);
}
