package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.electricity.CanTiming;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 强电定时器Mapper
 *
 * @author Linshiwen
 * @date 2018/5/31
 */
public interface CanTimingMapper extends BaseMapper<CanTiming> {
    /**
     * 根据场景编号和节点id查询
     *
     * @param sid 场景编号
     * @param nid 节点id
     * @return
     */
    List<CanTiming> selectBySidAndNid(@Param("sid") Integer sid, @Param("nid") Integer nid);

    /**
     * 根据定时类型和节点id查询
     *
     * @param type
     * @param nid
     * @return
     */
    List<CanTiming> selectByTypeAndNid(@Param("type") int type, @Param("nid") Integer nid);

    /**
     * 根据场景编号和节点id删除
     *
     * @param sid
     * @param nid
     */
    void deleteBySidAndNid(@Param("sid") Integer sid, @Param("nid") Integer nid);

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
     * 根据节点编号查询
     *
     * @param nid
     * @return
     */
    List<CanTiming> findByNid(Integer nid);

    /**
     * 根据定时器名称和场景tagID查询
     *
     * @param name
     * @param sid
     * @return
     */
    CanTiming selectByNameAndSid(@Param("name") String name, @Param("sid") Integer sid);

    /**
     * 根据名称和场景id删除
     *
     * @param name
     * @param sid
     */
    void deleteByNameAndSid(@Param("name") String name, @Param("sid") Integer sid);

    /**
     * 根据名称查询
     *
     * @param name
     * @return
     */
    List<CanTiming> selectByName(String name);

    /**
     * 根据名称删除
     *
     * @param name
     */
    void deleteByName(String name);

    /**
     * 根据节点id和定时时间查询类型为定时的场景
     *
     * @param nid
     * @param date
     * @return
     */
//    List<CanTiming> selectByNidAndDate(@Param("nid") Integer nid, @Param("date") Date date);

    /**
     * 根据定时时间查询类型为定时的场景
     *
     * @param date
     * @return
     */
//    List<CanTiming> selectByDate(@Param("date") Date date);

    /**
     * 刪除类型为定时的场景
     *
     * @param id
     */
    void deleteCanTiming(@Param("id") Integer id);

    /**
     * 根据节点id删除
     *
     * @param nid
     */
    void deleteByNid(Integer nid);

    /**
     * 根据场景名称、类型查询定时对象
     *
     * @param elId
     * @param cycleType
     * @param type
     * @param sceneName
     * @return
     */
    List<CanTiming> selectByTypeAndName(@Param("elId") Integer elId, @Param("cycleType") Integer cycleType,
                                        @Param("type") Integer type, @Param("sceneName") String sceneName
    );

    /**
     * 通过pid查找
     *
     * @param pid
     * @return
     */
    List<CanTiming> selectByPid(@Param("pid") Integer pid);

    /**
     * 通过pid和nid查找
     *
     * @param pid
     * @return
     */
    List<CanTiming> selectByPidAndNid(@Param("pid") Integer pid, @Param("nid") Integer nid);
}