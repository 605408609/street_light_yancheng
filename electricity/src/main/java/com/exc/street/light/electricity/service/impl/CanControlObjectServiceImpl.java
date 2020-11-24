package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanControlObjectMapper;
import com.exc.street.light.electricity.service.CanControlObjectService;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.entity.electricity.CanControlObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Linshiwen
 * @date 2018/05/28
 */
@Service
public class CanControlObjectServiceImpl extends ServiceImpl<CanControlObjectMapper, CanControlObject> implements CanControlObjectService {
    @Autowired
    private CanControlObjectMapper canControlObjectMapper;

    @Override
    public List<CanControlObject> patchSave(List<ControlObject> controlObjects) {
        List<CanControlObject> canControlObjects = new ArrayList<>();
        for (ControlObject controlObject : controlObjects) {
            CanControlObject canControlObject = new CanControlObject();
            BeanUtils.copyProperties(controlObject, canControlObject);
            canControlObjectMapper.insert(canControlObject);
            canControlObjects.add(canControlObject);
        }
        return canControlObjects;
    }

    @Override
    public void deleteBySid(Integer sid) {
        canControlObjectMapper.deleteBySid(sid);
    }

    @Override
    public List<ControlObject> selectBySceneIdAndNid(Integer nid, Integer sid) {
        return canControlObjectMapper.selectBySceneIdAndNid(nid,sid);
    }
}
