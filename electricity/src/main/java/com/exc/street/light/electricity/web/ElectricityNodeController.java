package com.exc.street.light.electricity.web;

import com.exc.street.light.electricity.service.ElectricityNodeService;
import com.exc.street.light.electricity.util.FileUtil;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.electricity.LongitudeAndLatitude;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import com.exc.street.light.resource.qo.electricity.ElectricityNodeQueryObject;
import com.exc.street.light.resource.vo.electricity.ElectricityNodeImportVO;
import com.exc.street.light.resource.vo.req.electricity.ReqElectricityNodeUniquenessVO;
import com.exc.street.light.resource.vo.resp.ImportDeviceResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
 * 路灯网关控制器
 *
 * @author Linshiwen
 * @date 2018/5/21
 */
@Api(tags = "路灯网关控制器")
@RestController
@RequestMapping("/api/electricity/node")
public class ElectricityNodeController {
    private static final Logger logger = LoggerFactory.getLogger(ElectricityNodeController.class);
    @Autowired
    private ElectricityNodeService electricityNodeService;
    @Value("${upload.path}")
    public String uploadPath;

    /**
     * 新增网关
     *
     * @param electricityNode 网关信息
     * @return
     */
    @Deprecated
    @PostMapping
    @RequiresPermissions(value = "electricity:manage:node:add")
    public Result add(@RequestBody ElectricityNode electricityNode, HttpServletRequest request) {
        logger.info("新增对象" + electricityNode);
        return electricityNodeService.add(electricityNode, request);
    }

    /**
     * 新增路灯网关
     *
     * @param electricityNode 网关信息
     * @return
     */
    @PostMapping("/addControl")
    @ApiOperation(value = "新增路灯网关", notes = "新增路灯网关")
    //electricity:manage:node:add
    @RequiresPermissions(value = "dm:module:same:device:the:gateway:add")
    public Result addControl(@RequestBody ElectricityNode electricityNode, HttpServletRequest request) {
        logger.info("新增网关" + electricityNode);
        return electricityNodeService.addControl(electricityNode, request);
    }

    /**
     * 唯一性校验
     *
     * @param uniquenessVO
     * @param request
     * @return
     */
    @PostMapping("/uniqueness")
    @RequiresPermissions(value = "dm:module:same:device:the:gateway")
    public Result<Integer> uniqueness(@RequestBody ReqElectricityNodeUniquenessVO uniquenessVO, HttpServletRequest request) {
        return electricityNodeService.uniqueness(uniquenessVO, request);
    }


    /**
     * 删除网关
     *
     * @param id      网关id
     * @param request
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions(value = "dm:module:same:device:the:gateway:delete")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        return electricityNodeService.delete(id, request);
    }

    @ApiOperation(value = "批量删除路灯网关", notes = "根据路灯id集合批量删除路灯网关，多个用英文逗号分隔")
    @DeleteMapping("/batch")
    @RequiresPermissions(value = "dm:module:same:device:the:gateway:delete")
    public Result delete(String ids, HttpServletRequest request) {
        return electricityNodeService.batchDelete(ids, request);
    }

    @ApiOperation(value = "批量导入路灯网关", notes = "批量导入路灯网关")
    @PostMapping("/batchImport")
    @RequiresPermissions(value = "electricity:manage:node:add")
    public Result<ImportDeviceResultVO> batchImport(MultipartFile file, HttpServletRequest request) {
        return electricityNodeService.batchImport(file, request);
    }

    /**
     * 编辑路灯网关
     *
     * @param electricityNode 网关对象
     * @param request
     * @return 编辑结果
     */
    @PutMapping
    @ApiOperation(value = "编辑路灯网关", notes = "编辑路灯网关")
    //electricity:manage:node:update
    @RequiresPermissions(value = "dm:module:same:device:the:gateway:update")
    public Result update(@RequestBody ElectricityNode electricityNode, HttpServletRequest request) {
        return electricityNodeService.modify(electricityNode, request);
    }

    /**
     * 根据路灯网关id查询网关信息
     *
     * @param id 网关id
     * @return 查询结果
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "网关详情", notes = "根据路灯网关id查询网关信息")
    //electricity:manage:node:get
    @RequiresPermissions(value = "dm:module:same:device:the:gateway:detail")
    public Result detail(@PathVariable Integer id) {
        return new Result().success(electricityNodeService.getById(id));
    }

    /**
     * 根据路灯网关id搜索路灯网关信息并存储
     *
     * @param id
     * @return
     */
    @GetMapping("/serach/{id}")
    @RequiresPermissions(value = "electricity:manage:node:get")
    public Result search(@PathVariable Integer id, HttpServletRequest request) {
        Result result = electricityNodeService.searchNodeInfoById(id, request);
        return result;
    }

