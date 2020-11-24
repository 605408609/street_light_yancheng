package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.dto.electricity.ControlChannelTagIdInfo;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.entity.electricity.CanChannel;
import com.exc.street.light.resource.qo.electricity.CanChannelQueryObject;
import com.exc.street.light.resource.vo.electricity.CanChannelListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 回路mapper接口
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
@Mapper
public interface CanChannelMapper extends BaseMapper<CanChannel> {
    /**
     * 查询节点的回路
     *
     * @param nid 节点id
     * @return 查询结果集
     */
    List<CanChannel> getByNid(Integer nid);

    /**
     * 根据tagId和节点id查询
     *
     * @param nid   节点id
     * @param tagId tagId
     * @return 回路对象
     */
    CanChannel getByTagIdAndNid(@Param("nid") Integer nid, @Param("tagId") Integer tagId);

    /**
     * 根据tagId和can设备id更新
     *
     * @param canChannel
     */
    void updateByTagIdAndDid(CanChannel canChannel);

    /**
     * 根据节点编号查询场景回路
     *
     * @param nid 节点编号
     * @return 查询结果集
     */
    List<CanChannel> querySceneChannelByNid(Integer nid);

    /**
     * 根据节点编号集合查询场景回路
     *
     * @param nidList 节点编号集合
     * @return 查询结果集
     */
    List<CanChannel> querySceneChannelByNidList(@Param("nidList") List<Integer> nidList);

    /**
     * 根据条件查询继电器回路
     *
     * @param qo 查询对象
     * @return 结果集
     */
    List<CanChannel> query(CanChannelQueryObject qo);

    /**
     * 根据条件分页查询继电器回路
     * @param page
     * @param qo
     * @return
     */
    IPage<CanChannelListVO> getPageList(IPage<CanChannelListVO> page, @Param("qo") CanChannelQueryObject qo);

    /**
     * 根据tagID和节点id查询
     *
     * @param tagId
     * @param nid
     * @return
     */
    CanChannel selectByTagIdAndNid(@Param("tagId") Integer tagId, @Param("nid") Integer nid);

    /**
     * 根据条件查询
     *
     * @param qo
     * @return
     */
    List<CanChannel> listAll(CanChannelQueryObject qo);

    /**
     * 分页查询所有信息
     * @param page
     * @param qo
     * @return
     */
    IPage<CanChannelListVO> getPageAllList(IPage<CanChannelListVO> page,@Param("qo") CanChannelQueryObject qo);

    /**
     * 根据节点id查询构建ControlObject
     *
     * @param nid
     * @return
     */
    List<ControlObject> selectControlByNid(@Param("nid") Integer nid);

    /**
     * 根据节点id查询可用场景模块
     *
     * @param nid
     * @return
     */
    List<CanChannel> selectSceneByNid(Integer nid);

    /**
     * 通过nid和tagId获取回路的相关信息
     *
     * @param nid
     * @param tagId
     * @return
     */
    List<ControlChannelTagIdInfo> selectInfoByNidAndTagId(@Param("tagId") Integer tagId, @Param("nid") Integer nid);

    /**
     * 根据address和节点id及控制id查询
     *
     * @param controlId
     * @param nid
     * @param address
     * @return
     */
    CanChannel selectByTagIdAndNidAndAddress(@Param("controlId") Integer controlId, @Param("nid") Integer nid, @Param("address") Integer address);

    /**
     * 通过id查找phasePosition
     *
     * @param id
     * @return
     */
    Integer selectPhasePositionById(@Param("id") Integer id);
}