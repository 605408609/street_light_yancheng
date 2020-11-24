package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.electricity.CanScene;
import com.exc.street.light.resource.qo.electricity.CanSceneQueryObject;
import com.exc.street.light.resource.vo.electricity.CanSceneNameVO;
import com.exc.street.light.resource.vo.electricity.CanSceneNodeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 强电场景Mapper
 *
 * @author Linshiwen
 * @date 2018/5/28
 */
public interface CanSceneMapper extends BaseMapper<CanScene> {

    /**
     * 根据场景编号和节点id查询
     *
     * @param sid 场景编号
     * @param nid 节点id
     * @return 查询数量
     */
    CanScene selectBySidAndNid(@Param("sid") Integer sid, @Param("nid") Integer nid);

    /**
     * 根据场景名称和节点id查询
     *
     * @param name 场景名称
     * @param nid  节点id
     * @return 查询数量
     */
    CanScene selectByNameAndNid(@Param("name") String name, @Param("nid") Integer nid);

    /**
     * 场景选择节点列表
     *
     * @param qo
     * @return
     */
    List<CanScene> query(CanSceneQueryObject qo);

    /**
     * 场景选择节点分页列表
     * @param page
     * @param qo
     * @return
     */
    IPage<CanSceneNodeVO> getPageList(IPage<CanSceneNodeVO> page, @Param("qo") CanSceneQueryObject qo);

    /**
     * 根据场景名称和分区id查询场景数量
     *
     * @param name
     * @param areaId
     * @return
     */
    int countByName(@Param("name") String name, @Param("areaId") Integer areaId);

    /**
     * 根据场景名称和分区id查询场景
     *
     * @param qo
     * @return
     */
    List<CanScene> selectByName(CanSceneQueryObject qo);

    /**
     * 根据场景名称查询集控列表
     * @param qo
     * @return
     */
    List<CanSceneNameVO> getNodeListBySceneName(@Param("qo") CanSceneQueryObject qo);

    /**
     * 根据节点id查询
     *
     * @param nid
     * @return
     */
    List<Integer> findByNid(Integer nid);

    /**
     * 根据区域id查询节点ID
     *
     * @param areaId 区域id
     * @return
     */
    List<Integer> selectByAreaId(@Param("areaId") Integer areaId);

    /**
     * 根据nid查询场景
     *
     * @param nid
     * @return
     */
    List<CanScene> selectByNid(@Param("nid") Integer nid);

}