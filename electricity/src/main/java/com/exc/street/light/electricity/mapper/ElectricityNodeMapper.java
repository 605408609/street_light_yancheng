package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.dto.electricity.AreaDTO;
import com.exc.street.light.resource.dto.electricity.LongitudeAndLatitude;
import com.exc.street.light.resource.dto.electricity.Online;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import com.exc.street.light.resource.qo.electricity.ElectricityNodeQueryObject;
import com.exc.street.light.resource.vo.electricity.ElectricityNodeListVO;
import com.exc.street.light.resource.vo.electricity.RespElectricityNodeVO;
import com.exc.street.light.resource.vo.resp.electricity.RespLampPostVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 强电节点mapper接口
 *
 * @author Linshiwen
 * @date 2018/5/22
 */
public interface ElectricityNodeMapper extends BaseMapper<ElectricityNode> {

    /**
     * 更新节点状态
     *
     * @param online
     */
    void updateNodeOnlineState(Online online);


    /**
     * 根据网关id获取灯杆名称
     *
     * @param nid
     * @return
     */
    String getLampPostNameByNid(Integer nid);


    /**
     * 根据条件查询
     *
     * @param qo 查询对象
     * @return 查询结果集
     */
    List<ElectricityNode> query(ElectricityNodeQueryObject qo);

    /**
     * 根据条件查询分页集控列表
     *
     * @param page
     * @param qo
     * @return
     */
    IPage<ElectricityNodeListVO> getPageList(Page<ElectricityNodeListVO> page, @Param("qo") ElectricityNodeQueryObject qo);

    /**
     * 根据节点编号查询
     *
     * @param num 节点编号
     * @return 查询结果
     */
    ElectricityNode selectByNum(String num);

    /**
     * 根据集控id获取区域信息
     *
     * @param nid
     * @return
     */
    AreaDTO getAreaInfoByNid(Integer nid);

    /**
     * 统计节点个数
     *
     * @param partitionId
     * @return
     */
    int getNodeCount(@Param("partitionId") Integer partitionId);

    /**
     * 统计在线节点个数
     *
     * @param partitionId
     * @return
     */
    int countOnLineNum(@Param("partitionId") Integer partitionId);

    /**
     * 根据分区id查询所有节点
     *
     * @param partitionId
     * @return
     */
    List<ElectricityNode> listAll(Integer partitionId);

    /**
     * 根据分区id和节点名称查询
     *
     * @param partitionId
     * @param name
     * @return
     */
    List<ElectricityNode> selectByPid(@Param("partitionId") Integer partitionId, @Param("name") String name);

    /**
     * 根据站点id和节点名称查询
     *
     * @param siteId
     * @param name
     * @return
     */
    List<ElectricityNode> selectBySiteId(@Param("siteId") Integer siteId, @Param("name") String name);

    /**
     * 根据建筑物id查询
     *
     * @param buildingId
     * @return
     */
    List<ElectricityNode> selectByBuildingId(Integer buildingId);


    /**
     * 更新经纬度
     *
     * @param longitudeAndLatitude 经纬度对象
     */
    void updateLongitudeAndLatitudeByNum(LongitudeAndLatitude longitudeAndLatitude);

    /**
     * 根据区域id获取灯杆及其绑定的路灯网关信息
     *
     * @param areaId 区域id
     * @return
     */
    List<RespLampPostVO> getLampPostInfoByAreaId(@Param("areaId") Integer areaId);

    /**
     * 根据区域id获取网关信息
     * @param areaId
     * @return
     */
    List<RespElectricityNodeVO> getRespPullDownList(@Param("areaId") Integer areaId);
}