    /**
     * 网关列表查询
     *
     * @param request HttpServletRequest对象
     * @param qo      查询对象
     * @return 查询结果集
     */
    @GetMapping
    @ApiOperation(value = "网关列表查询", notes = "网关列表查询")
    @RequiresPermissions(value = "dm:module:same:device:the:gateway")
    public Result list(HttpServletRequest request, ElectricityNodeQueryObject qo) {
        logger.info("请求参数:ElectricityNodeQueryObject={}", qo);
        return electricityNodeService.query(request, qo);
    }

    @GetMapping("/pullDown/tree")
    @ApiOperation(value = "网关树状列表", notes = "网关树状列表")
    @RequiresPermissions(value = "dm:module:same:device:the:gateway")
    public Result tree(HttpServletRequest request) {
        return electricityNodeService.tree(request);
    }

    /**
     * 路灯网关下拉框列表
     *
     * @param request HttpServletRequest对象
     * @return 查询结果集
     */
    @GetMapping("/list")
    public Result findAll(HttpServletRequest request) {
        return electricityNodeService.listAll(request);
    }

    /**
     * 统计网关在线率
     *
     * @param request
     * @return
     */
    @GetMapping("/count")
    @RequiresPermissions(value = "electricity:common:home")
    public Result count(HttpServletRequest request) {
        return electricityNodeService.count(request);
    }

    /**
     * 路灯网关导入模板
     *
     * @param request
     * @param response
     */
    @GetMapping("/templet")
    public void templet(HttpServletRequest request, HttpServletResponse response) {
        InputStream in = null;
        OutputStream out = null;
        try {
            String fileName = URLEncoder.encode("路灯网关导入模板.xlsx", "UTF-8");
            response.reset();
            response.setContentType(request.getServletContext().getMimeType(fileName));
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            in = new FileInputStream(uploadPath + "路灯网关导入模板.xlsx");
            out = response.getOutputStream();
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = in.read(b)) != -1) {
                out.write(b, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * excel导入网关
     *
     * @param request
     * @return
     */
    @PostMapping("/excel/import")
    @RequiresPermissions(value = "electricity:manage:node:list")
    public Result batchImport(HttpServletRequest request) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        if (file.isEmpty()) {
            return new Result().error("文件为空");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.info("上传的后缀名为：" + suffixName);
        if (!"xls".equals(suffixName) && !"xlsx".equals(suffixName)) {
            return new Result().error("文件类型错误,不是Excel文件");
        }
        String realName = UUID.randomUUID() + "." + suffixName;
        String filePath = uploadPath + realName;
        File dest = new File(filePath);
        Result result;
        try {
            file.transferTo(dest);
            List<ElectricityNodeImportVO> nodes = FileUtil.importExcel(filePath, 0, 1, ElectricityNodeImportVO.class);
            result = electricityNodeService.importNode(nodes, request);
        } catch (IOException e) {
            return new Result().error("文件内容不符合模板");
        } finally {
            if (dest.exists()) {
                try {
                    dest.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;

    }

    /**
     * 根据路灯网关获取对应场景列表
     *
     * @param request HttpServletRequest对象
     * @param qo      查询对象
     * @return 查询结果集
     */
    @GetMapping("/scenelist")
    @ApiOperation(value = "根据路灯网关获取对应场景列表", notes = "根据路灯网关获取对应场景列表")
    @RequiresPermissions(value = "electricity:manage:node:list")
    public Result sceneList(HttpServletRequest request, ElectricityNodeQueryObject qo) {
        logger.info("根据路灯网关获取对应场景列表,请求参数:ElectricityNodeQueryObject={}", qo);
        return electricityNodeService.sceneList(request, qo);
    }

    /**
     * 网关经纬度修改
     */
    @PutMapping("/longitudeAndLatitude")
    @ApiOperation(value = "网关经纬度修改", notes = "网关经纬度修改")
    @RequiresPermissions(value = "electricity:manage:node:update")
    public Result putLongitudeAndLatitude(HttpServletRequest request, @RequestBody LongitudeAndLatitude longitudeAndLatitude) {
        logger.info("请求参数:" + longitudeAndLatitude);
        return electricityNodeService.updateLongitudeAndLatitude(request, longitudeAndLatitude);
    }

    /**
     * 网关经纬度获取
     */
    @GetMapping("/longitudeAndLatitude/{id}")
    @ApiOperation(value = "网关经纬度获取", notes = "网关经纬度获取")
    @RequiresPermissions(value = "electricity:manage:node:update")
    public Result getLongitudeAndLatitude(@PathVariable Integer id, HttpServletRequest request) {
        return electricityNodeService.getLongitudeAndLatitude(id, request);
    }
}
