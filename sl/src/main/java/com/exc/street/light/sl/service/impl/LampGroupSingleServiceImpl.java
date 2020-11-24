/**
 * @filename:LampGroupSingleServiceImpl 2020-07-16
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampGroup;
import com.exc.street.light.resource.entity.sl.LampGroupSingle;
import com.exc.street.light.resource.vo.req.SlReqLampGroupVO;
import com.exc.street.light.sl.mapper.LampGroupSingleMapper;
import com.exc.street.light.sl.service.LampGroupSingleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:TODO(灯具分组中间表服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class LampGroupSingleServiceImpl extends ServiceImpl<LampGroupSingleMapper, LampGroupSingle> implements LampGroupSingleService {
    private static final Logger logger = LoggerFactory.getLogger(LampGroupSingleServiceImpl.class);

    @Override
    public Result updateRelation(List<SlReqLampGroupVO> slReqLampGroupVOList, HttpServletRequest request) {
        logger.info("修改灯具分组中间表，更新灯具分组关联关系：{}", slReqLampGroupVOList);
        for (SlReqLampGroupVO slReqLampGroupVO : slReqLampGroupVOList) {
            // 删除之前的关联关系
            LambdaQueryWrapper<LampGroupSingle> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(LampGroupSingle::getLampGroupId, slReqLampGroupVO.getId());
            this.remove(deleteWrapper);
            // 构建新的关联关系对象
            List<LampGroupSingle> lampGroupSingleList = new ArrayList<>();
            List<Integer> singleIdList = slReqLampGroupVO.getSingleIdList();
            if (singleIdList != null && singleIdList.size() > 0) {
                for (Integer singleId : singleIdList) {
                    LampGroupSingle lampGroupSingle = new LampGroupSingle();
                    lampGroupSingle.setLampGroupId(slReqLampGroupVO.getId());
                    lampGroupSingle.setSingleId(singleId);
                    lampGroupSingleList.add(lampGroupSingle);
                }
            }
            // 批量添加关联关系
            this.saveBatch(lampGroupSingleList);
        }
        Result result = new Result();
        return result.success("添加关联关系成功");
    }

    @Override
    public void cancelDevice(LampGroupSingle lampGroupSingle) {
        logger.info("取消设备关联灯具分组");
        LambdaQueryWrapper<LampGroupSingle> wrapper = new LambdaQueryWrapper<LampGroupSingle>();
        wrapper.eq(LampGroupSingle::getLampGroupId, lampGroupSingle.getLampGroupId());
        if (lampGroupSingle.getSingleId() != null) {
            wrapper.eq(LampGroupSingle::getSingleId, lampGroupSingle.getSingleId());
        }
        this.baseMapper.delete(wrapper);
    }
}