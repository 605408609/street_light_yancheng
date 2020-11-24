/**
 * @filename:RadioDeviceService 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.pb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.qo.PbRadioDeviceQueryObject;
import com.exc.street.light.resource.vo.req.PbReqBatchUpdateVolumeVO;
import com.exc.street.light.resource.vo.resp.ImportDeviceResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: LeiJing
 * 
 */
public interface RadioDeviceService extends IService<RadioDevice> {

    /**
     * 新增公共广播设备
     *
     * @param radioDevice
     * @param request
     * @return
     */
    public Result addDevice(RadioDevice radioDevice, HttpServletRequest request);

    /**
     * 编辑公共广播设备
     *
     * @param radioDevice
     * @param request
     * @return
     */
    public Result updateDevice(RadioDevice radioDevice, HttpServletRequest request);

    /**
     * 删除公共广播设备
     *
     * @param deviceId
     * @param request
     * @return
     */
    public Result deleteDevice(Integer deviceId, HttpServletRequest request);

    /**
     * 批量删除公共广播设备
     *
     * @param idList
     * @return
     */
    public Result batchDeleteDevice(List<Integer> idList, HttpServletRequest request);

    /**
     * 公共广播设备详情
     *
     * @param deviceId
     * @param request
     * @return
     */
    public Result getDeviceInfo(Integer deviceId, HttpServletRequest request);

    /**
     * 公共广播设备列表
     *
     * @param qo
     * @param request
     * @return
     */
    public Result getDeviceList(PbRadioDeviceQueryObject qo, HttpServletRequest request);

    /**
     * 公共广播设备批量导入
     *
     * @param file
     * @param request
     * @return
     */
    public Result<ImportDeviceResultVO> batchImportDevice(MultipartFile file, HttpServletRequest request);

    /**
     * 唯一性校验
     *
     * @param radioDevice
     * @param request
     * @return
     */
    public Result uniqueness(RadioDevice radioDevice, HttpServletRequest request);

    /**
     * 公共广播设备下拉列表
     *
     * @param request
     * @return
     */
    public Result getDevicePulldownList(HttpServletRequest request);

    /**
     * 根据灯杆id集合查询公共广播设备
     *
     * @param lampPostIdList
     * @param request
     * @return
     */
    public Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request);

    /**
     * 批量调节公共广播设备音量
     *
     * @param pbReqBatchUpdateVolumeVO
     * @param request
     * @return
     */
    public Result updateDeviceVolume(PbReqBatchUpdateVolumeVO pbReqBatchUpdateVolumeVO, HttpServletRequest request);

    /**
     * 获取查询对象
     * @param request
     * @return
     */
    public PbRadioDeviceQueryObject getDeviceQueryObject(HttpServletRequest request);
    /**
     * 定时刷新设备状态
     */
    public void updateDeviceStatus();

    public Result getTermState(Integer termId,HttpServletRequest request);

    /**
     * 根据设备id获取正在播放的节目
     * @param id
     * @param request
     * @return
     */
    public Result getPlayingProgram(Integer id,HttpServletRequest request);

    /**
     * 根据灯杆分组id集合查询下属的设备id集合
     * @param groupIdList 灯杆分组id集合
     * @return 广播设备id集合
     */
    public List<Integer> getIdListByGroupIdList(List<Integer> groupIdList);
}