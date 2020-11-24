/**
 * @filename:RadioProgramServiceImpl 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2018 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.pb.config.HttpApi;
import com.exc.street.light.pb.mapper.RadioProgramDao;
import com.exc.street.light.pb.service.RadioMaterialService;
import com.exc.street.light.pb.service.RadioPlayService;
import com.exc.street.light.pb.service.RadioProgramMaterialService;
import com.exc.street.light.pb.service.RadioProgramService;
import com.exc.street.light.pb.utils.DateUtil;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.pb.RadioMaterial;
import com.exc.street.light.resource.entity.pb.RadioPlay;
import com.exc.street.light.resource.entity.pb.RadioProgram;
import com.exc.street.light.resource.entity.pb.RadioProgramMaterial;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.PbRadioProgramQueryObject;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.PbRespMaterialSizeVO;
import com.exc.street.light.resource.vo.req.PbReqRadioProgramMaterialVO;
import com.exc.street.light.resource.vo.req.PbReqRadioProgramVO;
import com.exc.street.light.resource.vo.req.PbReqVerifyProgramVO;
import com.exc.street.light.resource.vo.resp.PbRespRadioProgramVO;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version: V1.0
 * @author: LeiJing
 */
@Service
public class RadioProgramServiceImpl extends ServiceImpl<RadioProgramDao, RadioProgram> implements RadioProgramService {
    private static final Logger logger = LoggerFactory.getLogger(RadioProgramServiceImpl.class);
    @Autowired
    private HttpApi httpApi;

    @Autowired
    private RadioProgramDao radioProgramDao;

    @Autowired
    private RadioProgramMaterialService radioProgramMaterialService;

    @Autowired
    private RadioMaterialService radioMaterialService;

    @Autowired
    private RadioPlayService radioPlayService;

    @Autowired
    private LogUserService userService;

    @Override
    public Result add(PbReqRadioProgramVO pbReqRadioProgramVO, HttpServletRequest request) {
        logger.info("新增公共广播节目，接收参数：pbReqRadioProgramVO = {}", pbReqRadioProgramVO);
        Result result = new Result();
        //获取用户id
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        if (userId == null) {
            return result.error("请先登录");
        }
        //校验名称
        RadioProgram programValidate = new RadioProgram();
        BeanUtils.copyProperties(pbReqRadioProgramVO, programValidate);
        Result<Integer> uniquenessRes = uniqueness(programValidate, request);
        if (uniquenessRes.getData() == null || !uniquenessRes.getData().equals(0)) {
            return result.error(uniquenessRes.getMessage());
        }
        RadioProgram radioProgram = new RadioProgram();
        BeanUtils.copyProperties(pbReqRadioProgramVO, radioProgram);
        radioProgram.setCapacity(0F);
        radioProgram.setDuration(0);
        radioProgram.setCreator(userId);
        //设置默认未审核
        radioProgram.setVerifyStatus(0);
        radioProgram.setCreateTime(new Date());
        radioProgramDao.insert(radioProgram);
        //添加节目和素材关联
        int order = 1;
        //做排序
        for (Integer radioMaterialId : pbReqRadioProgramVO.getRadioMaterialIdList()) {
            RadioProgramMaterial radioProgramMaterial = new RadioProgramMaterial();
            radioProgramMaterial.setCreateTime(new Date());
            radioProgramMaterial.setMaterialId(radioMaterialId);
            radioProgramMaterial.setMaterialOrder(order++);
            radioProgramMaterial.setProgramId(radioProgram.getId());
            radioProgramMaterialService.save(radioProgramMaterial);
        }
        //刷新节目的时长
        refreshProgramDurationAndSize(radioProgram.getId());
        return result.success("新增公共广播节目成功");
    }

