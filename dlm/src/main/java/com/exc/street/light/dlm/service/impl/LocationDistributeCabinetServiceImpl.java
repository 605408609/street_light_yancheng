/**
 * @filename:LocationDistributeCabinetServiceImpl 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.LocationDistributeCabinetMapper;
import com.exc.street.light.dlm.service.LocationControlService;
import com.exc.street.light.dlm.service.LocationDistributeCabinetService;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import com.exc.street.light.resource.entity.dlm.LocationDistributeCabinet;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.DlmDistributeCabinetQuery;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.DlmReqLocationDistributeCabinetVO;
import com.exc.street.light.resource.vo.resp.DlmRespDistributeCabinetVO;
import com.exc.street.light.resource.vo.resp.DlmRespDistributeCabinetWithOptionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
public class LocationDistributeCabinetServiceImpl extends ServiceImpl<LocationDistributeCabinetMapper, LocationDistributeCabinet> implements LocationDistributeCabinetService {

    private static final Logger logger = LoggerFactory.getLogger(LocationDistributeCabinetServiceImpl.class);

    @Autowired
    private LocationControlService locationControlService;

    @Autowired
    private LogUserService logUserService;

    @Override
    public Result insertDistributeCabinet(DlmReqLocationDistributeCabinetVO cabinetVO, HttpServletRequest request) {
        logger.info("insertDistributeCabinet - 新增配电柜 cabinetVO=[{}]", cabinetVO);
        // 创建人
        Integer creator = JavaWebTokenUtil.parserStaffIdByToken(request);
        LocationDistributeCabinet cabinet = new LocationDistributeCabinet();
        BeanUtils.copyProperties(cabinetVO, cabinet);
        cabinet.setCreateTime(new Date());
        cabinet.setCreator(creator);
        int result = baseMapper.insert(cabinet);
        if (result < 1) {
            logger.error("新增配电柜失败 cabinetVO=[{}]", cabinetVO);
            return new Result().error("新增配电柜失败");
        }
        return new Result().success("添加成功");
    }

    @Override
    public Result updateDistributeCabinet(DlmReqLocationDistributeCabinetVO cabinetVO, HttpServletRequest request) {
        logger.info("updateDistributeCabinet - 编辑配电柜 cabinetVO=[{}]", cabinetVO);
        LocationDistributeCabinet cabinet = new LocationDistributeCabinet();
        BeanUtils.copyProperties(cabinetVO, cabinet);
        int result = baseMapper.updateById(cabinet);
        if (result < 1) {
            logger.error("编辑配电柜失败 cabinetVO=[{}]", cabinetVO);
            return new Result().error("编辑配电柜失败");
        }
        return new Result().success("编辑成功");
    }

    @Override
    public Result detailOfDistributeCabinet(Integer cabinetId, HttpServletRequest request) {
        logger.info("detailOfDistributeCabinet - 配电柜详情 cabinetId=[{}]", cabinetId);
        DlmRespDistributeCabinetVO dlmRespDistributeCabinetVO =  baseMapper.selectDistributeCabinetByCabinetId(cabinetId);
        Result<DlmRespDistributeCabinetVO> result = new Result<>();
        return result.success(dlmRespDistributeCabinetVO);
    }

    @Override
    public Result deleteDistributeCabinetByCabinetId(Integer cabinetId, HttpServletRequest request) {
        logger.info("deleteDistributeCabinetByCabinetId - 删除配电柜 cabinetId=[{}]", cabinetId);
        int result = baseMapper.deleteById(cabinetId);
        if (result < 1) {
            logger.error("删除配电柜失败 cabinetId=[{}]", cabinetId);
            return new Result().error("删除配电柜失败");
        }
        // 删除关联的集控
        LambdaQueryWrapper<LocationControl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LocationControl::getCabinetId, cabinetId);
        List<LocationControl> locationControlList = locationControlService.list(queryWrapper);
        if (locationControlList != null && locationControlList.size() > 0) {
            locationControlService.deleteOfBatchLocationControl(locationControlList.stream().map(e -> e.getId().toString()).toArray(String[]::new), request);
        }
        return new Result().success("删除成功");
    }

    @Override
    public Result deleteOfBatchDistributeCabinet(String[] cabinetIdList, HttpServletRequest request) {
        logger.info("deleteOfBatchDistributeCabinet - 批量删除配电柜 cabinetIdList=[{}]", cabinetIdList);
        if (cabinetIdList == null || cabinetIdList.length <= 0) {
            return new Result<>().error("删除失败，非法参数");
        }
        List<Integer> idList = Stream.of(cabinetIdList).map(Integer::parseInt).collect(Collectors.toList());
        if (!idList.isEmpty()) {
            int result = baseMapper.deleteBatchIds(idList);
            if (result < 1) {
                logger.error("deleteOfBatchDistributeCabinet - 批量删除配电柜失败 idList=[{}]", idList);
                return new Result().error("批量删除配电柜失败");
            }
            // 删除关联的集控
            LambdaQueryWrapper<LocationControl> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(LocationControl::getCabinetId, idList);
            List<LocationControl> locationControlList = locationControlService.list(queryWrapper);
            if (locationControlList != null && locationControlList.size() > 0) {
                locationControlService.deleteOfBatchLocationControl(locationControlList.stream().map(e -> e.getId().toString()).toArray(String[]::new), request);
            }
        }
        return new Result().success("删除成功");
    }

    @Override
    public Result listDistributeCabinetWithPageByCabinetQuery(DlmDistributeCabinetQuery cabinetQuery, HttpServletRequest request) {
        logger.info("listDistributeCabinetWithPageByCabinetQuery - 分页查询配电柜列表 cabinetQuery=[{}]", cabinetQuery);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        boolean flag = logUserService.isAdmin(userId);
        User user = logUserService.get(userId);
        // 不是超级管理员则根据区域查询
        if (!flag) {
            cabinetQuery.setAreaId(user.getAreaId());
        }
        // 不分页
        if (cabinetQuery.getPageSize() == 0) {
            List<DlmRespDistributeCabinetVO> voList =  baseMapper.selectDistributeCabinetListByCabinetQuery(cabinetQuery);
            Result<List<DlmRespDistributeCabinetVO>> result = new Result<>();
            return result.success(voList);
        } else {
            // 分页查询
            Result<IPage<DlmRespDistributeCabinetVO>> result = new Result<>();
            Page<DlmRespDistributeCabinetVO> page = new Page<>(cabinetQuery.getPageNum(), cabinetQuery.getPageSize());
            return result.success(baseMapper.selectDistributeCabinetWithPageByCabinetQuery(page, cabinetQuery));
        }
    }

    @Override
    public Result listDistributeCabinetWithOptionQuery(HttpServletRequest request) {
        logger.info("listDistributeCabinetWithOptionQuery - 配电柜下拉列表");
        LambdaQueryWrapper<LocationDistributeCabinet> queryWrapper = new LambdaQueryWrapper<>();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        boolean flag = logUserService.isAdmin(userId);
        User user = logUserService.get(userId);
        // 不是超级管理员则根据区域查询
        if (!flag) {
            queryWrapper.eq(LocationDistributeCabinet::getAreaId, user.getAreaId());
        }
        List<DlmRespDistributeCabinetWithOptionVO> voList = new ArrayList<>();
        List<LocationDistributeCabinet> distributeCabinetList = baseMapper.selectList(queryWrapper);
        if (distributeCabinetList != null && distributeCabinetList.size() > 0) {
            for (LocationDistributeCabinet cabinet : distributeCabinetList) {
                DlmRespDistributeCabinetWithOptionVO cabinetWithOptionVO = new DlmRespDistributeCabinetWithOptionVO();
                cabinetWithOptionVO.setId(cabinet.getId());
                cabinetWithOptionVO.setName(cabinet.getName());
                voList.add(cabinetWithOptionVO);
            }
        }
        Result<List<DlmRespDistributeCabinetWithOptionVO>> result = new Result<>();
        return result.success(voList);
    }

    @Override
    public Result nameAndNumUniqueness(Integer id, String name, String num) {
        logger.info("nameAndNumUniqueness - 配电柜名称和编号唯一性校验 id={} name={} num={}", id, name, num);
        Result<Integer> result = new Result<>();
        int isUniqueness = 0;
        LambdaQueryWrapper<LocationDistributeCabinet> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(LocationDistributeCabinet::getName, name);
        LambdaQueryWrapper<LocationDistributeCabinet> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(LocationDistributeCabinet::getNum, num);
        LocationDistributeCabinet cabinetByName = baseMapper.selectOne(wrapper1);
        LocationDistributeCabinet cabinetByNum = baseMapper.selectOne(wrapper2);
        // 编辑时判重
        if (id != null) {
            if (cabinetByName != null && !cabinetByName.getId().equals(id)) {
                isUniqueness = 1;
                return result.error("配电柜名称已存在", isUniqueness);
            }
            if (cabinetByNum != null && !cabinetByNum.getId().equals(id)) {
                isUniqueness = 2;
                return result.error("配电柜编号已存在", isUniqueness);
            }
        } else if (cabinetByName != null){
            isUniqueness = 1;
            return result.error("配电柜名称已存在", isUniqueness);
        } else if (cabinetByNum != null){
            isUniqueness = 2;
            return result.error("配电柜编号已存在", isUniqueness);
        }
        return result.success(isUniqueness);
    }

}