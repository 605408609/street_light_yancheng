package com.exc.street.light.electricity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.electricity.Scene;
import com.exc.street.light.resource.entity.electricity.CanScene;
import com.exc.street.light.resource.qo.electricity.CanSceneQueryObject;
import com.exc.street.light.resource.vo.electricity.CanSceneListVO;
import com.exc.street.light.resource.vo.electricity.SiteSceneVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 强电场景服务接口
 *
 * @author Linshiwen
 * @date 2018/05/28
 */
public interface CanSceneService extends IService<CanScene> {

    /**
     * 新增场景
     *
     * @param scene
     * @param request
     * @return
     */
    Result add(int isSend, Scene scene, HttpServletRequest request);

    /**
     * 根据场景id查询
     *
     * @param sid 场景编码
     * @param nid 节点id
     * @return
     */
    CanScene selectBySidAndNid(Integer sid, Integer nid);

    /**
     * 场景选择节点列表
     *
     * @param qo
     * @param request
     * @return
     */
    Result query(CanSceneQueryObject qo, HttpServletRequest request);

    /**
     * 根据场景名称查询
     *
     * @param qo
     * @param request
     * @return 结果集
     */
    Result listByName(CanSceneQueryObject qo, HttpServletRequest request);

    /**
     * 根据站点id查询
     *
     * @param siteId
     * @return
     */
    Result findBySiteId(Integer siteId);

    /**
     * 添加站点场景
     *
     * @param vo
     * @param request
     * @return
     */
    Result addSite(SiteSceneVO vo, HttpServletRequest request);

    /**
     * 判断节点的场景名称是否重复
     *
     * @param nid
     * @param name
     * @return
     */
    Result check(Integer nid, String name);

    /**
     * 编辑场景
     *
     * @param scene   场景接收对象
     * @param request
     * @return 处理结果
     */
    Result modify(Scene scene, HttpServletRequest request);

    /**
     * 删除场景
     *
     * @param sn
     * @param nid
     * @param request
     * @return 处理结果
     */
    Result delete(Integer sn, Integer nid, HttpServletRequest request);

    /**
     * 删除指定参数下的场景
     *
     * @param cycleType
     * @param type
     * @param sceneName
     * @param date
     * @param request
     * @return
     */
    Result deleteByDateName(Integer cycleType, Integer type, String sceneName, String date, HttpServletRequest request);

    /**
     * 删除场景通过nid
     *
     * @param nid
     * @return
     */
    Result deleteByNid(Integer nid, HttpServletRequest request);
}
