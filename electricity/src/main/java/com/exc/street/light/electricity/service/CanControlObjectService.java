package com.exc.street.light.electricity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.entity.electricity.CanControlObject;

import java.util.List;


/**
 * 强电控制对象服务接口
 *
 * @author Linshiwen
 * @date 2018/05/28
 */
public interface CanControlObjectService extends IService<CanControlObject> {
    /**
     * 批量新增控制对象
     *
     * @param controlObjects
     * @return
     */
    List<CanControlObject> patchSave(List<ControlObject> controlObjects);

    /**
     * 根据场景id删除控制对象
     *
     * @param sid
     */
    void deleteBySid(Integer sid);

    /**
     * 根据sid和nid查询场景下的回路集合
     *
     * @param nid
     * @param sid
     * @return
     */
    List<ControlObject> selectBySceneIdAndNid(Integer nid, Integer sid);
}
