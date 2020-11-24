/**
 * @filename:LampGroupServiceImpl 2020-07-16
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampGroup;
import com.exc.street.light.resource.entity.sl.LampGroupSingle;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.req.SlReqLampGroupVO;
import com.exc.street.light.resource.vo.resp.SlRespLampGroupSingleParamVO;
import com.exc.street.light.resource.vo.resp.SlRespLampGroupSingleVO;
import com.exc.street.light.resource.vo.resp.SlRespSingleVO;
import com.exc.street.light.sl.mapper.LampGroupMapper;
import com.exc.street.light.sl.service.LampGroupService;
import com.exc.street.light.sl.service.LampGroupSingleService;
import com.exc.street.light.sl.service.SingleLampParamService;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:TODO(灯具分组表服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class LampGroupServiceImpl extends ServiceImpl<LampGroupMapper, LampGroup> implements LampGroupService {
    private static final Logger logger = LoggerFactory.getLogger(LampGroupServiceImpl.class);

    @Autowired
    private LampGroupSingleService lampGroupSingleService;
    @Autowired
    private LogUserService logUserService;
    @Autowired
    private SingleLampParamService singleLampParamService;

    @Override
    public Result addLampGroup(SlReqLampGroupVO slReqLampGroupVO, HttpServletRequest request) {
        logger.info("添加灯具分组接收参数：{}", slReqLampGroupVO);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result unique = this.unique(slReqLampGroupVO, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            // 添加灯具分组
            LampGroup lampGroup = new LampGroup();
            BeanUtils.copyProperties(slReqLampGroupVO, lampGroup);
            lampGroup.setCreator(userId);
            lampGroup.setCreateTime(new Date());
            this.save(lampGroup);
            // 修改路灯的分组id关联
            List<SlReqLampGroupVO> slReqLampGroupVOList = new ArrayList<>();
            slReqLampGroupVO.setId(lampGroup.getId());
            slReqLampGroupVOList.add(slReqLampGroupVO);
            lampGroupSingleService.updateRelation(slReqLampGroupVOList, request);
            result.success("添加灯具分组成功");
        } else {
            result = unique;
        }
        return result;
    }

    @Override
    public Result unique(SlReqLampGroupVO slReqLampGroupVO, HttpServletRequest request) {
        logger.info("分组验证唯一性，接收参数：{}", slReqLampGroupVO);
        Result result = new Result();
        if (null != slReqLampGroupVO) {
            if (slReqLampGroupVO.getId() != null) {
                if (slReqLampGroupVO.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<LampGroup> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(LampGroup::getName, slReqLampGroupVO.getName());
                    LampGroup slLampPostByName = this.getOne(wrapperName);
                    if (slLampPostByName != null && !slLampPostByName.getId().equals(slReqLampGroupVO.getId())) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
            } else {
                if (slReqLampGroupVO.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<LampGroup> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(LampGroup::getName, slReqLampGroupVO.getName());
                    LampGroup slLampPostByName = this.getOne(wrapperName);
                    if (slLampPostByName != null) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
            }
        } else {
            return result.error("接收参数为空");
        }
        return result.success("");
    }

    @Override
    public Result<List<SlRespLampGroupSingleVO>> getList(HttpServletRequest request) {
        logger.info("获取灯具分组列表");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        Integer areaId = null;
        if (!logUserService.isAdmin(user.getId())) {
            areaId = user.getAreaId();
        }
        // 分区下所有灯具
        List<SingleLampParam> singleLampParams = singleLampParamService.listByAreaId(areaId);
        // 分区下所有灯具id
        List<Integer> singleLampIdList = singleLampParams.stream().map(SingleLampParam::getId).distinct().collect(Collectors.toList());
        // 分区下所有分组集合
        LambdaQueryWrapper<LampGroupSingle> lampGroupSingleQueryWrapper = new LambdaQueryWrapper<>();
        lampGroupSingleQueryWrapper.in(LampGroupSingle::getSingleId, singleLampIdList);
        List<LampGroupSingle> lampGroupSingleList = lampGroupSingleService.list(lampGroupSingleQueryWrapper);
        // 得到所有分组id
        List<Integer> lampGroupIdList = lampGroupSingleList.stream().map(LampGroupSingle::getLampGroupId).distinct().collect(Collectors.toList());
        // 得到所有分组
        List<SlRespLampGroupSingleVO> slRespLampGroupSingleVOList = new ArrayList<>();
        if (lampGroupIdList != null && lampGroupIdList.size() > 0) {
            Collection<LampGroup> lampGroupList = this.listByIds(lampGroupIdList);
            for (LampGroup lampGroup : lampGroupList) {
                SlRespLampGroupSingleVO slRespLampGroupSingleVO = new SlRespLampGroupSingleVO();
                slRespLampGroupSingleVO.setId(lampGroup.getId());
                slRespLampGroupSingleVO.setName(lampGroup.getName());
                slRespLampGroupSingleVO.setPartId("lampGroup" + lampGroup.getId());
                List<SlRespSingleVO> singleList = new ArrayList<>();
                List<SlRespLampGroupSingleParamVO> respSingleList = singleLampParamService.listByLampGroupIdAndSingleIdList(lampGroup.getId(), singleLampIdList);
                for (SlRespLampGroupSingleParamVO singleLampParam : respSingleList) {
                    SlRespSingleVO slRespSingleVO = new SlRespSingleVO();
                    slRespSingleVO.setId(singleLampParam.getId());
                    slRespSingleVO.setName(singleLampParam.getName() + "<" + singleLampParam.getLampPosition() + ">");
                    slRespSingleVO.setPartId("single" + singleLampParam.getId());
                    singleList.add(slRespSingleVO);
                }
                slRespLampGroupSingleVO.setSingleList(singleList);
                slRespLampGroupSingleVOList.add(slRespLampGroupSingleVO);
            }
        }
        Result<List<SlRespLampGroupSingleVO>> result = new Result<>();
        return result.success(slRespLampGroupSingleVOList);
    }

    @Override
    public Result deleteList(SlReqLampGroupVO slReqLampGroupVO, HttpServletRequest request) {
        logger.info("批量删除灯具分组，接收参数：{}", slReqLampGroupVO.getLampGroupSingleList());
        List<LampGroupSingle> lampGroupSingleList = slReqLampGroupVO.getLampGroupSingleList();
        Result result = new Result();
        for(LampGroupSingle lampGroupSingle : lampGroupSingleList){
            if(lampGroupSingle.getSingleId() == null && lampGroupSingle.getLampGroupId() != null){
                this.removeById(lampGroupSingle.getLampGroupId());
                lampGroupSingleService.cancelDevice(lampGroupSingle);
            }else if(lampGroupSingle.getSingleId() !=null && lampGroupSingle.getLampGroupId() != null){
                // 取消设备关联灯具分组
                lampGroupSingleService.cancelDevice(lampGroupSingle);
            }else{
                return result.error("删除失败");
            }
        }
        return result.success("批量删除成功");
    }
}