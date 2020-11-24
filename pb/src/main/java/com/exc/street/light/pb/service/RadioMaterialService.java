/**
 * @filename:RadioMaterialService 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.pb.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.pb.RadioMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.qo.PbRadioMaterialQueryObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: LeiJing
 * 
 */
public interface RadioMaterialService extends IService<RadioMaterial> {
    /**
     * 新增公共广播素材
     *
     * @param file
     * @param request
     * @return
     */
    public Result add(MultipartFile file, HttpServletRequest request) throws IOException;
//    public Result add(File file, HttpServletRequest request);

//    /**
//     * 编辑公共广播素材
//     *
//     * @param radioMaterial
//     * @param request
//     * @return
//     */
//    public Result update(RadioMaterial radioMaterial, HttpServletRequest request);

    /**
     * 删除公共广播素材
     *
     * @param id
     * @param request
     * @return
     */
    public Result delete(Integer id, HttpServletRequest request);

    /**
     * 批量删除公共广播素材
     *
     * @param idList
     * @return
     */
    public Result batchDelete(List<Integer> idList);

    /**
     * 获取公共广播素材详情
     *
     * @param id
     * @param request
     * @return
     */
    public Result getInfo(Integer id, HttpServletRequest request);

    /**
     * 获取公共广播素材列表
     *
     * @param qo
     * @param request
     * @return
     */
    public Result getList(PbRadioMaterialQueryObject qo, HttpServletRequest request);

    /**
     * 根据用户id获取用户名称
     * @param userId
     * @return
     */
    public String getNameByUserId(Integer userId);

}