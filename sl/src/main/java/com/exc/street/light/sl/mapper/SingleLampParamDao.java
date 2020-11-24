package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.qo.SlLampDeviceQuery;
import com.exc.street.light.resource.vo.resp.SlRespLampDeviceVO;
import com.exc.street.light.resource.vo.resp.SlRespLampGroupSingleParamVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: HuangJinHao
 */
@Component
@Mapper
public interface SingleLampParamDao extends BaseMapper<SingleLampParam> {

    List<SingleLampParamRespVO> getList(@Param("areaId") String deviceName);

    SingleLampParam getSingleLampOne(@Param("deviceNum") String deviceNum,@Param("loopNum") Integer loopNum,@Param("topicNum") Integer topicNum);

    SingleLampParam getSingleLampOneNewLora(@Param("sendId") String sendId,@Param("loopNum") Integer loopNum);

    /**
     * 根据分区id查询灯具列表
     *
     * @param areaId
     */
    List<SingleLampParam> listByAreaId(@Param("areaId") Integer areaId);

    /**
     * 根据灯具分组id和灯具id集合查询灯具列表
     * @param lampGroup
     * @param list
     * @return
     */
    List<SlRespLampGroupSingleParamVO> listByLampGroupIdAndSingleIdList(@Param("lampGroup") Integer lampGroup, @Param("list") List<Integer> list);

    /**
     * 分页查询灯具列表
     * @param page
     * @param slLampDeviceQuery
     * @return
     */
    List<SlRespLampDeviceVO> query(IPage<SlRespLampDeviceVO> page,@Param("slLampDeviceQuery") SlLampDeviceQuery slLampDeviceQuery);

    /**
     * 查询灯具列表
     * @param slLampDeviceQuery
     * @return
     */
    List<SlRespLampDeviceVO> query(@Param("slLampDeviceQuery") SlLampDeviceQuery slLampDeviceQuery);

    /**
     * 根据id查询创建时间距离当前最近的对象
     * @param deviceId
     * @return
     */
    SingleLampParam getlastTimeByDeviceId(Integer deviceId);
}
