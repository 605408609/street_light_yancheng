package com.exc.street.light.electricity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.CanTiming;
import com.exc.street.light.resource.vo.electricity.CanTimingVO;
import com.exc.street.light.resource.vo.electricity.TimerPatchVO;
import com.exc.street.light.resource.vo.electricity.TimerVO;
import com.exc.street.light.resource.vo.req.electricity.ReqElectricityScriptVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 强电定时器服务接口
 *
 * @author Linshiwen
 * @date 2018/05/31
 */
public interface CanTimingService extends IService<CanTiming> {
    /**
     * 新增定时器(节点选择场景)
     *
     * @param timerVO
     * @param request
     * @return
     */
    Result add(TimerVO timerVO, HttpServletRequest request);

    /**
     * 根据场景编号和节点id查询
     *
     * @param sid 场景编号
     * @param nid 节点id
     * @return
     */
    List<CanTiming> selectBySidAndNid(Integer sid, Integer nid);

    /**
     * 根据场景编号和节点id删除
     *
     * @param sid
     * @param nid
     */
    void deleteBySidAndNid(Integer sid, Integer nid);

    /**
     * 根据节点统计按时执行的定时器数量
     *
     * @param nid 节点id
     * @return 数量
     */
    int countTimingNumByNid(Integer nid);

    /**
     * 根据节点统计周期执行的定时器数量
     *
     * @param nid 节点id
     * @return 数量
     */
    int countCycleNumByNid(Integer nid);

    /**
     * 新增定时器(场景选择节点)
     *
     * @param timerPatchVO
     * @param request
     * @return
     */
    Result patchAdd(TimerPatchVO timerPatchVO, HttpServletRequest request);

    /**
     * 根据节点id查询定时器数量
     *
     * @param nid 节点id
     * @return
     */
    List<CanTimingVO> findByNid(Integer nid);

    /**
     * 成对删除
     *
     * @param id
     * @param request
     * @return
     */
    Result delete2ById(Integer id, HttpServletRequest request);

    /**
     * 新增站点日程定时器
     *
     * @param vo
     * @param request
     * @return
     */
    Result addSite(ReqElectricityScriptVO vo, HttpServletRequest request);

    /**
     * 清空节点定时
     *
     * @param nid
     * @param request
     * @return
     */
    Result clear(Integer nid, HttpServletRequest request);

    /**
     * 清空站点定时
     *
     * @param siteId
     * @param request
     * @return
     */
    Result clearSite(Integer siteId, HttpServletRequest request);

    /**
     * 清空分区定时
     *
     * @param partitionId
     * @param request
     * @return
     */
    Result clearPartition(Integer partitionId, HttpServletRequest request);

    /**
     * 清空所有定时
     *
     * @param request
     * @return
     */
    Result clearAll(HttpServletRequest request);

    /**
     * 根据名称删除定时
     *
     * @param nid
     * @param sceneName
     * @return
     */
    Result deleteTiming(Integer nid, Integer cycleType, Integer type, String sceneName);

    /**
     * 根据pid删除节点定时
     *
     * @param pid
     * @param nid
     * @return
     */
    Result deleteTimingByPid(Integer pid, Integer nid);

    /**
     * 改变状态
     *
     * @param id
     * @return
     */
    Result changeExecuteState(Integer nid, Integer id, Integer isExecute);

    /**
     * 改变节点多个定时执行状态
     *
     * @param nid
     * @param pids
     * @return
     */
    Result changeExecuteStates(Integer nid, List<Integer> pids, Integer isExecute);

    /**
     * 通过pid查找
     *
     * @param pid
     * @return
     */
    List<CanTiming> selectByPid(Integer pid);

    /**
     * 通过pid和nid查找
     *
     * @param pid
     * @return
     */
    List<CanTiming> selectByPidAndNid(Integer pid, Integer nid);

    /**
     * 通过
     *
     * @param id
     * @return
     */
    List<CanTiming> deleteByTimingId(Integer id);
}
