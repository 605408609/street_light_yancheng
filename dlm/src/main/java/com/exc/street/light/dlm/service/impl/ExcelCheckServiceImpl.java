package com.exc.street.light.dlm.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.exc.street.light.dlm.config.parameter.LoraApi;
import com.exc.street.light.dlm.config.parameter.PathApi;
import com.exc.street.light.dlm.service.*;
import com.exc.street.light.dlm.utils.CTWingApi;
import com.exc.street.light.dlm.utils.MessageOperationUtil;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.entity.sl.*;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.entity.wifi.WifiAcDevice;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.ec.TableStructure;
import com.exc.street.light.resource.vo.resp.DlmBatchCreateDeviceRespVO;
import com.exc.street.light.resource.vo.resp.ImportDeviceFailVO;
import com.exc.street.light.resource.vo.resp.ImportDeviceResultVO;
import com.exc.street.light.resource.vo.sl.LoopParamVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamReqVO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelCheckServiceImpl implements ExcelCheckService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelCheckServiceImpl.class);

    @Autowired
    TableStructureService tableStructureService;

    @Autowired
    SlLampPostService slLampPostService;

    @Autowired
    AhDeviceService ahDeviceService;

    /*@Autowired
    LampDeviceService lampDeviceService;*/

    @Autowired
    MeteorologicalDeviceService meteorologicalDeviceService;

    @Autowired
    RadioDeviceService radioDeviceService;

    @Autowired
    ScreenDeviceService screenDeviceService;

    @Autowired
    SsDeviceService ssDeviceService;

    @Autowired
    WifiApDeviceService wifiApDeviceService;

    @Autowired
    WifiAcDeviceService wifiAcDeviceService;

    @Autowired
    LampLoopTypeService lampLoopTypeService;

    @Autowired
    LampPositionService lampPositionService;

    /*@Autowired
    SingleLampParamService singleLampParamService;*/

    @Autowired
    private SystemDeviceService systemDeviceService;

    @Autowired
    LoraApi loraApi;

    @Autowired
    PathApi pathApi;

    @Autowired
    private SystemDeviceTypeService systemDeviceTypeService;

    @Autowired
    private SystemDeviceParameterService systemDeviceParameterService;

    @Autowired
    private LampDeviceParameterService lampDeviceParameterService;

    @Autowired
    private CTWingApi ctWingApi;

    /**
     * 检查灯杆信息
     *
     * @param realPath
     * @param fileName
     * @return
     */
    @Override
    public Result lampPostNameCheck(String fileName, String realPath) {

        ImportDeviceResultVO importDeviceResultVO = new ImportDeviceResultVO();
        Integer failNum = 0;
        logger.info("检查灯杆信息，fileName：" + fileName + "，realPath：" + realPath);
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);

        FileInputStream file = null;
        //查询数据库中灯杆信息
        List<String> slLampPostName = slLampPostService.getSlLampPostName();
        if (!slLampPostName.isEmpty()) {

            try {
                // 读取excel表格信息
                file = new FileInputStream(realPath);
                // 根据指定的文件输入流导入Excel从而产生Workbook对象
                @SuppressWarnings("resource")
                Workbook workbook = null;
                if ("xls".equals(suffixName)) {
                    workbook = new HSSFWorkbook(file);
                } else {
                    workbook = new XSSFWorkbook(file);
                }

                //获取sheet对象
                Sheet sheet = workbook.getSheetAt(0);
                if (sheet == null) {
                    logger.info("提交了空表，文件名：" + fileName);
                    return new Result().error("请勿提交空表");
                }

                Row rowTitle = sheet.getRow(1);
                if (rowTitle == null) {
                    logger.info("上传表格中标题行为空，文件名：" + fileName);
                    return new Result().error("标题行不能为空");
                }
                //获取标题列的信息
                Cell titleCell = rowTitle.getCell(0);
                String stringCellValue = "";
                if (titleCell != null) {
                    stringCellValue = titleCell.getStringCellValue();
                }
                //判断当前列是否为lampPostName列数据
                if ("lampPostName".equals(stringCellValue)) {

                    //遍历row（从第四行开始）
                    int rowCount = sheet.getPhysicalNumberOfRows();
                    for (int rowNum = 3; rowNum < rowCount; rowNum++) {
                        //获取行row对象
                        Row rowData = sheet.getRow(rowNum);

                        if (rowData != null) {// 行不为空
                            failNum++;
                            //获取列cell对象
                            Cell cell = rowData.getCell(0);
                            if (cell != null) {

                                String key = new String("【" + (rowNum + 1) + "-" + "A】");
                                int cellType = cell.getCellType();
                                //判断单元格数据类型
                                switch (cellType) {
                                    case HSSFCell.CELL_TYPE_STRING://字符串
                                        String cellValue = cell.getStringCellValue();
                                        if (!slLampPostName.contains(cellValue)) {
                                            ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                            importDeviceFailVO.setRows(key);
                                            importDeviceFailVO.getFailMsgList().add("不含该类灯杆");
                                            importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                        }
                                        break;

                                    case HSSFCell.CELL_TYPE_BLANK://空
                                        //判断当前字段是否可以为空
                                        ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                        importDeviceFailVO.setRows(key);
                                        importDeviceFailVO.getFailMsgList().add("灯杆信息为空");
                                        importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                        break;

                                    default:
                                        ImportDeviceFailVO importDeviceFailDefaultVO = new ImportDeviceFailVO();
                                        importDeviceFailDefaultVO.setRows(key);
                                        importDeviceFailDefaultVO.getFailMsgList().add("数据类型错误");
                                        importDeviceResultVO.getFailInfoList().add(importDeviceFailDefaultVO);
                                        break;
                                }

                            } else {

                            }

                        }
                    }
                }
            } catch (Exception e) {
                logger.info("未按表格模板填写并提交数据：(lampPostNameCheck)" + e);
                e.printStackTrace();
                return new Result().error("请按表格模板填写并提交数据");
            } finally {
                if (file != null) {
                    try {
                        //关流
                        file.close();
                    } catch (IOException e) {
                        logger.info("关流失败：(lampPostNameCheck)" + e);
                    }

                }
            }

        } else {
            logger.info("sl_lamp_post数据库表查询失败");
            return new Result().error("表格上传失败，请稍后再试");
        }

        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
            return new Result().success("灯杆信息无误");
        }
        logger.info("灯杆信息有误，文件名：" + fileName);
        importDeviceResultVO.setFailNum(failNum);
        return new Result().error("灯杆信息有误，如果单元格未填写数据，请检查是否删除多余框线或重新下载模板填写", importDeviceResultVO);
    }

    /**
     * 将表转成集合（含判断）
     *
     * @param fileName
     * @param realPath
     * @return
     */
    @Override
    public Result changeExcelToList(String fileName, String realPath) {

        logger.info("将表转成集合（含判断），fileName：" + fileName + "，realPath：" + realPath);
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);

        //创建各集合List
        List<AhDevice> ahDeviceList = new ArrayList<>();
        List<SingleLampParamReqVO> lampDeviceList = new ArrayList<>();
        List<ScreenDevice> screenDeviceList = new ArrayList<>();
        List<SsDevice> ssDeviceList = new ArrayList<>();
        List<WifiApDevice> wifiApDeviceList = new ArrayList<>();
        List<MeteorologicalDevice> meteorologicalDeviceList = new ArrayList<>();
        List<RadioDevice> radioDeviceList = new ArrayList<>();
        List<WifiAcDevice> wifiAcDeviceList = new ArrayList<>();
        //创建错误信息结果集
        ImportDeviceResultVO importDeviceResultVO = new ImportDeviceResultVO();
        Integer failNum = 0;
        //创建结果信息的集合Map
        Map<String, List> resultMap = new HashMap<>();
        resultMap.put("ahDeviceList", ahDeviceList);
        resultMap.put("lampDeviceList", lampDeviceList);
        resultMap.put("screenDeviceList", screenDeviceList);
        resultMap.put("ssDeviceList", ssDeviceList);
        resultMap.put("wifiApDeviceList", wifiApDeviceList);
        resultMap.put("meteorologicalDeviceList", meteorologicalDeviceList);
        resultMap.put("radioDeviceList", radioDeviceList);
        resultMap.put("wifiAcDeviceList", wifiAcDeviceList);

        FileInputStream file = null;
        try {
            // 读取excel表格信息
            file = new FileInputStream(realPath);
            // 根据指定的文件输入流导入Excel从而产生Workbook对象
            @SuppressWarnings("resource")
            Workbook workbook = null;
            if ("xls".equals(suffixName)) {
                workbook = new HSSFWorkbook(file);
            } else {
                workbook = new XSSFWorkbook(file);
            }
            //获取sheet对象
            Sheet sheet = workbook.getSheetAt(0);

            if (sheet == null) {
                logger.info("提交了空表，文件名：" + fileName);
                return new Result().error("请勿提交空表");
            }
            Map<String, Integer> informationMap = new HashMap<>();
            //获取标题行
            Row rowTitle = sheet.getRow(1);
            //获取设备类型行
            Row rowDeviceType = sheet.getRow(3);
            if (rowTitle == null || rowDeviceType == null) {
                logger.info("第二行或设备类型为空，文件名：" + fileName);
                return new Result().error("第二行和设备类型不能为空");
            }
            //遍历标题行
            int cellCount = rowTitle.getPhysicalNumberOfCells();
            for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                //获取标题列的信息
                Cell titleCell = rowTitle.getCell(cellNum);
                if (titleCell != null) {
                    String titleCellValue = titleCell.getStringCellValue();
                    //判断是否为DeviceType列
                    if ("DeviceType".equals(titleCellValue)) {
                        Cell deviceTypeCell = rowDeviceType.getCell(cellNum);
                        if (rowDeviceType == null) {
                            logger.info("DeviceType数据为空，文件名：" + fileName);
                            return new Result().error("DeviceType数据不能为空");
                        }
                        String deviceTypeCellValue = deviceTypeCell.getStringCellValue();
                        switch (deviceTypeCellValue) {
                            case "一键求助":
                                informationMap.put("ah_device", cellNum);
                                break;
                            case "灯控":
                                informationMap.put("lamp_device", cellNum);
                                break;
                            case "传感器":
                                informationMap.put("meteorological_device", cellNum);
                                break;
                            case "广播":
                                informationMap.put("radio_device", cellNum);
                                break;
                            case "信息屏":
                                informationMap.put("screen_device", cellNum);
                                break;
                            case "摄像头":
                                informationMap.put("ss_device", cellNum);
                                break;
                            case "AP":
                                informationMap.put("wifi_ap_device", cellNum);
                                break;
                            case "AC":
                                informationMap.put("wifi_ac_device", cellNum);
                                break;
                            default:
                                logger.info("DeviceType数据未匹配，文件名：" + fileName);
                                return new Result().error("DeviceType数据未匹配，请按模板填写并提交数据");
                        }
                    }
                }

            }

            //创建数据库的表结构集合
            Map<String, List<TableStructure>> ahDeviceStructureMap = new HashMap<>();
            Map<String, List<TableStructure>> systemDeviceStructureMap = new HashMap<>();
            Map<String, List<TableStructure>> meteorologicalDeviceStructureMap = new HashMap<>();
            Map<String, List<TableStructure>> radioDeviceStructureMap = new HashMap<>();
            Map<String, List<TableStructure>> screenDeviceStructureMap = new HashMap<>();
            Map<String, List<TableStructure>> ssDeviceStructureMap = new HashMap<>();
            Map<String, List<TableStructure>> wifiApDeviceStructureMap = new HashMap<>();
            Map<String, List<TableStructure>> wifiAcDeviceStructureMap = new HashMap<>();

            Integer ahDeviceNum = informationMap.get("ah_device");
            Integer lampDeviceNum = informationMap.get("lamp_device");
            Integer meteorologicalDeviceNum = informationMap.get("meteorological_device");
            Integer radioDeviceNum = informationMap.get("radio_device");
            Integer screenDeviceNum = informationMap.get("screen_device");
            Integer ssDeviceNum = informationMap.get("ss_device");
            Integer wifiApDeviceNum = informationMap.get("wifi_ap_device");
            Integer wifiAcDeviceNum = informationMap.get("wifi_ac_device");

            //获取所有灯杆集合
            Map<String, Integer> slLampPostNameMap = new HashMap<>();
            List<SlLampPost> slLampPostList = slLampPostService.getList();
            if (slLampPostList != null) {
                for (SlLampPost slLampPost : slLampPostList) {
                    String name = slLampPost.getName();
                    Integer id = slLampPost.getId();
                    slLampPostNameMap.put(name, id);
                }
            } else {
                logger.info("sl_lamp_post数据库表查询失败");
                return new Result().error("表格上传失败，请稍后再试");
            }


            List<String> ahdNameList = new ArrayList<>();
            List<String> ahdNumList = new ArrayList<>();
            if (ahDeviceNum != null) {
                //获取ah_device表中的name和num字段信息
                Result ahdResult = ahDeviceService.selectAll();
                List<AhDevice> ahDeviceListNow = (List<AhDevice>) ahdResult.getData();
                ahdNameList = ahDeviceListNow.stream().map(AhDevice::getName).collect(Collectors.toList());
                ahdNumList = ahDeviceListNow.stream().map(AhDevice::getNum).collect(Collectors.toList());
                //获取数据库表结构信息
                List<TableStructure> ahDeviceStructureList = tableStructureService.getTableStructure("ah_device");
                ahDeviceStructureMap = ahDeviceStructureList.stream()
                        .collect(Collectors.groupingBy(TableStructure::getColumnName));
            }
            List<String> sdNameList = new ArrayList<>();
            List<SystemDevice> sdObjectList = new ArrayList<>();
            if (lampDeviceNum != null) {
                List<SystemDevice> systemDeviceList = systemDeviceService.list();
                //List<SingleLampParam> singleLampParamList = singleLampParamService.list();
                sdNameList = systemDeviceList.stream().map(SystemDevice::getName).collect(Collectors.toList());
                //ladNameList = singleLampParamList.stream().map(SingleLampParam::getName).collect(Collectors.toList());
                //获取lamp_device表中的name和num字段信息
                /*Result ladResult = lampDeviceService.selectAll();
                List<LampDevice> lampDeviceListNow = (List<LampDevice>) ladResult.getData();
                ladObjectList.addAll(lampDeviceListNow);*/
                sdObjectList.addAll(systemDeviceList);
                List<TableStructure> systemDeviceStructureList = tableStructureService.getTableStructure("system_device");
                systemDeviceStructureMap = systemDeviceStructureList.stream()
                        .collect(Collectors.groupingBy(TableStructure::getColumnName));
            }
            List<String> medNameList = new ArrayList<>();
            List<String> medNumList = new ArrayList<>();
            if (meteorologicalDeviceNum != null) {
                //获取meteorological_device表中的name和num字段信息
                Result medResult = meteorologicalDeviceService.selectAll();
                List<MeteorologicalDevice> meteorologicalDeviceListNow = (List<MeteorologicalDevice>) medResult.getData();
                medNameList = meteorologicalDeviceListNow.stream().map(MeteorologicalDevice::getName).collect(Collectors.toList());
                medNumList = meteorologicalDeviceListNow.stream().map(MeteorologicalDevice::getNum).collect(Collectors.toList());
                List<TableStructure> meteorologicalDeviceStructureList = tableStructureService.getTableStructure("meteorological_device");
                meteorologicalDeviceStructureMap = meteorologicalDeviceStructureList.stream()
                        .collect(Collectors.groupingBy(TableStructure::getColumnName));
            }
            List<String> radNameList = new ArrayList<>();
            List<String> radNumList = new ArrayList<>();
            List<Integer> radLtNumList = new ArrayList<>();
            if (radioDeviceNum != null) {
                //获取radio_device表中的name和num字段信息
                Result radResult = radioDeviceService.selectAll();
                List<RadioDevice> radioDeviceListNow = (List<RadioDevice>) radResult.getData();
                radNameList = radioDeviceListNow.stream().map(RadioDevice::getName).collect(Collectors.toList());
                radNumList = radioDeviceListNow.stream().map(RadioDevice::getNum).collect(Collectors.toList());
                radLtNumList = radioDeviceListNow.stream().map(RadioDevice::getTermId).collect(Collectors.toList());
                List<TableStructure> radioDeviceStructureList = tableStructureService.getTableStructure("radio_device");
                radioDeviceStructureMap = radioDeviceStructureList.stream()
                        .collect(Collectors.groupingBy(TableStructure::getColumnName));
            }
            List<String> scdNameList = new ArrayList<>();
            List<String> scdNumList = new ArrayList<>();
            if (screenDeviceNum != null) {
                //获取screen_device表中的name和num字段信息
                Result scdResult = screenDeviceService.selectAll();
                List<ScreenDevice> screenDeviceListNow = (List<ScreenDevice>) scdResult.getData();
                scdNameList = screenDeviceListNow.stream().map(ScreenDevice::getName).collect(Collectors.toList());
                scdNumList = screenDeviceListNow.stream().map(ScreenDevice::getNum).collect(Collectors.toList());
                List<TableStructure> screenDeviceStructureList = tableStructureService.getTableStructure("screen_device");
                screenDeviceStructureMap = screenDeviceStructureList.stream()
                        .collect(Collectors.groupingBy(TableStructure::getColumnName));
            }
            List<String> ssdNameList = new ArrayList<>();
            List<String> ssdNumList = new ArrayList<>();
            if (ssDeviceNum != null) {
                //获取ss_device表中的name和num字段信息
                Result ssdResult = ssDeviceService.selectAll();
                List<SsDevice> ssDeviceListNow = (List<SsDevice>) ssdResult.getData();
                ssdNameList = ssDeviceListNow.stream().map(SsDevice::getName).collect(Collectors.toList());
                ssdNumList = ssDeviceListNow.stream().map(SsDevice::getNum).collect(Collectors.toList());
                List<TableStructure> ssDeviceStructureList = tableStructureService.getTableStructure("ss_device");
                ssDeviceStructureMap = ssDeviceStructureList.stream()
                        .collect(Collectors.groupingBy(TableStructure::getColumnName));
            }
            List<String> widNameList = new ArrayList<>();
            List<String> widNumList = new ArrayList<>();
            if (wifiApDeviceNum != null) {
                //获取wifi_ap_device表中的name和num字段信息
                Result widResult = wifiApDeviceService.selectAll();
                List<WifiApDevice> wifiApDeviceListNow = (List<WifiApDevice>) widResult.getData();
                widNameList = wifiApDeviceListNow.stream().map(WifiApDevice::getName).collect(Collectors.toList());
                widNumList = wifiApDeviceListNow.stream().map(WifiApDevice::getNum).collect(Collectors.toList());
                List<TableStructure> wifiApDeviceStructureList = tableStructureService.getTableStructure("wifi_ap_device");
                wifiApDeviceStructureMap = wifiApDeviceStructureList.stream()
                        .collect(Collectors.groupingBy(TableStructure::getColumnName));
            }
            List<String> wicdNameList = new ArrayList<>();
            List<String> wicdNumList = new ArrayList<>();
            if (wifiAcDeviceNum != null) {
                //获取wifi_ac_device表中的name和num字段信息
                Result wicdResult = wifiAcDeviceService.selectAll();
                List<WifiAcDevice> wifiAcDeviceListNow = (List<WifiAcDevice>) wicdResult.getData();
                wicdNameList = wifiAcDeviceListNow.stream().map(WifiAcDevice::getName).collect(Collectors.toList());
                wicdNumList = wifiAcDeviceListNow.stream().map(WifiAcDevice::getNum).collect(Collectors.toList());
                List<TableStructure> wifiAcDeviceStructureList = tableStructureService.getTableStructure("wifi_ac_device");
                wifiAcDeviceStructureMap = wifiAcDeviceStructureList.stream()
                        .collect(Collectors.groupingBy(TableStructure::getColumnName));
            }

            //遍历row（从第4行开始）
            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int rowNum = 3; rowNum < rowCount; rowNum++) {
                //获取行row对象
                Row rowData = sheet.getRow(rowNum);

                if (rowData != null) {// 行不为空
                    failNum++;
                    String firstCell = rowTitle.getCell(0).getStringCellValue();
                    Integer lampPostId = null;
                    if ("lampPostName".equals(firstCell)) {
                        //获取lampPostName列
                        Cell lampPostNameCell = rowData.getCell(0);

                        if (lampPostNameCell != null) {
                            String lampPostName = lampPostNameCell.getStringCellValue();
                            lampPostId = slLampPostNameMap.get(lampPostName);
                        } else {
                            logger.info("lampPostName列为空");
                            return new Result().error("lampPostName列不能为空");
                        }
                    }

                    //AhDevice
                    if (ahDeviceNum != null) {
                        //设置灯杆类型
                        AhDevice ahDevice = new AhDevice();
                        ahDevice.setLampPostId(lampPostId);
                        ahDevice.setCreateTime(new Date());
                        for (int cellNum = ahDeviceNum + 1; cellNum < ahDeviceNum + 7; cellNum++) {


                            //获取列cell对象
                            Cell cell = rowData.getCell(cellNum);
                            if (cell != null) {

                                String key = getKey(rowNum, cellNum);
                                Cell titleCell = rowTitle.getCell(cellNum);
                                String titleCellValue = titleCell.getStringCellValue();
                                String titleKey = key.substring(key.indexOf("-"), key.indexOf("】") + 1);

                                //获取数据库表结构信息
                                TableStructure tableStructure = ahDeviceStructureMap.get(titleCellValue).get(0);
                                //判断数据是否符合要求填写
                                List<String> resultList = excelCheckUtil(tableStructure, cell);

                                if ("".equals(resultList.get(0))) {
                                    switch (titleCellValue) {
                                        case "model":
                                            String model = resultList.get(1);
                                            ahDevice.setModel(model);
                                            break;
                                        case "ip":
                                            String ip = resultList.get(1);
                                            ahDevice.setIp(ip);
                                            break;
                                        case "port":
                                            Integer port = new Double(resultList.get(1)).intValue();
                                            ahDevice.setPort(port);
                                            break;
                                        case "factory":
                                            String factory = resultList.get(1);
                                            ahDevice.setFactory(factory);
                                            break;
                                        case "name":
                                            String name = resultList.get(1);
                                            if (ahdNameList.contains(name)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备名称重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                ahdNameList.add(name);
                                            }
                                            ahDevice.setName(name);
                                            break;
                                        case "num":
                                            String num = resultList.get(1);
                                            if (ahdNumList.contains(num)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备编号重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                ahdNumList.add(num);
                                            }

                                            ahDevice.setNum(num);
                                            break;
                                        case "":
                                            return new Result().error("【2" + titleKey + "为空值，请按模板填写提交");
                                        default:
                                            return new Result().error("【2" + titleKey + "为无法匹配的值，请按模板填写提交");
                                    }

                                } else {
                                    //将错误信息添加到集合中
                                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                    importDeviceFailVO.setRows(key);
                                    importDeviceFailVO.getFailMsgList().add(resultList.get(0));
                                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                }
                            }
                        }
                        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
                            ahDeviceList.add(ahDevice);
                        }
                    }

                    //LampDevice
                    if (lampDeviceNum != null) {
                        //设置灯控器基本信息
                        SingleLampParamReqVO singleLampParamReqVO = new SingleLampParamReqVO();
                        singleLampParamReqVO.setLampPostId(lampPostId);
                        //获取回路类型信息
                        Cell deviceTypeCell = rowData.getCell(lampDeviceNum + 1);
                        String deviceTypeKey = getKey(rowNum, lampDeviceNum + 1);

                        List<SystemDeviceType> systemDeviceTypeList = systemDeviceTypeService.list();
                        Map<String, SystemDeviceType> systemDeviceTypeMap = systemDeviceTypeList.stream().collect(Collectors.toMap(SystemDeviceType::getName, obj -> obj));

                        /*List<LampLoopType> lampLoopTypeList = lampLoopTypeService.list();
                        Map<String, LampLoopType> loopTotalMap = lampLoopTypeList.stream().collect(Collectors.toMap(LampLoopType::getType, obj -> obj));*/


                        Integer loopTotal = 0;
                        Integer deviceTypeId = 0;
                        if (deviceTypeCell != null) {
                            try {
                                String deviceType = deviceTypeCell.getStringCellValue();
                                SystemDeviceType systemDeviceType = systemDeviceTypeMap.get(deviceType);
                                if (loopTotal != null) {
                                    String loopType = systemDeviceType.getLoopType();
                                    if("单回路".equals(loopType)){
                                        loopTotal = 1;
                                    }else {
                                        loopTotal = 2;
                                    }
                                    deviceTypeId = systemDeviceType.getId();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                return new Result().error(deviceTypeKey + "请正确填写设备类型");
                            }
                        } else {
                            logger.info("设备类型为空：" + fileName);
                            return new Result().error(deviceTypeKey + "设备类型不能为空");
                        }
                        if(loopTotal==0||deviceTypeId==0){
                            return new Result().error(deviceTypeKey + "请正确填写设备类型");
                        }
                        singleLampParamReqVO.setDeviceTypeId(deviceTypeId);

                        //获取灯具位置信息
                        List<LampPosition> lampPositionList = lampPositionService.list();
                        Map<String, Integer> lampPositionMap = lampPositionList.stream().collect(Collectors.toMap(LampPosition::getPosition,LampPosition::getId));

                        //根据回路类型信息创建回路参数对象
                        List<LoopParamVO> loopParamVOList = new ArrayList<>();
                        singleLampParamReqVO.setLoopParamVOList(loopParamVOList);
                        for (int i = 0; i < loopTotal; i++){
                            LoopParamVO loopParamVO = new LoopParamVO();
                            loopParamVO.setLoopNum(i+1);
                            loopParamVOList.add(loopParamVO);
                        }
                        for (int cellNum = lampDeviceNum + 2; cellNum < lampDeviceNum + 3 + 2 * loopTotal; cellNum++) {

                            //获取列cell对象
                            Cell cell = rowData.getCell(cellNum);
                            if (cell != null) {
                                String key = getKey(rowNum, cellNum);
                                Cell titleCell = rowTitle.getCell(cellNum);
                                String titleCellValue = titleCell.getStringCellValue();
                                String titleKey = key.substring(key.indexOf("-"), key.indexOf("】") + 1);
                                List<String> resultList = new ArrayList<>();
                                //获取数据库表结构信息
                                List<TableStructure> tableStructures = systemDeviceStructureMap.get(titleCellValue);

                                if(tableStructures!=null&&tableStructures.size()>0){
                                    TableStructure tableStructure = tableStructures.get(0);
                                    //判断数据是否符合要求填写
                                    resultList = excelCheckUtil(tableStructure, cell);
                                }else {
                                    resultList.add("特殊字段");
                                }
                                if ("".equals(resultList.get(0))||"特殊字段".equals(resultList.get(0))) {
                                    switch (titleCellValue) {
                                        /*case "model":
                                            String model = resultList.get(1);
                                            if("nb".equals(model)||"cat1".equals(model)||"lora_old".equals(model)){
                                                singleLampParamReqVO.setModel(model);
                                            }else {
                                                errorMap.put(key, "请按规定填写接入协议");
                                            }
                                            break;
                                        case "factory":
                                            String factory = resultList.get(1);
                                            if("EXC1".equals(factory)||"EXC2".equals(factory)){
                                                singleLampParamReqVO.setFactory(factory);
                                            }else {
                                                errorMap.put(key, "请按规定填写设备厂家");
                                            }
                                            break;*/
                                        case "name":
                                            String name = resultList.get(1);
                                            if (sdNameList.contains(name)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备名称重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                for (int i = 0; i < loopTotal; i++){
                                                    String nameTemp = loopParamVOList.get(i).getName();
                                                    if(nameTemp==null){
                                                        loopParamVOList.get(i).setName(name);
                                                        sdNameList.add(name);
                                                        break;
                                                    }
                                                }
                                            }
                                            break;
                                        case "lampPosition":
                                            String lampPosition = cell.getStringCellValue();
                                            Integer lampPositionId = lampPositionMap.get(lampPosition);
                                            if(lampPositionId!=null){
                                                for (int i = 0; i < loopTotal; i++){
                                                    Integer lampPositionIdTemp = loopParamVOList.get(i).getLampPositionId();
                                                    if(lampPositionIdTemp==null){
                                                        loopParamVOList.get(i).setLampPositionId(lampPositionId);
                                                        break;
                                                    }
                                                }
                                            }else {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("请按规定填写灯具位置");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            }
                                            break;
                                        case "num":
                                            String num = resultList.get(1);
                                            String deviceNum = "";
                                            Integer deviceTypeIdTemp = singleLampParamReqVO.getDeviceTypeId();
                                            if(deviceTypeIdTemp != null && deviceTypeIdTemp != 0){
                                                List<Integer> specialDeviceTypeList = Arrays.asList(1, 2, 5, 6);
                                                if(specialDeviceTypeList.contains(deviceTypeIdTemp)){
                                                    try {
                                                        deviceNum = Long.toHexString(Long.parseLong(num));
                                                    }catch (Exception e){
                                                        ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                        importDeviceFailVO.setRows(key);
                                                        importDeviceFailVO.getFailMsgList().add("对应设备类型编号只能为数字");
                                                        importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                                        break;
                                                    }
                                                    deviceNum = deviceNum.toUpperCase();
                                                    String prefix = "";
                                                    for (int i = 0; i < 8 - deviceNum.length(); i++){
                                                        prefix += "0";
                                                    }
                                                    deviceNum = prefix + deviceNum;
                                                    if(deviceTypeIdTemp==1||deviceTypeIdTemp==2){
                                                        String sendId = "00" + num;
                                                        singleLampParamReqVO.setSendId(sendId);
                                                    }else if(deviceTypeIdTemp==5||deviceTypeIdTemp==6){
                                                        String sendId = "cat1/00" + num;
                                                        singleLampParamReqVO.setSendId(sendId);
                                                    }
                                                }else {
                                                    deviceNum = num;
                                                    if(deviceTypeIdTemp==3||deviceTypeIdTemp==4){
                                                        singleLampParamReqVO.setSendId(deviceNum);
                                                    }
                                                }
                                                for (SystemDevice systemDevice : sdObjectList) {
                                                    if(deviceNum.equals(systemDevice.getNum()) && singleLampParamReqVO.getDeviceTypeId()==(systemDevice.getDeviceTypeId())){
                                                        ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                        importDeviceFailVO.setRows(key);
                                                        importDeviceFailVO.getFailMsgList().add("设备编号重复");
                                                        importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                                        break;
                                                    }
                                                }
                                                SystemDevice systemDeviceTemp = new SystemDevice();
                                                systemDeviceTemp.setDeviceTypeId(singleLampParamReqVO.getDeviceTypeId());
                                                systemDeviceTemp.setNum(deviceNum);
                                                sdObjectList.add(systemDeviceTemp);
                                                singleLampParamReqVO.setNum(deviceNum);
                                            }
                                            break;
                                        case "":
                                            return new Result().error("【2" + titleKey + "为空值，请按模板填写提交");
                                        default:
                                            return new Result().error("【2" + titleKey + "为无法匹配的值，请按模板填写提交");
                                    }

                                } else {
                                    //将错误信息添加到集合中
                                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                    importDeviceFailVO.setRows(key);
                                    importDeviceFailVO.getFailMsgList().add(resultList.get(0));
                                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                }

                            }
                        }
                        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
                            lampDeviceList.add(singleLampParamReqVO);
                        }
                    }

                    //MeteorologicalDevice
                    if (meteorologicalDeviceNum != null) {
                        //设置灯杆类型
                        MeteorologicalDevice meteorologicalDevice = new MeteorologicalDevice();
                        meteorologicalDevice.setLampPostId(lampPostId);
                        meteorologicalDevice.setCreateTime(new Date());
                        for (int cellNum = meteorologicalDeviceNum + 1; cellNum < meteorologicalDeviceNum + 7; cellNum++) {

                            //获取列cell对象
                            Cell cell = rowData.getCell(cellNum);
                            if (cell != null) {

                                String key = getKey(rowNum, cellNum);
                                Cell titleCell = rowTitle.getCell(cellNum);
                                String titleCellValue = titleCell.getStringCellValue();
                                String titleKey = key.substring(key.indexOf("-"), key.indexOf("】") + 1);

                                //获取数据库表结构信息
                                TableStructure tableStructure = meteorologicalDeviceStructureMap.get(titleCellValue).get(0);
                                //判断数据是否符合要求填写
                                List<String> resultList = excelCheckUtil(tableStructure, cell);

                                if ("".equals(resultList.get(0))) {
                                    switch (titleCellValue) {
                                        case "model":
                                            String model = resultList.get(1);
                                            meteorologicalDevice.setModel(model);
                                            break;
                                        case "ip":

                                            String ip = resultList.get(1);
                                            meteorologicalDevice.setIp(ip);
                                            break;
                                        case "port":
                                            Integer port = new Double(resultList.get(1)).intValue();
                                            meteorologicalDevice.setPort(port);
                                            break;
                                        case "factory":
                                            String factory = resultList.get(1);
                                            meteorologicalDevice.setFactory(factory);
                                            break;
                                        case "name":
                                            String name = resultList.get(1);
                                            if (medNameList.contains(name)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备名称重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                medNameList.add(name);
                                            }
                                            meteorologicalDevice.setName(name);
                                            break;
                                        case "num":
                                            String num = resultList.get(1);
                                            if (medNumList.contains(num)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备编号重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                medNumList.add(num);
                                            }
                                            meteorologicalDevice.setNum(num);
                                            break;
                                        case "":
                                            return new Result().error("【2" + titleKey + "为空值，请按模板填写提交");
                                        default:
                                            return new Result().error("【2" + titleKey + "为无法匹配的值，请按模板填写提交");
                                    }

                                } else {
                                    //将错误信息添加到集合中
                                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                    importDeviceFailVO.setRows(key);
                                    importDeviceFailVO.getFailMsgList().add(resultList.get(0));
                                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                }

                            }
                        }
                        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
                            meteorologicalDeviceList.add(meteorologicalDevice);
                        }

                    }

                    //RadioDevice
                    if (radioDeviceNum != null) {
                        //设置灯杆类型
                        RadioDevice radioDevice = new RadioDevice();
                        radioDevice.setLampPostId(lampPostId);
                        radioDevice.setCreateTime(new Date());
                        for (int cellNum = radioDeviceNum + 1; cellNum < radioDeviceNum + 9; cellNum++) {

                            //获取列cell对象
                            Cell cell = rowData.getCell(cellNum);
                            if (cell != null) {

                                String key = getKey(rowNum, cellNum);
                                Cell titleCell = rowTitle.getCell(cellNum);
                                String titleCellValue = titleCell.getStringCellValue();
                                String titleKey = key.substring(key.indexOf("-"), key.indexOf("】") + 1);

                                //获取数据库表结构信息
                                TableStructure tableStructure = radioDeviceStructureMap.get(titleCellValue).get(0);
                                //判断数据是否符合要求填写
                                List<String> resultList = excelCheckUtil(tableStructure, cell);

                                if ("".equals(resultList.get(0))) {
                                    switch (titleCellValue) {
                                        case "model":
                                            String model = resultList.get(1);
                                            radioDevice.setModel(model);
                                            break;
                                        case "ip":

                                            String ip = resultList.get(1);
                                            radioDevice.setIp(ip);
                                            break;
                                        case "mac":
                                            String port = resultList.get(1);
                                            radioDevice.setMac(port);
                                            break;
                                        case "volume":
                                            Integer volume = new Double(resultList.get(1)).intValue();
                                            radioDevice.setVolume(volume);
                                            break;
                                        case "factory":
                                            String factory = resultList.get(1);
                                            radioDevice.setFactory(factory);
                                            break;
                                        case "name":
                                            String name = resultList.get(1);
                                            if (radNameList.contains(name)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备名称重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                radNameList.add(name);
                                            }
                                            radioDevice.setName(name);
                                            break;
                                        case "num":
                                            String num = resultList.get(1);
                                            if (radNumList.contains(num)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备编号重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                radNumList.add(num);
                                            }
                                            radioDevice.setNum(num);
                                            break;
                                        case "term_id":
                                            Integer termId = new Double(resultList.get(1)).intValue();
                                            if (radLtNumList.contains(termId)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("雷拓终端编号重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                radLtNumList.add(termId);
                                            }
                                            radioDevice.setTermId(termId);
                                            break;
                                        case "":
                                            return new Result().error("【2" + titleKey + "为空值，请按模板填写提交");
                                        default:
                                            return new Result().error("【2" + titleKey + "为无法匹配的值，请按模板填写提交");
                                    }

                                } else {
                                    //将错误信息添加到集合中
                                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                    importDeviceFailVO.setRows(key);
                                    importDeviceFailVO.getFailMsgList().add(resultList.get(0));
                                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                }

                            }
                        }
                        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
                            radioDeviceList.add(radioDevice);
                        }

                    }

                    //ScreenDevice
                    if (screenDeviceNum != null) {
                        //设置灯杆类型
                        ScreenDevice screenDevice = new ScreenDevice();
                        screenDevice.setLampPostId(lampPostId);
                        screenDevice.setCreateTime(new Date());
                        for (int cellNum = screenDeviceNum + 1; cellNum < screenDeviceNum + 11; cellNum++) {

                            //获取列cell对象
                            Cell cell = rowData.getCell(cellNum);
                            if (cell != null) {

                                String key = getKey(rowNum, cellNum);
                                Cell titleCell = rowTitle.getCell(cellNum);
                                String titleCellValue = titleCell.getStringCellValue();
                                String titleKey = key.substring(key.indexOf("-"), key.indexOf("】") + 1);

                                //获取数据库表结构信息
                                TableStructure tableStructure = screenDeviceStructureMap.get(titleCellValue).get(0);
                                //判断数据是否符合要求填写
                                List<String> resultList = excelCheckUtil(tableStructure, cell);

                                if ("".equals(resultList.get(0))) {


                                    switch (titleCellValue) {
                                        case "ip":
                                            String ip = resultList.get(1);
                                            screenDevice.setIp(ip);
                                            break;
                                        case "port":
                                            Integer port = new Double(resultList.get(1)).intValue();
                                            screenDevice.setPort(port);
                                            break;
                                        case "width":
                                            Float width = new Float(resultList.get(1));
                                            screenDevice.setWidth(width);
                                            break;
                                        case "height":
                                            Float height = new Float(resultList.get(1));
                                            screenDevice.setHeight(height);
                                            break;
                                        case "bright":
                                            Integer bright = new Double(resultList.get(1)).intValue();
                                            screenDevice.setBright(bright);
                                            break;
                                        case "volume":
                                            Integer volume = new Double(resultList.get(1)).intValue();
                                            screenDevice.setVolume(volume);
                                            break;
                                        case "factory":
                                            String factory = resultList.get(1);
                                            screenDevice.setFactory(factory);
                                            break;
                                        case "name":
                                            String name = resultList.get(1);
                                            if (scdNameList.contains(name)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备名称重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                scdNameList.add(name);
                                            }
                                            screenDevice.setName(name);
                                            break;
                                        case "num":
                                            String num = resultList.get(1);
                                            if (scdNumList.contains(num)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备编号重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                scdNumList.add(num);
                                            }
                                            screenDevice.setNum(num);
                                            break;
                                        case "model":
                                            String model = resultList.get(1);
                                            screenDevice.setModel(model);
                                            break;
                                        case "":
                                            return new Result().error("【2" + titleKey + "为空值，请按模板填写提交");
                                        default:
                                            return new Result().error("【2" + titleKey + "为无法匹配的值，请按模板填写提交");
                                    }

                                } else {
                                    //将错误信息添加到集合中
                                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                    importDeviceFailVO.setRows(key);
                                    importDeviceFailVO.getFailMsgList().add(resultList.get(0));
                                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                }

                            }
                        }
                        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
                            screenDeviceList.add(screenDevice);
                        }

                    }

                    //SsDevice
                    if (ssDeviceNum != null) {
                        //设置灯杆类型
                        SsDevice ssDevice = new SsDevice();
                        ssDevice.setLampPostId(lampPostId);
                        ssDevice.setCreateTime(new Date());
                        for (int cellNum = ssDeviceNum + 1; cellNum < ssDeviceNum + 8; cellNum++) {

                            //获取列cell对象
                            Cell cell = rowData.getCell(cellNum);
                            if (cell != null) {

                                String key = getKey(rowNum, cellNum);
                                Cell titleCell = rowTitle.getCell(cellNum);
                                String titleCellValue = titleCell.getStringCellValue();
                                String titleKey = key.substring(key.indexOf("-"), key.indexOf("】") + 1);

                                //获取数据库表结构信息
                                TableStructure tableStructure = ssDeviceStructureMap.get(titleCellValue).get(0);
                                //判断数据是否符合要求填写
                                List<String> resultList = excelCheckUtil(tableStructure, cell);

                                if ("".equals(resultList.get(0))) {

                                    switch (titleCellValue) {
                                        case "model":
                                            String model = resultList.get(1);
                                            ssDevice.setModel(model);
                                            break;
                                        case "type":
                                            Integer type = new Double(resultList.get(1)).intValue();
                                            ssDevice.setType(type);
                                            break;
                                        case "ip":
                                            String ip = resultList.get(1);
                                            ssDevice.setIp(ip);
                                            break;
                                        case "port":
                                            Integer port = new Double(resultList.get(1)).intValue();
                                            ssDevice.setPort(port);
                                            break;
                                        case "factory":
                                            String factory = resultList.get(1);
                                            ssDevice.setFactory(factory);
                                            break;
                                        case "name":
                                            String name = resultList.get(1);
                                            if (ssdNameList.contains(name)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备名称重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                ssdNameList.add(name);
                                            }
                                            ssDevice.setName(name);
                                            break;
                                        case "num":
                                            String num = resultList.get(1);
                                            if (ssdNumList.contains(num)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备编号重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                ssdNumList.add(num);
                                            }
                                            ssDevice.setNum(num);
                                            break;
                                        case "":
                                            return new Result().error("【2" + titleKey + "为空值，请按模板填写提交");
                                        default:
                                            return new Result().error("【2" + titleKey + "为无法匹配的值，请按模板填写提交");
                                    }

                                } else {
                                    //将错误信息添加到集合中
                                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                    importDeviceFailVO.setRows(key);
                                    importDeviceFailVO.getFailMsgList().add(resultList.get(0));
                                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                }

                            }
                        }
                        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
                            ssDeviceList.add(ssDevice);
                        }

                    }

                    //WifiApDevice
                    if (wifiApDeviceNum != null) {
                        //设置灯杆类型
                        WifiApDevice wifiApDevice = new WifiApDevice();
                        wifiApDevice.setLampPostId(lampPostId);
                        wifiApDevice.setCreateTime(new Date());
                        for (int cellNum = wifiApDeviceNum + 1; cellNum < wifiApDeviceNum + 7; cellNum++) {

                            //获取列cell对象
                            Cell cell = rowData.getCell(cellNum);
                            if (cell != null) {

                                String key = getKey(rowNum, cellNum);
                                Cell titleCell = rowTitle.getCell(cellNum);
                                String titleCellValue = titleCell.getStringCellValue();
                                String titleKey = key.substring(key.indexOf("-"), key.indexOf("】") + 1);

                                //获取数据库表结构信息
                                TableStructure tableStructure = wifiApDeviceStructureMap.get(titleCellValue).get(0);
                                //判断数据是否符合要求填写
                                List<String> resultList = excelCheckUtil(tableStructure, cell);

                                if ("".equals(resultList.get(0))) {


                                    switch (titleCellValue) {
                                        case "model":
                                            String model = resultList.get(1);
                                            wifiApDevice.setModel(model);
                                            break;
                                        case "ip":
                                            String ip = resultList.get(1);
                                            wifiApDevice.setIp(ip);
                                            break;
                                        case "mac":
                                            String mac = resultList.get(1);
                                            wifiApDevice.setMac(mac);
                                            break;
                                        case "factory":
                                            String factory = resultList.get(1);
                                            wifiApDevice.setFactory(factory);
                                            break;
                                        case "name":
                                            String name = resultList.get(1);
                                            if (widNameList.contains(name)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备名称重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                widNameList.add(name);
                                            }
                                            wifiApDevice.setName(name);
                                            break;
                                        case "num":
                                            String num = resultList.get(1);
                                            if (widNumList.contains(num)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备编号重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                widNumList.add(num);
                                            }
                                            wifiApDevice.setNum(num);
                                            break;
                                        case "":
                                            return new Result().error("【2" + titleKey + "为空值，请按模板填写提交");
                                        default:
                                            return new Result().error("【2" + titleKey + "为无法匹配的值，请按模板填写提交");
                                    }

                                } else {
                                    //将错误信息添加到集合中
                                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                    importDeviceFailVO.setRows(key);
                                    importDeviceFailVO.getFailMsgList().add(resultList.get(0));
                                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                }

                            }
                        }
                        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
                            wifiApDeviceList.add(wifiApDevice);
                        }

                    }

                    //WifiAcDevice
                    if (wifiAcDeviceNum != null) {
                        //设置灯杆类型
                        WifiAcDevice wifiAcDevice = new WifiAcDevice();
                        wifiAcDevice.setCreateTime(new Date());
                        for (int cellNum = wifiAcDeviceNum + 1; cellNum < wifiAcDeviceNum + 8; cellNum++) {

                            //获取列cell对象
                            Cell cell = rowData.getCell(cellNum);
                            if (cell != null) {

                                String key = getKey(rowNum, cellNum);
                                Cell titleCell = rowTitle.getCell(cellNum);
                                String titleCellValue = titleCell.getStringCellValue();
                                String titleKey = key.substring(key.indexOf("-"), key.indexOf("】") + 1);

                                //获取数据库表结构信息
                                TableStructure tableStructure = wifiAcDeviceStructureMap.get(titleCellValue).get(0);
                                //判断数据是否符合要求填写
                                List<String> resultList = excelCheckUtil(tableStructure, cell);

                                if ("".equals(resultList.get(0))) {

                                    switch (titleCellValue) {
                                        case "model":
                                            String model = resultList.get(1);
                                            wifiAcDevice.setModel(model);
                                            break;
                                        case "ip":
                                            String ip = resultList.get(1);
                                            wifiAcDevice.setIp(ip);
                                            break;
                                        case "mac":
                                            String mac = resultList.get(1);
                                            wifiAcDevice.setMac(mac);
                                            break;
                                        case "factory":
                                            String factory = resultList.get(1);
                                            wifiAcDevice.setFactory(factory);
                                            break;
                                        case "name":
                                            String name = resultList.get(1);
                                            if (wicdNameList.contains(name)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备名称重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                wicdNameList.add(name);
                                            }
                                            wifiAcDevice.setName(name);
                                            break;
                                        case "num":
                                            String num = resultList.get(1);
                                            if (wicdNumList.contains(num)) {
                                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                                importDeviceFailVO.setRows(key);
                                                importDeviceFailVO.getFailMsgList().add("设备编号重复");
                                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                            } else {
                                                wicdNumList.add(num);
                                            }
                                            wifiAcDevice.setNum(num);
                                            break;
                                        case "conn_ap_count":
                                            Integer connApCount = new Double(resultList.get(1)).intValue();
                                            wifiAcDevice.setConnApCount(connApCount);
                                            break;
                                        case "":
                                            return new Result().error("【2" + titleKey + "为空值，请按模板填写提交");
                                        default:
                                            return new Result().error("【2" + titleKey + "为无法匹配的值，请按模板填写提交");
                                    }

                                } else {
                                    //将错误信息添加到集合中
                                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                    importDeviceFailVO.setRows(key);
                                    importDeviceFailVO.getFailMsgList().add(resultList.get(0));
                                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                }

                            }
                        }
                        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
                            wifiAcDeviceList.add(wifiAcDevice);
                        }

                    }

                }
            }
        } catch (Exception e) {
            logger.info("未按模板填写并提交数据：(changeExcelToList)" + e);
            e.printStackTrace();
            return new Result().error("请按模板填写并提交数据");
        } finally {
            if (file != null) {
                try {
                    //关流
                    file.close();
                } catch (IOException e) {
                    logger.info("关流失败：(changeExcelToList)" + e);
                }

            }
        }

        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
            return new Result().success("转换成功", resultMap);
        } else {
            logger.info("转换List失败，文件名：" + fileName);
            importDeviceResultVO.setFailNum(failNum);
            return new Result().error("上传失败", importDeviceResultVO);
        }
    }

    /**
     * 将文件写入磁盘
     *
     * @param multipartFile
     * @return
     */
    @Override
    public Result writeFile(MultipartFile multipartFile) {
        //上传文件
        if (multipartFile.isEmpty()) {
            return new Result().error("文件为空");
        }
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        logger.info("写入文件，文件名称：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!"xls".equals(suffixName) && !"xlsx".equals(suffixName)) {
            logger.info("导入文件" + fileName + "失败,文件类型错误,不是Excel文件");
            return new Result().error("导入文件" + fileName + "失败,文件类型错误,不是Excel文件");
        }
        String realName = System.currentTimeMillis() + "." + suffixName;
        String batchUploadPath = pathApi.getUpload();
        File folder = new File(batchUploadPath);
        if(!folder.exists()){
            folder.mkdir();
        }
        String realPath = batchUploadPath + realName;
        try {
            multipartFile.transferTo(new File(realPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("fileName", fileName);
        paramMap.put("realPath", realPath);
        return new Result().success(paramMap);
    }

    /**
     * 将数据插入数据库
     *
     * @param result
     * @return
     */
    @Override
    public Result saveList(Result result,HttpServletRequest request) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        logger.info("将数据插入数据库，传入参数：" + result.getCode());
        Result realResult = result;
        ImportDeviceResultVO importDeviceResultVO = new ImportDeviceResultVO();
        Integer successNum = 0;
        Integer failNum = 0;

        if (realResult.getCode() == 200) {
            Map resultData = (Map) realResult.getData();
            List<AhDevice> ahDeviceList = (List<AhDevice>) resultData.get("ahDeviceList");
            List<SingleLampParamReqVO> singleLampParamReqVOList = (List<SingleLampParamReqVO>) resultData.get("lampDeviceList");
            List<ScreenDevice> screenDeviceList = (List<ScreenDevice>) resultData.get("screenDeviceList");
            List<SsDevice> ssDeviceList = (List<SsDevice>) resultData.get("ssDeviceList");
            List<WifiApDevice> wifiApDeviceList = (List<WifiApDevice>) resultData.get("wifiApDeviceList");
            List<MeteorologicalDevice> meteorologicalDeviceList = (List<MeteorologicalDevice>) resultData.get("meteorologicalDeviceList");
            List<RadioDevice> radioDeviceList = (List<RadioDevice>) resultData.get("radioDeviceList");
            List<WifiAcDevice> wifiAcDeviceList = (List<WifiAcDevice>) resultData.get("wifiAcDeviceList");

            if (!ahDeviceList.isEmpty()) {
                int size = ahDeviceList.size();
                try {
                    Result ahDeviceListResult = ahDeviceService.addList(ahDeviceList);
                    if (!("批量导入AhDevice成功".equals(ahDeviceListResult.getMessage()))) {
                        failNum += size;
                        ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                        importDeviceFailVO.setRows("导入失败");
                        importDeviceFailVO.getFailMsgList().add("一键呼叫");
                        importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                        logger.info("一键呼叫表格导入失败");
                    }else {
                        successNum += size;
                    }
                } catch (Exception e) {
                    failNum += size;
                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                    importDeviceFailVO.setRows("导入失败");
                    importDeviceFailVO.getFailMsgList().add("一键呼叫");
                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                    logger.info("ahDeviceList插入数据库出错", e);
                }
            }

            if (!singleLampParamReqVOList.isEmpty()) {
                int size = singleLampParamReqVOList.size();
                try {
                    List<SingleLampParamReqVO> dxnbSingleLampParamReqVO = singleLampParamReqVOList.stream().filter(e ->
                            e.getDeviceTypeId().equals(7) || e.getDeviceTypeId().equals(8)
                    ).collect(Collectors.toList());
                    List<String> dxnbDeviceNameList = dxnbSingleLampParamReqVO.stream().map(e -> e.getLoopParamVOList().get(0).getName()).collect(Collectors.toList());
                    List<String> dxnbDeviceNumList = dxnbSingleLampParamReqVO.stream().map(SingleLampParamReqVO::getNum).collect(Collectors.toList());
                    Result dxnbAddResult = ctWingApi.batchCreateDevice(dxnbDeviceNameList, dxnbDeviceNumList);
                    List<DlmBatchCreateDeviceRespVO> dlmBatchCreateDeviceRespVOList = new ArrayList<>();
                    if(dxnbAddResult.getCode()==200&&dxnbAddResult.getData()!=null){
                        dlmBatchCreateDeviceRespVOList = JSONArray.parseArray(dxnbAddResult.getData().toString(), DlmBatchCreateDeviceRespVO.class);
                    }
                    Map<String, DlmBatchCreateDeviceRespVO> dlmBatchCreateDeviceRespVOMap = dlmBatchCreateDeviceRespVOList.stream().collect(Collectors.toMap(DlmBatchCreateDeviceRespVO::getImei, obj -> obj));

                    for (int i = 0; i < singleLampParamReqVOList.size(); i++){
                        SingleLampParamReqVO singleLampParamReqVO = singleLampParamReqVOList.get(i);
                        SystemDeviceParameter sdpLampPosition = systemDeviceParameterService.selectByName(singleLampParamReqVO.getDeviceTypeId(), "灯具位置");
                        SystemDeviceParameter sdpLoopNum = systemDeviceParameterService.selectByName(singleLampParamReqVO.getDeviceTypeId(), "支路数");
                        SystemDeviceParameter sdpBrightness = systemDeviceParameterService.selectByName(singleLampParamReqVO.getDeviceTypeId(), "亮度");
                        Integer sdpLampPositionId = null;
                        Integer sdpLoopNumId = null;
                        Integer sdpBrightnessId = null;
                        if(sdpLampPosition!=null){
                            sdpLampPositionId = sdpLampPosition.getId();
                        }
                        if(sdpLoopNum!=null){
                            sdpLoopNumId = sdpLoopNum.getId();
                        }
                        if(sdpBrightness!=null){
                            sdpBrightnessId = sdpBrightness.getId();
                        }
                        SystemDevice systemDevice = new SystemDevice();
                        BeanUtils.copyProperties(singleLampParamReqVO, systemDevice);
                        systemDevice.setCreateTime(new Date());
                        systemDevice.setDeviceState(0);
                        systemDevice.setLastOnlineTime(new Date(System.currentTimeMillis()-86400000));
                        systemDevice.setIsOnline(0);
                        systemDevice.setReserveOne(singleLampParamReqVO.getSendId());
                        systemDevice.setCreator(userId);

                        if(systemDevice.getDeviceTypeId()==3||systemDevice.getDeviceTypeId()==4){
                            String devEui = systemDevice.getReserveOne();
                            boolean createNode = MessageOperationUtil.createNode("批量导入" + devEui, devEui);
                            if(createNode){
                                MessageOperationUtil.createNodeMc(devEui, loraApi.getLoraMcId());
                            }else {
                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                importDeviceFailVO.setRows("第"+(i+4)+"行");
                                importDeviceFailVO.getFailMsgList().add("灯具编号错误");
                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                failNum++;
                                continue;
                            }
                        }
                        if(systemDevice.getDeviceTypeId()==9||systemDevice.getDeviceTypeId()==10){
                            String devEui = systemDevice.getNum();
                            systemDevice.setReserveOne(devEui);
                            String newLoraId = MessageOperationUtil.createNewLoraNode(devEui);
                            if(newLoraId==null||newLoraId.length()==0){
                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                importDeviceFailVO.setRows("第"+(i+4)+"行");
                                importDeviceFailVO.getFailMsgList().add("灯具编号错误");
                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                failNum++;
                                continue;
                            }else {
                                systemDevice.setNum(newLoraId);
                            }
                        }
                        if(systemDevice.getDeviceTypeId()==7||systemDevice.getDeviceTypeId()==8){
                            String systemDeviceNum = systemDevice.getNum();
                            DlmBatchCreateDeviceRespVO dlmBatchCreateDeviceRespVO = dlmBatchCreateDeviceRespVOMap.get(systemDeviceNum);
                            String deviceId = dlmBatchCreateDeviceRespVO.getDeviceId();
                            if(deviceId==null||deviceId.length()==0){
                                ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                                importDeviceFailVO.setRows("第"+(i+4)+"行");
                                importDeviceFailVO.getFailMsgList().add(dlmBatchCreateDeviceRespVO.getDescription());
                                importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                                failNum++;
                                continue;
                            }else {
                                systemDevice.setReserveOne(deviceId);
                            }
                        }
                        //boolean lampDeviceSave = true;
                        //List<Integer> errorIdList = new ArrayList<>();
                        List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();
                        for (LoopParamVO loopParamVO : loopParamVOList) {
                            SystemDevice systemDeviceTemp = new SystemDevice();
                            BeanUtils.copyProperties(systemDevice, systemDeviceTemp);
                            systemDeviceTemp.setName(loopParamVO.getName());
                            /*singleLampParam.setLoopTypeId(singleLampParamReqVO.getLoopTypeId());
                            singleLampParam.setCreateTime(new Date());
                            singleLampParam.setBrightState(0);
                            singleLampParam.setBrightness(0);
                            singleLampParam.setDeviceNum(singleLampParamReqVO.getNum());
                            singleLampParam.setDeviceId(lampDevice.getId());*/
                            systemDeviceService.save(systemDeviceTemp);
                            lampDeviceParameterService.addDefaultParamValue(systemDeviceTemp.getId(),singleLampParamReqVO.getDeviceTypeId());
                            if(sdpLampPositionId!=null){
                                lampDeviceParameterService.saveParamValue(systemDeviceTemp.getId(),sdpLampPositionId,String.valueOf(loopParamVO.getLampPositionId()));
                            }
                            if(sdpLoopNumId!=null){
                                lampDeviceParameterService.saveParamValue(systemDeviceTemp.getId(),sdpLoopNumId,String.valueOf(i+1));
                            }
                            if(sdpBrightnessId!=null){
                                lampDeviceParameterService.saveParamValue(systemDeviceTemp.getId(),sdpBrightnessId,"50");
                            }
                            //errorIdList.add(systemDeviceTemp.getId());
                        }
                        successNum++;
                    }
                    /*if (!importDeviceResultVO.getFailInfoList().isEmpty()) {
                        errorMap.put("灯控导入成功数量", ""+successNum);
                        logger.info("灯控表格部分导入失败");
                    }*/
                } catch (Exception e) {
                    failNum += size;
                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                    importDeviceFailVO.setRows("导入失败");
                    importDeviceFailVO.getFailMsgList().add("灯控");
                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                    logger.info("systemDeviceList插入数据库出错", e);
                }
            }

            if (!screenDeviceList.isEmpty()) {
                int size = screenDeviceList.size();
                try {


                    Result screenDeviceResult = screenDeviceService.addList(screenDeviceList);
                    if (!("批量导入ScreenDevice成功".equals(screenDeviceResult.getMessage()))) {
                        failNum += size;
                        ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                        importDeviceFailVO.setRows("导入失败");
                        importDeviceFailVO.getFailMsgList().add("信息屏");
                        importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                        logger.info("信息屏表格导入失败");
                    }else {
                        successNum += size;
                    }
                } catch (Exception e) {
                    failNum += size;
                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                    importDeviceFailVO.setRows("导入失败");
                    importDeviceFailVO.getFailMsgList().add("信息屏");
                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                    logger.info("screenDeviceList插入数据库出错", e);
                }
            }

            if (!ssDeviceList.isEmpty()) {
                int size = ssDeviceList.size();
                try {

                    Result ssDeviceResult = ssDeviceService.addList(ssDeviceList);
                    if (!("批量导入SsDevice成功".equals(ssDeviceResult.getMessage()))) {
                        failNum += size;
                        ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                        importDeviceFailVO.setRows("导入失败");
                        importDeviceFailVO.getFailMsgList().add("摄像头");
                        importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                        logger.info("摄像头表格导入失败");
                    }else {
                        successNum += size;
                    }
                } catch (Exception e) {
                    failNum += size;
                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                    importDeviceFailVO.setRows("导入失败");
                    importDeviceFailVO.getFailMsgList().add("摄像头");
                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                    logger.info("ssDeviceList插入数据库出错", e);
                }
            }

            if (!wifiApDeviceList.isEmpty()) {
                int size = wifiApDeviceList.size();
                try {
                    Result wifiApDeviceResult = wifiApDeviceService.addList(wifiApDeviceList);
                    if (!("批量导入WifiApDevice成功".equals(wifiApDeviceResult.getMessage()))) {
                        failNum += size;
                        ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                        importDeviceFailVO.setRows("导入失败");
                        importDeviceFailVO.getFailMsgList().add("AP");
                        importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                        logger.info("AP表格导入失败");
                    }else {
                        successNum += size;
                    }
                } catch (Exception e) {
                    failNum += size;
                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                    importDeviceFailVO.setRows("导入失败");
                    importDeviceFailVO.getFailMsgList().add("AP");
                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                    logger.info("wifiApDeviceList插入数据库出错", e);
                }
            }

            if (!meteorologicalDeviceList.isEmpty()) {
                int size = meteorologicalDeviceList.size();
                try {
                    Result meteorologicalDeviceResult = meteorologicalDeviceService.addList(meteorologicalDeviceList);
                    if (!("批量导入MeteorologicalDevice成功".equals(meteorologicalDeviceResult.getMessage()))) {
                        failNum += size;
                        ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                        importDeviceFailVO.setRows("导入失败");
                        importDeviceFailVO.getFailMsgList().add("传感器");
                        importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                        logger.info("传感器表格导入失败");
                    }else {
                        successNum += size;
                    }
                } catch (Exception e) {
                    failNum += size;
                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                    importDeviceFailVO.setRows("导入失败");
                    importDeviceFailVO.getFailMsgList().add("传感器");
                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                    logger.info("meteorologicalDeviceList插入数据库出错", e);
                }
            }

            if (!radioDeviceList.isEmpty()) {
                int size = radioDeviceList.size();
                try {
                    Result radioDeviceResult = radioDeviceService.addList(radioDeviceList);
                    if (!("批量导入RadioDevice成功".equals(radioDeviceResult.getMessage()))) {
                        failNum += size;
                        ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                        importDeviceFailVO.setRows("导入失败");
                        importDeviceFailVO.getFailMsgList().add("广播");
                        importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                        logger.info("广播表格导入失败");
                    }else {
                        successNum += size;
                    }
                } catch (Exception e) {
                    failNum += size;
                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                    importDeviceFailVO.setRows("导入失败");
                    importDeviceFailVO.getFailMsgList().add("广播");
                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                    logger.info("radioDeviceList插入数据库出错", e);
                }
            }

            if (!wifiAcDeviceList.isEmpty()) {
                int size = wifiAcDeviceList.size();
                try {
                    Result wifiAcDeviceResult = wifiAcDeviceService.addList(wifiAcDeviceList);
                    if (!("批量导入WifiAcDevice成功".equals(wifiAcDeviceResult.getMessage()))) {
                        failNum += size;
                        ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                        importDeviceFailVO.setRows("导入失败");
                        importDeviceFailVO.getFailMsgList().add("AC");
                        importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                        logger.info("AC表格导入失败");
                    }else {
                        successNum += size;
                    }
                } catch (Exception e) {
                    failNum += size;
                    ImportDeviceFailVO importDeviceFailVO = new ImportDeviceFailVO();
                    importDeviceFailVO.setRows("导入失败");
                    importDeviceFailVO.getFailMsgList().add("AC");
                    importDeviceResultVO.getFailInfoList().add(importDeviceFailVO);
                    logger.info("wifiAcDeviceList插入数据库出错", e);
                }
            }
        } else {
            return result;
        }

        if (importDeviceResultVO.getFailInfoList().isEmpty()) {
            logger.info("表格已完整导入");
            realResult.success("表格已完整导入");
        } else {
            importDeviceResultVO.setSuccessNum(successNum);
            importDeviceResultVO.setFailNum(failNum);
            realResult.success("部分导入出错",importDeviceResultVO);
        }

        return realResult;
    }

    /**
     * 判断Excel表格数据是否符合规范
     *
     * @param tableStructure
     * @param cell
     * @return
     */
    public List<String> excelCheckUtil(TableStructure tableStructure, Cell cell) {

        List<String> resultList = new ArrayList<>();
        String result = "";
        String value = "";
        //初步判断字段类型
        String columnType = tableStructure.getColumnType();
        if ("datetime".equals(columnType)) {
            resultList.add(result);
            resultList.add(value);
            return resultList;
        }
        if ("float".equals(columnType) || "double".equals(columnType)) {
            int cellType = cell.getCellType();
            switch (cellType) {
                case HSSFCell.CELL_TYPE_BLANK://空
                    //判断当前字段是否可以为空
                    String nullable = tableStructure.getNullable();
                    if (nullable.equals("NO")) {
                        //如果不能为空，添加错误信息，跳出当前循环
                        result = "该数据不能为空";
                    }
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC://数字
                    value = String.valueOf(cell.getNumericCellValue());
                    break;
                default:
                    result = "数据类型错误";
                    break;
            }
        } else {
            //获取最大长度
            String length = columnType.substring(columnType.indexOf("(") + 1, columnType.indexOf(")"));
            int size = Integer.parseInt(length);
            //获取字段类型
            String dataType = tableStructure.getDataType();
            int cellType = cell.getCellType();
            //判断单元格数据类型
            switch (cellType) {
                case HSSFCell.CELL_TYPE_STRING://字符串
                    //判断类型
                    if (!("varchar".equals(dataType))) {
                        result = "数据类型错误";
                        break;
                    } else {
                        value = cell.getStringCellValue();
                    }
                    //判断长度
                    int strlength = cell.getStringCellValue().length();
                    if (strlength > size) {
                        result = "超出最大长度（" + size + "）";
                    }
                    break;
                case HSSFCell.CELL_TYPE_BLANK://空
                    //判断当前字段是否可以为空
                    String nullable = tableStructure.getNullable();
                    if (nullable.equals("NO")) {
                        //如果不能为空，添加错误信息，跳出当前循环
                        result = "该数据不能为空";
                    }
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC://数字
                    //判断类型
                    if (!("int".equals(dataType)) && !("varchar".equals(dataType))) {
                        result = "数据类型错误";
                        break;
                    } else {

                        value = String.valueOf(cell.getNumericCellValue());
                    }
                    //判断长度
                    int numlength = String.valueOf(new Double(cell.getNumericCellValue()).intValue()).length();
                    if (numlength > size) {
                        result = "超出最大长度（" + size + "）";
                    }
                    break;
                default:
                    result = "数据类型错误";
                    break;
            }
        }

        resultList.add(result);
        resultList.add(value);
        return resultList;
    }


    /**
     * 获取当前列的坐标值
     *
     * @param cellNum
     * @param rowNum
     * @return
     */
    public String getKey(int rowNum, int cellNum) {
        String key = "";
        if (cellNum < 26) {
            key = new String("【" + (rowNum + 1) + "-" + (char) (cellNum + 65) + "】");
        } else if (cellNum >= 26 && cellNum < 52) {
            key = new String("【" + (rowNum + 1) + "-" + "A" + (char) (cellNum + 39) + "】");
        } else {
            key = new String("【" + (rowNum + 1) + "-" + "B" + (char) (cellNum + 13) + "】");
        }
        return key;
    }


}
