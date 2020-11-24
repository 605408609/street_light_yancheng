/**
 * @filename:ScreenMaterialServiceImpl 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.ir.config.parameter.MaterialApi;
import com.exc.street.light.ir.config.parameter.PathApi;
import com.exc.street.light.ir.mapper.ScreenMaterialMapper;
import com.exc.street.light.ir.service.ScreenMaterialService;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenMaterial;
import com.exc.street.light.resource.entity.ir.ScreenProgram;
import com.exc.street.light.resource.entity.ir.ScreenProgramMaterial;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.IrMaterialQueryObject;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.IrMaterialVO;
import com.exc.street.light.resource.vo.resp.IrRespScreenMaterialVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class ScreenMaterialServiceImpl extends ServiceImpl<ScreenMaterialMapper, ScreenMaterial> implements ScreenMaterialService {
    private static final Logger logger = LoggerFactory.getLogger(ScreenMaterialServiceImpl.class);

    @Autowired
    private MaterialApi materialApi;
    @Autowired
    private PathApi pathApi;
    @Autowired
    private ScreenProgramServiceImpl screenProgramService;
    @Autowired
    private ScreenProgramMaterialServiceImpl screenProgramMaterialService;
    
    @Autowired
    private LogUserService userService;

    @Override
    public Result uploadText(ScreenMaterial material, HttpServletRequest httpServletRequest) {
        logger.info("上传文本素材，接收的素材数据{}", material);
        Result result = new Result();
        if (material.getName() == null || "".equals(material.getName().trim())) {
            logger.info("素材名称为空");
            return result.error("素材名称不能为空");
        }
        // 判断是否重名
        LambdaQueryWrapper<ScreenMaterial> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScreenMaterial::getName, material.getName());
        ScreenMaterial materialByName = this.getOne(wrapper);
        if (materialByName != null) {
            logger.info("素材名称已存在");
            return result.error("素材名称已存在");
        }
        //设置为文本格式
        material.setType("MultiLineText");
        //获取登录用户信息
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        //设置参数
        material.setCreator(userId);
        material.setCreateTime(new Date());
        material.setSpeed(materialApi.getSpeed());
        material.setCenter(material.getCenter());
        material.setBackgroundColor(materialApi.getBackgroundColor());
        if (material.getLineHeight() == null) {
            material.setLineHeight(materialApi.getLineHeight());
        }
        //保存至数据库
        logger.info("素材保存对象{}", material);
        this.save(material);
        return result.success("文本上传成功");
    }

    @Override
    public Result uploadVideoImage(MultipartFile file, HttpServletRequest httpServletRequest) {
        logger.info("上传文件或视频素材:{}",file);
        Result result = new Result();
        //验证资源路径是否存在
        File srcDirPath = new File(pathApi.getFile());
        if (!srcDirPath.exists()) {
            srcDirPath.mkdirs();
        }
        //验证传输文件是否已存在
        //文件名称
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || "".equals(originalFilename)) {
            return result.error("请选择文件");
        }
        //文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        //传入的文件类型  0是图片，1是视频
        int fileType = 0;
        String[] imageSuffixArr = materialApi.getImageSuffix().split(",");
        String[] videoSuffixArr = materialApi.getVideoSuffix().split(",");
        //判断文件是否是符合格式的文件
        if (Arrays.asList(imageSuffixArr).contains(suffix)) {
            fileType = 0;
        } else if (Arrays.asList(videoSuffixArr).contains(suffix)) {
            fileType = 1;
        } else {
            return result.error("上传文件类型错误");
        }
        // 校验文件名是否存在
        LambdaQueryWrapper<ScreenMaterial> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScreenMaterial::getName, originalFilename);
        ScreenMaterial materialByName = this.getOne(wrapper);
        if (materialByName != null) {
            return result.error("素材名称已存在");
        }
        //传入文件对象，文件保存到本地
        String fileName = UUID.randomUUID() + suffix;
        File srcFile = new File(pathApi.getFile() + fileName);
        try {
            file.transferTo(srcFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保存到本地的文件对象
        FileChannel fc = null;
        try {
            //构建持久化对象
            ScreenMaterial material = new ScreenMaterial();
            if (fileType == 0) {
                FileInputStream fis = new FileInputStream(srcFile);
                fc = fis.getChannel();
                //图片宽高
                BufferedImage bufferedImg = ImageIO.read(fis);
                int imgWidth = bufferedImg.getWidth();
                int imgHeight = bufferedImg.getHeight();
                material.setType("Image");
                if (".gif".equals(suffix)) {
                    material.setMime("image/gif");
                } else {
                    material.setMime("image/jpeg");
                }
                material.setWidth(imgWidth);
                material.setHeight(imgHeight);
            }
            if (fileType == 1) {
                MultimediaObject instance = new MultimediaObject(srcFile);
                MultimediaInfo m = instance.getInfo();
                long ls = m.getDuration();
                //视频帧宽高
                int videoWidth = m.getVideo().getSize().getHeight();
                int videoHeight = m.getVideo().getSize().getWidth();
                FileInputStream fis = new FileInputStream(srcFile);
                fc = fis.getChannel();
                material.setType("Video");
                //获取登录用户信息
                material.setMaxPlayTime((int) (ls / 1000));
                if (".mp4".equals(suffix)) {
                    material.setMime("video/mp4");
                } else {
                    material.setMime("video/quicktime");
                }
                material.setWidth(videoWidth);
                material.setHeight(videoHeight);
            }

            material.setName(originalFilename);
            //获取登录用户信息
            Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
            material.setCreator(userId);
            material.setCreateTime(new Date());
            Long size = fc.size();
            material.setSize(size.intValue());
            material.setFileName(fileName);
            material.setOldfilePath(pathApi.getFile());
            material.setFileExt(suffix);
            //保存至数据库
            logger.info("准备持久化的素材对象：{}", material);
            this.save(material);
        } catch (Exception e) {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            //如果文件存在则删除文件
            File file1 = new File(srcFile.getPath());
            file1.delete();
            e.printStackTrace();
            logger.error("报错："+e);
            return result.error("素材上传失败");
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.success("素材上传成功");
    }

    @Override
    public Result getQuery(IrMaterialQueryObject irMaterialQueryObject, HttpServletRequest httpServletRequest) {
        //令时间区间为空串是为空
        logger.info("素材列表条件查询,接收参数：{}", irMaterialQueryObject);
        if (StringUtils.isEmpty(irMaterialQueryObject.getStartTime())) {
            irMaterialQueryObject.setStartTime(null);
        }
        if (StringUtils.isEmpty(irMaterialQueryObject.getEndTime())) {
            irMaterialQueryObject.setEndTime(null);
        }
        
     // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
        	irMaterialQueryObject.setAreaId(user.getAreaId());
        }
        
        //条件查询素材列表
        IPage<IrRespScreenMaterialVO> page = new Page<IrRespScreenMaterialVO>(irMaterialQueryObject.getPageNum(), irMaterialQueryObject.getPageSize());
        IPage<IrRespScreenMaterialVO> dlmRespGroupVOList = this.baseMapper.query(page, irMaterialQueryObject);
        Result result = new Result();
        return result.success(dlmRespGroupVOList);
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        logger.info("准备删除素材id：{}", id);
        Result result = new Result();
        if (id == null) {
            return result.error("id不能为空");
        }
        // 根据id获取素材对象
        ScreenMaterial material = this.getById(id);
        logger.info("准备删除的素材对象{}", material);
        // 如果素材不等于空，并且是视频或者图片的时候，删除源文件
        if (material != null) {
            if ("Video".equals(material.getType()) || "Image".equals(material.getType())) {
                String path = material.getOldfilePath();
                File file = new File(path + material.getFileName());
                //如果文件存在则删除文件
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        // 删除本地数据库数据
        this.removeById(id);
        // 删除素材和节目关联关系
        LambdaQueryWrapper<ScreenProgramMaterial> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScreenProgramMaterial::getMaterialId, id);
        screenProgramMaterialService.remove(wrapper);
        return result.success("删除成功");
    }

    @Override
    public Result batchDelete(String ids, HttpServletRequest request) {
        logger.info("批量删除显示屏素材，接收参数：{}", ids);
        List<Integer> idListFromString = StringConversionUtil.getIdListFromString(ids);
        this.removeByIds(idListFromString);
        Result result = new Result();
        return result.success("批量删除成功");
    }

    @Override
    public Result programList(String ids, HttpServletRequest request) {
        logger.info("根据素材id获取关联节目列表，接收参数：{}", ids);
        List<Integer> idListFromString = StringConversionUtil.getIdListFromString(ids);
        // 获取素材对应的节目id集合
        LambdaQueryWrapper<ScreenProgramMaterial> programMaterialWrapper = new LambdaQueryWrapper<>();
        programMaterialWrapper.in(ScreenProgramMaterial::getMaterialId, idListFromString);
        List<ScreenProgramMaterial> list = screenProgramMaterialService.list(programMaterialWrapper);
        List<Integer> programList = list.stream().map(ScreenProgramMaterial::getProgramId).collect(Collectors.toList());
        List<Integer> programIdList = programList.stream().distinct().collect(Collectors.toList());
        // 根据节目id集合查询所有节目
        Collection<ScreenProgram> screenPrograms = null;
        if (programIdList != null && programIdList.size() > 0) {
            screenPrograms = screenProgramService.listByIds(programIdList);
        }
        Result result = new Result();
        return result.success(screenPrograms);
    }

    @Override
    public List<IrMaterialVO> getIrMaterialVO(List<Integer> materialIdList) {
        return this.baseMapper.getIrMaterialVO(materialIdList);
    }
}