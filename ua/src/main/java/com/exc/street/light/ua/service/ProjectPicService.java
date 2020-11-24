/**
 * @filename:ProjectPicService 2020-10-20
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ProjectPic;
import com.exc.street.light.resource.vo.resp.DlmRespProjectPicVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 项目图片(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 */
public interface ProjectPicService extends IService<ProjectPic> {

    /**
     * 上传图片
     *
     * @param file
     * @param httpServletRequest
     * @return
     */
    Result uploadPic(MultipartFile file, HttpServletRequest httpServletRequest);

    /**
     * 根据id删除图片
     *
     * @param picId
     * @param request
     * @return
     */
    Result deletePicById(Integer picId, HttpServletRequest request);

    /**
     * 关联图片与项目（区域）
     *
     * @param picId
     * @param userId
     */
    void associatePictureAndProject(Integer picId, Integer userId);

    /**
     * 获取用户详情图片信息
     *
     * @param userId
     * @return
     */
    DlmRespProjectPicVO getProjectPicRespVO(Integer userId);
}