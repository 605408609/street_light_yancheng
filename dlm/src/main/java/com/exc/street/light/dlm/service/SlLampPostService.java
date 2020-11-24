/**
 * @filename:SlLampPostService 2020-03-17
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.qo.DlmLampPostQuery;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface SlLampPostService extends IService<SlLampPost> {

    /**
     * 查询灯杆下拉列表
     *
     * @param siteId
     * @param lampPostName
     * @param request
     * @return
     */
    Result pulldown(Integer siteId, Integer lampPostId, Integer deviceTypeNum, String lampPostName, HttpServletRequest request);

    /**
     * 获取带设备基础信息的灯杆详情
     *
     * @param dlmLampPostQuery
     * @param request
     * @return
     */
    Result get(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request);
    /**
     * 获取设备名称列表
     *
     * @param name,type
     * @return
     */
    Result getDevice(String name,int type,HttpServletRequest request);

    /**
     * 获取不带设备基础信息的灯杆详情
     * @param dlmLampPostQuery
     * @param request
     * @return
     */
    Result getNoDetails(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request);

    /**
     * 批量删除灯杆
     *
     * @param ids
     * @param request
     * @return
     */
    Result batchDelete(String ids, HttpServletRequest request);

//    /**
//     * 设备列表
//     *
//     * @param deviceName
//     * @param request
//     * @return
//     */
//    Result device(String deviceName, HttpServletRequest request);

    /**
     * 添加灯杆
     *
     * @param slLampPost
     * @param request
     * @return
     */
    Result add(SlLampPost slLampPost, HttpServletRequest request);

    /**
     * 根据站点id集合查询灯杆集合
     *
     * @param siteIdList
     * @param request
     * @return
     */
    Result getBySite(List<Integer> siteIdList, HttpServletRequest request);

    /**
     * 查询灯杆集合（根据分组id集合get）
     *
     * @param groupIdList
     * @param request
     * @return
     */
    Result getByGroup(List<Integer> groupIdList, HttpServletRequest request);

    /**
     * 构建设备返回对象
     *
     * @param slDeviceResultObj
     * @param deviceType
     * @param slLampPostTemp
     * @return
     */
    DlmRespDevicePublicParVO deviceJsonObjectToPublicParVO(JSONObject slDeviceResultObj, Integer deviceType, SlLampPost slLampPostTemp);

    /**
     * 灯杆分页条件查询
     *
     * @param dlmLampPostQuery
     * @param request
     * @return
     */
    Result getPage(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request);

    /**
     * 取消灯杆和分组的关联关系
     *
     * @param groupId
     */
    void updateToNoGroup(Integer groupId);

    /**
     * 获取各个设备的个数及在线率
     *
     * @param request
     * @return
     */
    Result mapNumber(int version,Integer type,HttpServletRequest request);

    /**
     * 查询灯杆名称
     *
     * @return
     */
    List<String> getSlLampPostName();

    /**
     * 查询所有灯杆信息
     *
     * @return
     */
    List<SlLampPost> getList();

    /**
     * 根据id删除灯杆
     *
     * @param id
     * @param request
     * @return
     */
    Result delete(Long id, HttpServletRequest request);

    /**
     * 取消灯杆关联站点
     *
     * @param siteIdList
     * @param groupIdList
     * @param request
     * @return
     */
    Result cancel(List<Integer> siteIdList, List<Integer> groupIdList, HttpServletRequest request);

    /**
     * 取消设备关联灯杆
     *
     * @param lampPostIdList
     * @param request
     * @return
     */
    Result cancelDevice(List<Integer> lampPostIdList, HttpServletRequest request);

    /**
     * 灯杆验证唯一性
     *
     * @param slLampPost
     * @param request
     * @return
     */
    Result unique(SlLampPost slLampPost, HttpServletRequest request);

    /**
     * 修改灯杆
     *
     * @param slLampPost
     * @param request
     * @return
     */
    Result updateLampPost(SlLampPost slLampPost, HttpServletRequest request);

    /**
     * 根据灯杆id查询灯杆详情
     * @param id
     * @param request
     * @return
     */
    Result getLampPostById(Integer id, HttpServletRequest request);

    /**
     * 获取灯杆默认弹窗的摄像头详情
     * @param dlmLampPostQuery
     * @param request
     * @return
     */
	Result getCamera(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request);
}