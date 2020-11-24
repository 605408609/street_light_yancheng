/**
 * @filename:ProjectPicServiceImpl 2020-10-20
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ProjectPic;
import com.exc.street.light.resource.vo.resp.DlmRespProjectPicVO;
import com.exc.street.light.ua.mapper.ProjectPicMapper;
import com.exc.street.light.ua.service.ProjectPicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * @Description: 项目图片(服务实现)
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Service
public class ProjectPicServiceImpl extends ServiceImpl<ProjectPicMapper, ProjectPic> implements ProjectPicService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectPicServiceImpl.class);

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public Result uploadPic(MultipartFile file, HttpServletRequest httpServletRequest) {
        Result<Object> result = new Result<>();
        // 验证资源路径是否存在
        File srcDirPath = new File(uploadPath);
        if (!srcDirPath.exists()) {
            srcDirPath.mkdirs();
        }
        // 验证传输文件是否已存在
        // 文件名称
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || "".equals(originalFilename)) {
            return result.error("请选择文件");
        }
        // 文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        // 传入文件对象，文件保存到本地
        String fileName = UUID.randomUUID() + suffix;
        logger.info("上传图片保存地址：{}", uploadPath + fileName);
        File srcFile = new File(uploadPath + fileName);
        try {
            file.transferTo(srcFile);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        // 保存到本地的文件对象
        ProjectPic projectPic = new ProjectPic();
        try {
            // 构建持久化对象
            projectPic.setName(fileName);
            projectPic.setCreateTime(new Date());
            logger.info("上传图片持久化保存对象：{}", projectPic);
            baseMapper.insert(projectPic);
        } catch (Exception e) {
            // 如果保存失败则删除文件
            File f = new File(srcFile.getPath());
            f.delete();
            logger.error(e.getMessage(), e);
            return result.error("图片上传失败");
        }
        return result.success(projectPic.getId());
    }

    @Override
    public Result deletePicById(Integer picId, HttpServletRequest request) {
        logger.info("根据图片id删除图片 - 接收参数：{}", picId);
        Result result = new Result();
        if (picId == null) {
            return result.error("picId不能为空");
        }
        // 根据id获取项目图片对象
        ProjectPic projectPic = baseMapper.selectById(picId);
        //如果项目图片不等于空，删除源文件
        if (projectPic != null) {
            File file = new File(uploadPath + projectPic.getName());
            //如果文件存在则删除文件
            if (file.exists()) {
                file.delete();
            }
        }
        //删除本地数据库数据
        baseMapper.deleteById(picId);
        return result.success("删除成功");
    }

    @Override
    public void associatePictureAndProject(Integer picId, Integer userId) {
        logger.info("关联图片与项目（区域）,接收参数：picId={}，areaId={}", picId, userId);
        LambdaUpdateWrapper<ProjectPic> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ProjectPic::getUserId, userId)
                .eq(ProjectPic::getId, picId);
        baseMapper.update(null, updateWrapper);
    }

    @Override
    public DlmRespProjectPicVO getProjectPicRespVO(Integer userId) {
        logger.info("获取区域详情图片信息,接收参数：userId={}", userId);
        DlmRespProjectPicVO projectPicVO = new DlmRespProjectPicVO();
        LambdaQueryWrapper<ProjectPic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectPic::getUserId, userId);
        ProjectPic projectPic = baseMapper.selectOne(queryWrapper);
        if (projectPic != null) {
            BeanUtils.copyProperties(projectPic, projectPicVO);
        }
        return projectPicVO;
    }
}