    @Override
    public Result update(PbReqRadioProgramVO pbReqRadioProgramVO, HttpServletRequest request) {
        logger.info("编辑公共广播节目，接收参数：pbReqRadioProgramVO = {}", pbReqRadioProgramVO);
        Result result = new Result();
        //判断是否可为修改状态
        RadioProgram program = this.getById(pbReqRadioProgramVO.getId());
        if (program == null) {
            return result.error("修改失败,未找到节目记录");
        }
        if (program.getVerifyStatus() != null && program.getVerifyStatus().equals(1)) {
            return result.error("修改失败,当前节目不允许修改");
        }
        //校验名称
        RadioProgram programValidate = new RadioProgram();
        BeanUtils.copyProperties(pbReqRadioProgramVO, programValidate);
        Result<Integer> uniquenessRes = uniqueness(programValidate, request);
        if (uniquenessRes.getData() == null || !uniquenessRes.getData().equals(0)) {
            return result.error(uniquenessRes.getMessage());
        }
        //编辑节目
        RadioProgram radioProgram = new RadioProgram();
        BeanUtils.copyProperties(pbReqRadioProgramVO, radioProgram);
        // 审核未通过时,编辑就修改审核状态为待审核
        if (program.getVerifyStatus() != null && program.getVerifyStatus().equals(2)) {
            radioProgram.setVerifyStatus(0);
        }
        radioProgram.setUpdateTime(new Date());
        this.updateById(radioProgram);
        //先删除原有节目和素材关联，再添加节目和素材关联
        LambdaQueryWrapper<RadioProgramMaterial> queryWrapperRadioProgramMaterial = new LambdaQueryWrapper<RadioProgramMaterial>();
        queryWrapperRadioProgramMaterial.eq(RadioProgramMaterial::getProgramId, radioProgram.getId());
        radioProgramMaterialService.remove(queryWrapperRadioProgramMaterial);
        int order = 1;
        //做排序
        for (Integer radioMaterialId : pbReqRadioProgramVO.getRadioMaterialIdList()) {
            RadioProgramMaterial radioProgramMaterial = new RadioProgramMaterial();
            radioProgramMaterial.setCreateTime(new Date());
            radioProgramMaterial.setMaterialId(radioMaterialId);
            radioProgramMaterial.setMaterialOrder(order++);
            radioProgramMaterial.setProgramId(radioProgram.getId());
            radioProgramMaterialService.save(radioProgramMaterial);
        }
        //刷新节目的时长
        refreshProgramDurationAndSize(radioProgram.getId());
        return result.success("编辑公共广播节目成功");
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        logger.info("删除公共广播节目，接收参数：id = {}", id);
        Result result = new Result();
        //删除关联信息
        LambdaQueryWrapper<RadioProgramMaterial> proMaterialWrapper = new LambdaQueryWrapper<>();
        proMaterialWrapper.eq(RadioProgramMaterial::getProgramId, id);
        radioProgramMaterialService.remove(proMaterialWrapper);
        //删除节目
        radioProgramDao.deleteById(id);
        //删除节目的定时任务
        LambdaQueryWrapper<RadioPlay> radioPlayWrapper = new LambdaQueryWrapper<>();
        radioPlayWrapper.eq(RadioPlay::getProgramId, id);
        List<RadioPlay> playList = radioPlayService.list(radioPlayWrapper);
        if (playList != null && !playList.isEmpty()) {
            for (RadioPlay radioPlay : playList) {
                if (radioPlay.getId() == null) {
                    continue;
                }
                radioPlayService.delete(radioPlay.getId(), request);
            }
        }
        return result.success("删除成功");
    }

    @Override
    public Result batchDelete(List<Integer> idList, HttpServletRequest request) {
        Result result = new Result();
        //删除关联信息
        LambdaQueryWrapper<RadioProgramMaterial> proMaterialWrapper = new LambdaQueryWrapper<>();
        proMaterialWrapper.in(RadioProgramMaterial::getProgramId, idList);
        radioProgramMaterialService.remove(proMaterialWrapper);
        //删除节目
        boolean bool = this.removeByIds(idList);
        //删除节目对应的定时任务
        LambdaQueryWrapper<RadioPlay> radioPlayWrapper = new LambdaQueryWrapper<>();
        radioPlayWrapper.in(RadioPlay::getProgramId, idList);
        List<RadioPlay> playList = radioPlayService.list(radioPlayWrapper);
        if (playList != null && !playList.isEmpty()) {
            for (RadioPlay radioPlay : playList) {
                if (radioPlay.getId() == null) {
                    continue;
                }
                radioPlayService.deletePlay(radioPlay.getId());
            }
        }
        if (bool) {
            return result.success("批量删除成功");
        } else {
            return result.success("批量删除失败");
        }
    }

