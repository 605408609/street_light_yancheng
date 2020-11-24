package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.core.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ExcelCheckService {

    /**
     * 检查灯杆信息
     * @param realPath
     * @param fileName
     * @return
     */
    Result lampPostNameCheck(String fileName, String realPath);


    /**
     * 将表转成集合（含判断）
     * @param fileName
     * @param realPath
     * @return
     */
    Result changeExcelToList(String fileName, String realPath);

    /**
     * 将文件写入磁盘
     * @param multipartFile
     * @return
     */
    Result writeFile(MultipartFile multipartFile);

    /**
     * 将数据插入数据库
     * @param result
     * @return
     */
    Result saveList(Result result,HttpServletRequest request);

}
