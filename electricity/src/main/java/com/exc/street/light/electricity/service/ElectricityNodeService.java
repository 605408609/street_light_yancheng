package com.exc.street.light.electricity.service;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.electricity.AreaDTO;
import com.exc.street.light.resource.dto.electricity.LongitudeAndLatitude;
import com.exc.street.light.resource.dto.electricity.Online;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import com.exc.street.light.resource.qo.electricity.ElectricityNodeQueryObject;
import com.exc.street.light.resource.vo.electricity.ElectricityNodeImportVO;
import com.exc.street.light.resource.vo.electricity.ElectricityNodeVO;
import com.exc.street.light.resource.vo.electricity.RespElectricityNodeVO;
import com.exc.street.light.resource.vo.req.electricity.ReqElectricityNodeUniquenessVO;
import com.exc.street.light.resource.vo.resp.ImportDeviceResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 路灯网关服务接口
 *
 * @author Linshiwen
 * @date 2018/5/21
 */
public interface ElectricityNodeService extends IService<ElectricityNode> {

    /**
     * 更新状态
     *
     * @param online
     */
    void updateNodeOnlineState(Online online);

    /**
     * 路灯网关唯一性校验
     * @param uniquenessVO
     * @param request
     * @return
     */
    Result<Integer> uniqueness(ReqElectricityNodeUniquenessVO uniquenessVO, HttpServletRequest request);

    /**
     * 根据excel文件批量导入路灯网关
     *
     * @param file
     * @param request
     * @return
     */
    Result<ImportDeviceResultVO> batchImport(MultipartFile file, HttpServletRequest request);

    /**
     * 根据条件查询
     *
     * @param request HttpServletRequest对象
     * @param qo      查询参数
     * @return 查询结果
     */
    Result query(HttpServletRequest request, ElectricityNodeQueryObject qo);

    /**
     * 根据id查询
     *
     * @param id 强电id
     * @return 查询结果
     */
    ElectricityNodeVO getById(Integer id);

    /**
     * 根据集控id获取区域对象
     *
     * @param nid
     * @return
     */
    AreaDTO getAreaById(Integer nid);

    /**
     * 编辑路灯网关
     *
     * @param electricityNode 路灯网关信息
     * @param request
     * @return 编辑结果
     */
    Result modify(ElectricityNode electricityNode, HttpServletRequest request);

    /**
     * 新增路灯网关
     *
     * @param electricityNode 路灯网关信息
     * @param request
     * @return 新增结果
     */
    Result add(ElectricityNode electricityNode, HttpServletRequest request);

    /**
     * 新增集控路灯网关
     *
     * @param electricityNode 路灯网关信息
     * @param request
     * @return 新增结果
     */
    Result addControl(ElectricityNode electricityNode, HttpServletRequest request);


    /**
     * Excel导入路灯网关
     *
     * @param nodes   路灯网关集合
     * @param request
     * @return 处理结果
     */
    Result importNode(List<ElectricityNodeImportVO> nodes, HttpServletRequest request);

    /**
     * 获取路灯网关
     *
     * @param nid
     * @return
     */
    ElectricityNode get(Integer nid);


    /**
     * 统计路灯网关
     *
     * @param request
     * @return
     */
    Result count(HttpServletRequest request);

    /**
     * 统计分区路灯网关数量
     *
     * @param partitionId
     * @return
     */
    int getNodeCount(Integer partitionId);

    /**
     * 统计分区路灯网关在线数量
     *
     * @param partitionId
     * @return
     */
    int countOnLineNum(Integer partitionId);

    /**
     * 获取所有路灯网关信息
     *
     * @param request
     * @return
     */
    Result listAll(HttpServletRequest request);

    /**
     * 根据分区id和路灯网关名称查询
     *
     * @param partitionId
     * @param name
     * @return
     */
    List<ElectricityNode> findByPid(Integer partitionId, String name);

    /**
     * 根据站点id和路灯网关名称查询
     *
     * @param siteId
     * @param name
     * @return
     */
    List<ElectricityNode> findBySiteId(Integer siteId, String name);

    /**
     * 根据建筑物id查询
     *
     * @param buildingId
     * @return
     */
    List<ElectricityNode> findByBuildingId(Integer buildingId);

    /**
     * 删除路灯网关
     *
     * @param id      路灯网关id
     * @param request
     * @return 删除结果
     */
    Result delete(Integer id, HttpServletRequest request);

    /**
     * 批量删除路灯网关
     * @param ids 路灯网关集合
     * @param request
     * @return
     */
    Result batchDelete(String ids, HttpServletRequest request);

    /**
     * 路灯网关列表场景信息查询
     *
     * @param request HttpServletRequest对象
     * @param qo      查询参数
     * @return 查询结果
     */
    Result sceneList(HttpServletRequest request, ElectricityNodeQueryObject qo);

    /**
     * 根据路灯网关id连接强电网关并搜索设备信息
     *
     * @param nid
     * @return
     */
    Result searchNodeInfoById(Integer nid, HttpServletRequest request);

    /**
     * 根据路灯网关编号修改经纬度
     *
     * @param request
     * @param longitudeAndLatitude
     * @return
     */
    Result updateLongitudeAndLatitude(HttpServletRequest request, LongitudeAndLatitude longitudeAndLatitude);

    /**
     * 获取经纬度
     *
     * @param request
     * @return
     */
    Result getLongitudeAndLatitude(Integer id, HttpServletRequest request);

    /**
     * 网关下拉列表
     * @param request
     * @return
     */
    Result<JSONArray> tree(HttpServletRequest request);
}
