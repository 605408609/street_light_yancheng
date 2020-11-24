/**
 * @filename:LampDeviceController 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.web;


import java.io.File;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exc.street.light.dlm.service.ExcelCheckService;
import com.exc.street.light.dlm.service.impl.ExcelCheckServiceImpl;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;

import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-17
 *
 */
@Api(tags = "", value = "")
@RestController
@RequestMapping("/api/dlm/ec/device")
public class UploadExcelController {

    @Autowired
    private ExcelCheckService excelCheckService;

    /**
     * 上传表格
     * @param file
     * @return
     */
    @PostMapping("/uploadExcel")
    @SystemLog(logModul = "设备位置管理",logType = "上传",logDesc = "上传表格")
    @RequiresPermissions(value = "view:data")
    Result uploadExcel(@RequestBody MultipartFile file,HttpServletRequest request) {

        Result realResult = new Result().error("上传错误，请稍后再试");
        int code = realResult.getCode();
        Result paramResult = excelCheckService.writeFile(file);
        if(paramResult != null){
            code = paramResult.getCode();
        }

        if(code==200){
            Map<String,String> paramMap = (Map<String, String>)paramResult.getData();
            String fileName = paramMap.get("fileName");
            String realPath = paramMap.get("realPath");
            try {
                Result result = excelCheckService.lampPostNameCheck(fileName, realPath);
                int lampPostCode = result.getCode();
                if(lampPostCode == 200){
                    realResult = excelCheckService.changeExcelToList(fileName, realPath);
                }else {
                    realResult = result;
                }
            }catch (Exception e){
                return new Result().error("表格上传失败，请稍后再试");
            }finally {
                if(!realPath.isEmpty()){
                    // 删除源文件
                    File file0 = new File(realPath);
                    if (file0.exists()) {
                        file0.delete();
                    }
                }
            }
        }else{
            realResult = paramResult;
        }

        if(realResult.getCode() == 200){
            realResult = excelCheckService.saveList(realResult,request);
        }


        return realResult;
    }

}