    @Override
    public Result getInfo(Integer id, HttpServletRequest request) {
        Result result = new Result();
        PbReqRadioProgramVO pbReqRadioProgramVO = new PbReqRadioProgramVO();
        //节目
        RadioProgram radioProgram = radioProgramDao.selectById(id);
        BeanUtils.copyProperties(radioProgram, pbReqRadioProgramVO);

        //节目素材关联
        LambdaQueryWrapper<RadioProgramMaterial> queryWrapperProgramMaterial = new LambdaQueryWrapper<RadioProgramMaterial>();
        queryWrapperProgramMaterial.eq(RadioProgramMaterial::getProgramId, id);
        List<RadioProgramMaterial> radioProgramMaterialList = radioProgramMaterialService.list(queryWrapperProgramMaterial);
        //节目素材
        List<PbReqRadioProgramMaterialVO> radioProgramMaterialList2 = new ArrayList<PbReqRadioProgramMaterialVO>();
        for (RadioProgramMaterial radioProgramMaterial : radioProgramMaterialList) {
            PbReqRadioProgramMaterialVO pbReqRadioProgramMaterialVO = new PbReqRadioProgramMaterialVO();
            RadioMaterial radioMaterial = radioMaterialService.getById(radioProgramMaterial.getMaterialId());
            if (radioMaterial == null) {
                continue;
            }
            BeanUtils.copyProperties(radioMaterial, pbReqRadioProgramMaterialVO);
            BeanUtils.copyProperties(radioProgramMaterial, pbReqRadioProgramMaterialVO);
            radioMaterial.setDurationStr(DateUtil.secondToTime(radioMaterial.getDuration()));
            pbReqRadioProgramMaterialVO.setRadioMaterial(radioMaterial);
            radioProgramMaterialList2.add(pbReqRadioProgramMaterialVO);
        }
        pbReqRadioProgramVO.setRadioProgramMaterialList(radioProgramMaterialList2);
        //获取创建人姓名
        String userName = radioMaterialService.getNameByUserId(radioProgram.getCreator());
        pbReqRadioProgramVO.setCreatorName(userName);
        pbReqRadioProgramVO.setDurationStr(DateUtil.secondToTime(pbReqRadioProgramVO.getDuration()));
        return result.success(pbReqRadioProgramVO);
    }

    @Override
    public Result getList(PbRadioProgramQueryObject qo, HttpServletRequest request) {
        logger.info("获取公共广播素材列表，接收参数：radioMaterial = {}", qo);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        List<Integer> managerIdList = userService.getManagerIdList(request);
        Page<RadioProgram> page = new Page<RadioProgram>(qo.getPageNum(), qo.getPageSize());
        IPage<RadioProgram> list = radioProgramDao.getPageList(page, qo);
        if (list != null && list.getRecords() != null && !list.getRecords().isEmpty()) {
            for (RadioProgram record : list.getRecords()) {
                record.setDurationStr(DateUtil.secondToTime(record.getDuration()));
                boolean isCanVerify = managerIdList != null && managerIdList.contains(userId) && !record.getCreator().equals(userId);
                record.setIsCanVerify(isCanVerify ? 1 : 0);
            }
        }
        return result.success(list);
    }

    @Override
    public PbRespRadioProgramVO getResp(Integer programId) {
        if (programId == null) {
            return null;
        }
        RadioProgram radioProgram = this.getById(programId);
        PbRespRadioProgramVO pbRespRadioProgramVO = new PbRespRadioProgramVO();
        BeanUtils.copyProperties(radioProgram, pbRespRadioProgramVO);
        LambdaQueryWrapper<RadioProgramMaterial> queryWrapperProgramMaterial = new LambdaQueryWrapper<RadioProgramMaterial>();
        queryWrapperProgramMaterial.eq(RadioProgramMaterial::getProgramId, radioProgram.getId());
        List<RadioProgramMaterial> programMaterialList = radioProgramMaterialService.list(queryWrapperProgramMaterial);
        //获取对应素材
        if (programMaterialList != null && !programMaterialList.isEmpty()) {
            List<Integer> materialIdList = programMaterialList.stream().map(RadioProgramMaterial::getMaterialId).collect(Collectors.toList());
            LambdaQueryWrapper<RadioMaterial> materialWrapper = new LambdaQueryWrapper<>();
            materialWrapper.in(RadioMaterial::getId, materialIdList);
            pbRespRadioProgramVO.setRadioMaterialList(radioMaterialService.list(materialWrapper));
        }
        return pbRespRadioProgramVO;
    }

