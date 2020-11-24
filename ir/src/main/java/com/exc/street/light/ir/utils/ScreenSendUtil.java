package com.exc.street.light.ir.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.ir.config.parameter.ScreenApi;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.vo.IrMaterialVO;
import com.exc.street.light.resource.vo.IrProgramMaterialVO;
import com.exc.street.light.resource.vo.IrProgramVO;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitlePlayVO;
import com.exc.street.light.resource.vo.req.IrReqSendProgramVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 显示屏发送请求工具类
 *
 * @author Longshuangyang
 * @date 2019/08/15
 */
@Component
public class ScreenSendUtil {
    private static final Logger logger = LoggerFactory.getLogger(ScreenSendUtil.class);

    /**
     * 发送HttpPost请求
     *
     * @param urlString 服务地址
     * @param params    传入参数的json字符串
     * @return 成功:返回json字符串
     */
    public static String jsonPost(String urlString, String params) {
        logger.info("URL:{}", urlString);
        ScreenApi screenApi = new ScreenApi();
        try {
//            String urlString = "http://"+ip+":"+port+"/command/"+sn;
            // 创建连接
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            // 设置请求方式
            connection.setRequestMethod("POST");
            // 设置接收数据的格式
            connection.setRequestProperty("Accept", "application/json");
            // 设置发送数据的格式

            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            // utf-8编码
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.append(params);
            out.flush();
            out.close();
            int code = connection.getResponseCode();
            InputStream is = null;
            if (code == 200) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }
            // 读取响应
            // 获取长度
            int length = (int) connection.getContentLength();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                // utf-8编码
                String result = new String(data, "UTF-8");
                return result;
            }
        } catch (IOException e) {
            //显示屏发送请求失败,请求地址为url
            return "0";
        }
        //返回的数据为空，根据不同的接口判断成功与否
        return "1";
    }

    /**
     * 把base64格式转为文件，用于截图转为文件
     *
     * @param destPath
     * @param base64
     * @param fileName
     */
    public static void base64ToFile(String destPath, String base64, String fileName) {
        File file = null;
        //创建文件目录
        String filePath = destPath;
        File dir = new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取显示屏宽json串
     *
     * @return
     */
    public static String getScreenWidth() {
        String string = "{ \n" +
                "    \"type\": \"callCardService\", \n" +
                "    \"fn\": \"getScreenWidth\"\n" +
                "} ";
        return string;
    }

    /**
     * 获取显示屏高json串
     *
     * @return
     */
    public static String getScreenHeight() {
        String string = "{ \n" +
                "    \"type\": \"callCardService\", \n" +
                "    \"fn\": \"getScreenHeight\" \n" +
                "} ";
        return string;
    }

    /**
     * 获取显示屏亮度json串
     *
     * @return
     */
    public static String getBrightness() {
        String string = "{ \n" +
                "    \"type\": \"callCardService\", \n" +
                "    \"fn\": \"getBrightness\" \n" +
                "} ";
        return string;
    }

    /**
     * 获取显示屏音量json串
     *
     * @return
     */
    public static String getVolume() {
        String string = "{ \n" +
                "    \"type\": \"callCardService\", \n" +
                "    \"fn\": \"getVolume\" \n" +
                "} ";
        return string;
    }

    /**
     * 获取显示屏开关状态json串
     *
     * @return
     */
    public static String isScreenOpen() {
        String string = "{\n" +
                "        \"type\": \"callCardService\",\n" +
                "            \"fn\": \"isScreenOpen\"\n" +
                "    } ";
        return string;
    }

    /**
     * 获取显示屏播放节目json串
     *
     * @return
     */
    public static String getPlayingProgram() {
        String string = "{\n" +
                "        \"type\": \"getPlayingProgram\"\n" +
                "    }";
        return string;
    }

    /**
     * 获取显示屏网络类型json串
     *
     * @return
     */
    public static String getNetworkType() {
        String string = "{\n" +
                "        \"type\": \"callCardService\",\n" +
                "            \"fn\": \"getNetworkType\"\n" +
                "    }";
        return string;
    }

    /**
     * 设置显示屏开关json串
     *
     * @param bool (true：开，false：关)
     * @return
     */
    public static String setScreenOpen(String bool) {
        String string = "{\n" +
                "        \"type\": \"callCardService\",\n" +
                "            \"fn\": \"setScreenOpen\",\n" +
                "            \"arg1\": " + bool + " \n" +
                "    }";
        return string;
    }

    /**
     * 设置显示屏音量json串
     *
     * @param volume （取值范围：0-15）
     * @return
     */
    public static String setVolume(Integer volume) {
        String string = "{ \n" +
                "    \"type\": \"callCardService\", \n" +
                "    \"fn\": \"setVolume\", \n" +
                "    \"arg1\": " + volume + " \n" +
                "} ";
        return string;
    }

    /**
     * 设置显示屏亮度json串
     *
     * @param brightness （取值范围：80-255）
     * @return
     */
    public static String setBrightness(Integer brightness) {
        String string = "{ \n" +
                "    \"type\": \"callCardService\", \n" +
                "    \"fn\": \"setBrightness\", \n" +
                "    \"arg1\": " + brightness + " \n" +
                "} ";
        return string;
    }

    /**
     * 下发节目json串
     *
     * @param servicePath （素材存放地址）
     * @param progressInterface （上传节目进度条接口）
     * @param irReqSendProgramVO （节目定时对象）
     * @param screenDevice （设备对象，用于获取设备宽高）
     * @param irProgramVO （节目对象，用于获取节目名称及节目素材总大小）
     * @param irProgramMaterialVOList （素材节目中间对象列表，用于获取节目相关参数）
     * @param irMaterialVOList （素材对象列表，用于获取素材相关参数）
     * @return
     */
    public static String sendProgram(String servicePath, String progressInterface, IrReqSendProgramVO irReqSendProgramVO,
                                     ScreenDevice screenDevice,
                                     IrProgramVO irProgramVO,
                                     List<IrProgramMaterialVO> irProgramMaterialVOList,
                                     List<IrMaterialVO> irMaterialVOList) {
        String programString = "";
        String md5 = UUID.randomUUID() + "";
        for (int i = 0; i < irProgramMaterialVOList.size(); i++) {
            IrProgramMaterialVO programMaterial = irProgramMaterialVOList.get(i);
            IrMaterialVO irMaterialVO = irMaterialVOList.stream().filter(a -> programMaterial.getMaterialId().equals(a.getId())).collect(Collectors.toList()).get(0);
            if (i > 0) {
                programString = programString + ",\n";
            }
            if ("Video".equals(irMaterialVO.getType())) {
                programString = programString + "{\n" +
                        "\t\t\t\t\t\t\t\t\"_type\": \"Video\",\n" +
                        "\t\t\t\t\t\t\t\t\"_id\": \"" + UUID.randomUUID() + "\",\n" +
                        "\t\t\t\t\t\t\t\t\"id\": \"" + irMaterialVO.getFileName() + "\",\n" +
                        "\t\t\t\t\t\t\t\t\"md5\": \"" + md5 + "\",\n" +
                        "\t\t\t\t\t\t\t\t\"name\": \"" + irMaterialVO.getFileName() + "\",\n" +
                        "\t\t\t\t\t\t\t\t\"size\": \"" + irMaterialVO.getSize() + "\",\n" +
                        "\t\t\t\t\t\t\t\t\"playTime\": " + programMaterial.getPlayTime() + ",\n" +
                        "\t\t\t\t\t\t\t\t\"timeSpan\": " + programMaterial.getTimeSpan() + ",\n" +
                        "\t\t\t\t\t\t\t\t\"left\": 0,\n" +
                        "\t\t\t\t\t\t\t\t\"top\": 0,\n" +
                        "\t\t\t\t\t\t\t\t\"width\": " + screenDevice.getWidth() + ",\n" +
                        "\t\t\t\t\t\t\t\t\"height\": " + screenDevice.getHeight() + ",\n" +
                        "\t\t\t\t\t\t\t\t\"fileExt\": \"" + irMaterialVO.getFileExt() + "\",\n" +
                        "\t\t\t\t\t\t\t\t\"mime\": \"" + irMaterialVO.getMime() + "\"\n" +
                        "\t\t\t\t\t\t\t}";
            }
            if ("Image".equals(irMaterialVO.getType())) {
                programString = programString + "{\n" +
                        "\t\"_type\": \"Image\",\n" +
                        "\t\"_id\": \"" + UUID.randomUUID() + "\",\n" +
                        "\t\"id\": \"" + irMaterialVO.getFileName() + "\",\n" +
                        "\t\"md5\": \"" + md5 + "\",\n" +
                        "\t\"name\": \"" + irMaterialVO.getFileName() + "\",\n" +
                        "\t\"size\": \"" + irMaterialVO.getSize() + "\",\n" +
                        "\t\"playTime\":" + programMaterial.getPlayTime() + ",\n" +
                        "\t\"timeSpan\":" + programMaterial.getTimeSpan() + ",\n" +
                        "\t\"left\":0,\n" +
                        "\t\"top\":0,\n" +
                        "\t\"width\": " + screenDevice.getWidth() + ",\n" +
                        "\t\"height\": " + screenDevice.getHeight() + ",\n" +
                        "\t\"fileExt\": \"" + irMaterialVO.getFileExt() + "\",\n" +
                        "\t\"mime\": \"" + irMaterialVO.getMime() + "\"\n" +
                        "\t}";
            }
            if ("MultiLineText".equals(irMaterialVO.getType())) {
                boolean flg = true;
                if (irMaterialVO.getCenter() != null && irMaterialVO.getCenter() == 0) {
                    flg = false;
                }
                programString = programString + "{\n" +
                        "\t\t\t\t\t\t\t\t\"backgroundColor\": \"" + irMaterialVO.getBackgroundColor() + "\",\n" +
                        "\t\t\t\t\t\t\t\t\"id\": \"" + irMaterialVO.getName() + "\",\n" +
                        "\t\t\t\t\t\t\t\t\"name\": \"" + irMaterialVO.getName() + "\",\n" +
                        "\t\t\t\t\t\t\t\t\"_type\": \"MultiLineText\",\n" +
                        "\t\t\t\t\t\t\t\t\"speed\": " + irMaterialVO.getSpeed() + ",\n" +
                        "\t\t\t\t\t\t\t\t\"lineHeight\": " + irMaterialVO.getLineHeight() + ",\n" +
                        "\t\t\t\t\t\t\t\t\"center\": " + flg + ",\n" +
                        "\t\t\t\t\t\t\t\t\"html\": \"" + irMaterialVO.getHtml() + "\",\n" +
                        "\t\t\t\t\t\t\t\t\"playTime\": " + programMaterial.getPlayTime() + ",\n" +
                        "\t\t\t\t\t\t\t\t\"timeSpan\": " + programMaterial.getTimeSpan() + ",\n" +
                        "\t\t\t\t\t\t\t\t\"left\": 0,\n" +
                        "\t\t\t\t\t\t\t\t\"top\": 0,\n" +
                        "\t\t\t\t\t\t\t\t\"width\": " + screenDevice.getWidth() + ",\n" +
                        "\t\t\t\t\t\t\t\t\"height\": " + screenDevice.getHeight() + "\n" +
                        "\t\t\t\t\t\t\t}";
            }
        }
        String schedules = "";
        Integer[] cycleTypes = irReqSendProgramVO.getCycleTypes();
        String cycleTypeString = "";
        for (int i = 0; i < cycleTypes.length; i++) {
            if (cycleTypes[i] == 0) {
                cycleTypes[i] = 7;
            }
            if (i == 0) {
                cycleTypeString += (cycleTypes[i] - 1);
            } else {
                cycleTypeString += "," + (cycleTypes[i] - 1);
            }
        }
        //1：每天播放，2：周期播放
        if (irReqSendProgramVO.getExecutionMode() == 1) {
            schedules = "{\n" +
                    "\t\t\t\t\t\t\"filterType\": \"None\",\n" +
                    "\t\t\t\t\t\t\"dateType\": \"Range\",\n" +
                    "\t\t\t\t\t\t\"startDate\":\"" + irReqSendProgramVO.getStartDate() + "\",\n" +
                    "\t\t\t\t\t\t\"endDate\":\"" + irReqSendProgramVO.getEndDate() + "\",\n" +
                    "\t\t\t\t\t\t\"timeType\": \"Range\",\n" +
                    "\t\t\t\t\t\t\"startTime\": \"" + irReqSendProgramVO.getExecutionStartTime() + "\",\n" +
                    "\t\t\t\t\t\t\"endTime\": \"" + irReqSendProgramVO.getExecutionEndTime() + "\"\n" +
                    "\t\t\t\t\t}";
        } else {
            schedules = "{\n" +
                    "\t\t\t\t\t\t\"filterType\": \"Week\",\n" +
                    "\t\t\t\t\t\t\"dateType\": \"Range\",\n" +
                    "\t\t\t\t\t\t\"startDate\":\"" + irReqSendProgramVO.getStartDate() + "\",\n" +
                    "\t\t\t\t\t\t\"endDate\":\"" + irReqSendProgramVO.getEndDate() + "\",\n" +
                    "\t\t\t\t\t\t\"timeType\": \"Range\",\n" +
                    "\t\t\t\t\t\t\"startTime\": \"" + irReqSendProgramVO.getExecutionStartTime() + "\",\n" +
                    "\t\t\t\t\t\t\"endTime\": \"" + irReqSendProgramVO.getExecutionEndTime() + "\",\n" +
                    "\t\t\t\t\t\t\"weekFilter\": [" + cycleTypeString + "]\n" +
                    "\t\t\t\t\t}";
        }
        String string = "{\n" +
                "\t\"type\": \"commandXixunPlayer\",\n" +
                "\t\"_id\": \"" + UUID.randomUUID() + "\",\n" +
                "\t\"command\": {\n" +
                "\t\t\"_type\": \"PlayXixunTask\",\n" +
                "\t\t\"id\": \"" + UUID.randomUUID() + "\",\n" +
                "\t\t\"preDownloadURL\": \"" + servicePath + "\",\n" +
                "\t\t\"notificationURL\": \"" + progressInterface + "\",\n" +
                "\t\t\"task\": {\n" +
                "\t\t\t\"_id\": \"" + UUID.randomUUID() + "\",\n" +
                "\t\t\t\"name\": \"" + irProgramVO.getName() + "\",\n" +
                "\t\t\t\"items\": [{\n" +
                "\t\t\t\t\t\"_id\": \"" + UUID.randomUUID() + "\",\n" +
                "\t\t\t\t\t\"_program\": {\n" +
                "\t\t\t\t\t\t\"_id\": \"" + UUID.randomUUID() + "\",\n" +
                "\t\t\t\t\t\t\"totalSize\": \"" + irProgramVO.getTotalSize() + "\",\n" +
                "\t\t\t\t\t\t\"version\": 0,\n" +
                "\t\t\t\t\t\t\"name\": \"" + irProgramVO.getName() + "\",\n" +
                "\t\t\t\t\t\t\"width\": " + screenDevice.getWidth() + ",\n" +
                "\t\t\t\t\t\t\"height\": " + screenDevice.getHeight() + ",\n" +
                "\t\t\t\t\t\t\"layers\": [{\n" +
                "\t\t\t\t\t\t\t\"sources\": [ \n" +
                programString +
                "\t\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\t\"repeat\": true\n" +
                "\t\t\t\t\t\t}]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"repeatTimes\": 1,\n" +
                "\t\t\t\t\t\"schedules\": [" +
                schedules +
                "]\n" +
                "\t\t\t\t}\n" +
                "\n" +
                "\n" +
                "\n" +
                "\t\t\t]\n" +
                "\n" +
                "\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        return string;
    }

    /**
     * 取消播放
     *
     * @return
     */
    public static String deleteProgram() {
        String string = "{ \n" +
                "    \"type\": \"clearPlayerTask\"\n" +
                "} ";
        return string;
    }

    /**
     * 发送字幕json串
     *
     * @param irReqScreenSubtitlePlayVO
     * @return
     */
    public static String sendSubtitle(IrReqScreenSubtitlePlayVO irReqScreenSubtitlePlayVO) {
        String string = "{ \n" +
                "\"type\": \"invokeBuildInJs\", \n" +
                "\"method\": \"scrollMarquee\", \n" +
                "\"num\": " + irReqScreenSubtitlePlayVO.getNum() + " , \t\t\t\t\n" +
                "\"html\": \"" + irReqScreenSubtitlePlayVO.getHtml() + "\", \n" +
                "\"interval\":" + irReqScreenSubtitlePlayVO.getInterval() + ", \t\t\t\n" +
                "\"step\":" + irReqScreenSubtitlePlayVO.getStep() + ", \t\t\t\t\n" +
                "\"direction\": \"" + irReqScreenSubtitlePlayVO.getDirection() + "\",\t \n" +
                "\"align\": \"" + irReqScreenSubtitlePlayVO.getAlign() + "\"\t\t\t\n" +
                "}";
        return string;
    }

    /**
     * 显示屏任务
     *
     * @param ip
     * @param port
     * @param sn
     * @param program
     * @return
     */
    public static Result threadTask(String ip, Integer port, String sn, String program) {
        Result respResult = new Result();
        logger.info("发送：" + "http://" + ip + ":" + port + "/command/" + sn);
        String result = jsonPost("http://" + ip + ":" + port + "/command/" + sn, program);
        logger.info("接收：" + result);
        try {
            JSONObject jsonObject = JSON.parseObject(result);
            jsonObject.put("sn", sn);
            return respResult.success(jsonObject);
        } catch (Exception e) {
            Result failResult = respResult.error(result);
            failResult.setData(sn);
            return failResult;
        }
    }

}
