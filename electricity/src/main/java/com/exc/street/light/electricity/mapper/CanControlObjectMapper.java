package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.entity.electricity.CanControlObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 场景控制对象Mapper
 *
 * @author Linshiwen
 * @date 2018/5/30
 */
public interface CanControlObjectMapper extends BaseMapper<CanControlObject> {
    /**
     * 根据场景id删除控制对象
     *
     * @param sid
     */
    void deleteBySid(Integer sid);

    List<ControlObject> selectBySceneIdAndNid(@Param("nid") Integer nid, @Param("sceneId") Integer sceneId);
}