    @Override
    public Result uniqueness(RadioProgram radioProgram, HttpServletRequest request) {
        logger.info("公共广播节目唯一性验证，接收参数：radioProgram = {}", radioProgram);
        Result result = new Result();
        Integer id = radioProgram.getId();
        String name = radioProgram.getName();
        //如果存在id，则为编辑，判断是否是编辑本身
        if (StringUtils.isNotBlank(name)) {
            LambdaQueryWrapper<RadioProgram> queryWrapperName = new LambdaQueryWrapper<RadioProgram>();
            queryWrapperName.eq(RadioProgram::getName, name);
            queryWrapperName.last("LIMIT 1");
            RadioProgram program = this.getOne(queryWrapperName);
            if ((id != null && program != null && !program.getId().equals(id)) || (id == null && program != null)) {
                logger.info("节目名称 {} 已存在,请重新输入", name);
                return result.success("节目名称已存在,请重新输入", 1);
            }
        }
        return result.success("唯一性校验通过", 0);
    }

    @Override
    public void refreshProgramDurationAndSize(Integer programId) {
        if (programId == null) {
            return;
        }
        //节目时长
        int duration = 0;
        //节目文件大小
        float size = 0;
        //素材列表
        List<PbRespMaterialSizeVO> materialSizeVos = radioProgramMaterialService.selectMaterialSizeByProgramId(programId);
        if (materialSizeVos == null || materialSizeVos.isEmpty()) {
            return;
        }
        duration = materialSizeVos.stream().filter(e -> e.getDuration() != null).mapToInt(PbRespMaterialSizeVO::getDuration).sum();
        size = materialSizeVos.stream().filter(e -> e.getSize() != null).map(PbRespMaterialSizeVO::getSize).reduce(Float::sum).get();
        //素材文件大小，保留两位小数
        BigDecimal bigDecimal = new BigDecimal(size);
        size = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        RadioProgram radioProgram = new RadioProgram();
        radioProgram.setId(programId);
        radioProgram.setDuration(duration);
        radioProgram.setCapacity(size);
        this.updateById(radioProgram);
    }

    @Override
    public Result<String> verifyProgram(PbReqVerifyProgramVO reqVO, HttpServletRequest request) {
        logger.info("公共广播节目审核，接收参数：PbReqVerifyProgramVO = {}", reqVO);
        Result<String> result = new Result<>();
        if (reqVO == null || reqVO.getId() == null || reqVO.getVerifyStatus() == null) {
            return result.error("审核失败,参数缺失");
        }
        if (!Arrays.asList(1, 2).contains(reqVO.getVerifyStatus())) {
            return result.error("审核失败,参数错误");
        }
        RadioProgram program = this.getById(reqVO.getId());
        if (program == null) {
            return result.error("审核失败,未找到节目信息");
        }
        if (!program.getVerifyStatus().equals(0)) {
            return result.error("审核失败,该节目不可审核");
        }
        program.setVerifyStatus(reqVO.getVerifyStatus());
        this.updateById(program);
        return result.success("审核成功", "");
    }

    /**
     * @return
     */
    public int uniqueness(Integer id, String name) {
        logger.info("公共广播设备唯一性验证，接收参数：id = {}, name = {}", id, name);
        if (StringUtils.isNotBlank(name)) {
            LambdaQueryWrapper<RadioProgram> queryWrapperName = new LambdaQueryWrapper<RadioProgram>();
            queryWrapperName.eq(RadioProgram::getName, name);
            queryWrapperName.last("LIMIT 1");
            RadioProgram program = this.getOne(queryWrapperName);
            if ((id != null && program != null && !program.getId().equals(id)) || (id == null && program != null)) {
                logger.info("节目名称 {} 已存在,请重新输入", name);
                return 1;
            }
        }
        return 0;
    }
}