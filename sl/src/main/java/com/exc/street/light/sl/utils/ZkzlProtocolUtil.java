package com.exc.street.light.sl.utils;

import com.exc.street.light.sl.VO.TimeTableVO;
import com.exc.street.light.sl.config.zkzl.NettyService;
import com.exc.street.light.sl.netty.shuncom.ByteBufAndString;
import com.exc.street.light.sl.netty.shuncom.RequestBean;
import com.exc.street.light.sl.service.LocationControlService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 对接中科智联集中控制器
 * 协议工具类
 *
 * @author LeiJing
 * @date 2020/07/20
 */
@Component
public class ZkzlProtocolUtil {
    private final static Logger logger = LoggerFactory.getLogger(ZkzlProtocolUtil.class);

    private static NettyService nettyService;

    private static RedisUtil redisUtil;

    private static LocationControlService locationControlService;

    @Autowired
    public void setNettyService(NettyService nettyService) {
        this.nettyService = nettyService;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Autowired
    public void setLocationControlService(LocationControlService locationControlService) {
        this.locationControlService = locationControlService;
    }

    /**
     * 处理接收到的中科智联数据
     *
     * @param request
     * @param ctx
     */
    public static void receiveData(ByteBuf request, ChannelHandlerContext ctx) {
        Thread thread = Thread.currentThread();
        logger.info("正在运行的线程ID为{},名称为{}", thread.getId(), thread.getName());
        RequestBean bean = ByteBufAndString.getByteBufLength(request);
        byte[] requestData = bean.getReq();
        //释放资源
        request.release();

        logger.info("------------------进入ZKZL业务处理方法-----------------------");
        logger.info("接收到的数据为:{}", HexUtil.bytesTohex(requestData));
        //设备地址： 04 19 11 00---（集中器 ID： 19040011）
        String concentratorId = ZkzlProtocolUtil.getConcentratorId(requestData);
        nettyService.channelMap.put(concentratorId, ctx);
        //获取APN
        byte ApnByte = requestData[12];
        //获取Fn的数据单元格式，信息类 DT（02 01 = F10）
        String Fn = ZkzlProtocolUtil.getFn(requestData);
        logger.info("集中器 ID：{}， 功能码：{}， Fn：{}", concentratorId, HexUtil.byteToHex(ApnByte), Fn);
        //判断功能码
        if (ApnByte == (byte) 0x00) {
            //确认∕否认（ AFN=00H）,命令执行结果返回
            if (Fn.equals("F1")) {
                //全部确认
                logger.info("命令执行成功");
                //redis添加集中控制器操作成功记录
                addRedisExecuteSuccessRecord(concentratorId);
            }
            if (Fn.equals("F2")) {
                //全部否认
                logger.info("命令执行失败");
                //redis添加集中控制器操作失败记录
                addRedisExecuteFailedRecord(concentratorId, "命令执行失败");
            }
            if (Fn.equals("F3")) {
                //执行多个命令时成功或失败
                //灯具序号
                int lampNo = getLampNo(requestData);
                //错误码
                int errorCode = requestData[20];
                if (lampNo == 0 && errorCode == 0) {
                    logger.info("执行多个命令, 执行失败, 非设备信息出错或其他数据内容错");
                    addRedisExecuteFailedRecord(concentratorId, "执行失败, 非设备信息出错或其他数据内容错");
                }

                switch (errorCode) {
                    case 0:
                        logger.info("执行多个命令, 执行成功");
                        //redis添加集中控制器操作成功记录
                        addRedisExecuteSuccessRecord(concentratorId);
                        break;
                    case 1:
                        logger.info("命令执行失败，" + lampNo + " 数据内容出错");
                        //redis添加集中控制器操作失败记录
                        addRedisExecuteFailedRecord(concentratorId, "命令执行失败，" + lampNo + " 数据内容出错");
                        break;
                    case 2:
                        logger.info("命令执行失败，" + lampNo + " 重复灯具设备ID（灯具唯一编号）");
                        //redis添加集中控制器操作失败记录
                        addRedisExecuteFailedRecord(concentratorId, "命令执行失败，" + lampNo + " 重复设备序号");
                        break;
                    case 3:
                        logger.info("命令执行失败，" + lampNo + " 重复防盗序号");
                        //redis添加集中控制器操作失败记录
                        addRedisExecuteFailedRecord(concentratorId, "命令执行失败，" + lampNo + " 重复防盗序号");
                        break;
                    case 4:
                        logger.info("命令执行失败，" + lampNo + " 重复灯具序号");
                        //redis添加集中控制器操作失败记录
                        addRedisExecuteFailedRecord(concentratorId, "命令执行失败，" + lampNo + " 重复装置序号");
                        break;
                    case 5:
                        logger.info("命令执行失败，" + lampNo + " 透传超时");
                        //redis添加集中控制器操作失败记录
                        addRedisExecuteFailedRecord(concentratorId, "命令执行失败，" + lampNo + " 透传超时");
                        break;
                    case 6:
                        logger.info("命令执行失败，" + lampNo + " 未查询到此设备或信息");
                        //redis添加集中控制器操作失败记录
                        addRedisExecuteFailedRecord(concentratorId, "命令执行失败，" + lampNo + " 未查询到此设备或信息");
                        break;
                    case 7:
                        logger.info("命令执行失败，" + lampNo + " 组号超范围或灯号异常");
                        //redis添加集中控制器操作失败记录
                        addRedisExecuteFailedRecord(concentratorId, "命令执行失败，" + lampNo + " 组号超范围或灯号异常");
                        break;
                    case 8:
                        logger.info("命令执行失败，" + lampNo + " 集中器忙");
                        //redis添加集中控制器操作失败记录
                        addRedisExecuteFailedRecord(concentratorId, "命令执行失败，" + lampNo + " 集中器忙");
                        break;
                }
            }
            //下发命令复位，收到集中控制器上一条命令执行返回结果后才能执行下一个命令
            restoreRedisUseRecord(concentratorId);
        }
        if (ApnByte == (byte) 0x02) {
            //链路接口检测（ AFN=02H）
            if (Fn.equals("F1")) {
                logger.info("接收到 {} 注册包", concentratorId);
                //注册包
                boolean bool = ZkzlProtocolUtil.responseRegisteredConfirm(concentratorId);
                if (bool) {
                    logger.info("返回注册包确认帧成功");
                    //集中控制器上线后将其下所有单灯控制器都改为在线状态
                } else {
                    logger.info("返回注册包确认帧失败");
                }
                //下发命令复位，收到集中控制器上一条命令执行返回结果后才能执行下一个命令
                restoreRedisUseRecord(concentratorId);
                //集中控制器上线后将其下所有单灯控制器都改为在线状态
                locationControlService.findAllAndUpdateStatus(concentratorId, 1);
                addRedisOnlineStatus(concentratorId);
                logger.info("注册包解析并执行后续操作完成");
            }
            if (Fn.equals("F3")) {
                logger.info("接收到 {} 心跳包", concentratorId);
                //心跳包
                boolean bool = ZkzlProtocolUtil.responseHeartbeatConfirm(concentratorId);
                if (bool) {
                    logger.info("返回心跳包确认帧成功");
                } else {
                    logger.info("返回心跳包确认帧失败");
                }

            }
            //接收到集中控制器消息，默认其下所有单灯控制器为在线状态
            addRedisOnlineStatus(concentratorId);
        }
        if (ApnByte == (byte) 0x0E) {
            //灯具故障信息上报（ AFN=0EH）
            if (Fn.equals("F1")) {
                //灯具故障解析并保存
                logger.info("灯具故障解析并保存");
                //获取灯具故障编码集合
                List<Integer> errorCodeList = getErrorCode(Integer.valueOf(requestData[27]));
                if (errorCodeList.size() > 0) {
                    for (int i = 0; i < errorCodeList.size(); i++) {
                        Integer errorCode = errorCodeList.get(i);
                        switch (errorCode) {
                            case 0:
                                logger.info("中科智联集中控制器编号：" + concentratorId + "，灯具故障");
                                //添加告警记录
                                break;
                            case 1:
                                logger.info("中科智联集中控制器编号：" + concentratorId + "，温度故障");
                                //添加告警记录
                                break;
                            case 2:
                                logger.info("中科智联集中控制器编号：" + concentratorId + "，超负荷报警");
                                //添加告警记录
                                break;
                            case 3:
                                logger.info("中科智联集中控制器编号：" + concentratorId + "，功率因数过低");
                                //添加告警记录
                                break;
                            case 4:
                                logger.info("中科智联集中控制器编号：" + concentratorId + "，时钟故障");
                                //添加告警记录
                                break;
                            case 5:
                                logger.info("中科智联集中控制器编号：" + concentratorId + "，未用");
                                //添加告警记录
                                break;
                            case 6:
                                logger.info("中科智联集中控制器编号：" + concentratorId + "，灯珠故障");
                                //添加告警记录
                                break;
                            case 7:
                                logger.info("中科智联集中控制器编号：" + concentratorId + "，电源故障");
                                //添加告警记录
                                break;
                        }
                    }
                }
            }
        }

        logger.info("中科智联业务处理完毕");
    }

    /**
     * 注册包确认帧
     *
     * @param concentratorId 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     * @return
     */
    public static boolean responseRegisteredConfirm(String concentratorId) {
        String confirm = "68 32 00 32 00 68 0B 04 19 11 00 00 00 61 00 00 01 00 9B 16";
        confirm = confirm.replace(" ", "");
        byte[] bytes = HexUtil.hexStringToBytes(confirm);
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[7] = concentratorIdBytes[1];
        bytes[8] = concentratorIdBytes[0];
        bytes[9] = concentratorIdBytes[3];
        bytes[10] = concentratorIdBytes[2];
        //校验和
        bytes[bytes.length - 2] = getChecksum(bytes);
        logger.info("注册包确认帧：{}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = sendData(bytes, false);

        return bool;
    }

    /**
     * 心跳包确认帧
     *
     * @param concentratorId 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     * @return
     */
    public static boolean responseHeartbeatConfirm(String concentratorId) {
        String confirm = "68 32 00 32 00 68 0B 04 19 11 00 04 00 62 00 00 01 00 A0 16";
        confirm = confirm.replace(" ", "");
        byte[] bytes = HexUtil.hexStringToBytes(confirm);
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[7] = concentratorIdBytes[1];
        bytes[8] = concentratorIdBytes[0];
        bytes[9] = concentratorIdBytes[3];
        bytes[10] = concentratorIdBytes[2];
        //校验和
        bytes[bytes.length - 2] = getChecksum(bytes);
        logger.info("心跳包确认帧：{}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = false;
        try {
            ChannelHandlerContext channel = (ChannelHandlerContext) nettyService.channelMap.get(concentratorId);
            if (channel != null) {
                logger.info("向集中控制器 {} 发送心跳包确认帧:{}", concentratorId, HexUtil.bytesTohex(bytes));
                channel.writeAndFlush(bytes);
                logger.info("向集中控制器 {} 发送心跳包确认帧成功", concentratorId);
                bool = true;
            } else {
                logger.info("向集中控制器 {} 发送心跳包确认帧失败", concentratorId);
                bool = false;
            }
        } catch (Exception e) {
            logger.info("向集中控制器 {} 发送心跳包确认帧失败", concentratorId);
            logger.error(e.getMessage());
            bool = false;
        }

        return bool;
    }

    /**
     * 灯具安装, 一次性最多安装18个灯具
     *
     * @param concentratorId 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     * @param installNum     配置数量： 01 00---配置一个灯控器
     * @param lampNoList     灯装置序号集合： 37 00---十六进制数 0x0037， 装置序号为最小为 55，依次累加
     * @param addOrDelete    灯具安装或者删除，1 安装，0 删除。 测试点号： 01 00---0x0001 安装灯具， 0x0000 删除灯具
     * @param lampAdressList 灯具地址集合： 34 88 50 00 00 00---（灯控器 ID： 000000508834）
     * @param groupNo        所属组： 01---安装至 1 组
     * @return
     */
    public static boolean installLamp(String concentratorId, Integer installNum, List<Integer> lampNoList, int addOrDelete, List<String> lampAdressList, int groupNo) {
        byte[] bytes = new byte[38 + 12 * installNum];
        int index = 0;
        //命令开头
        String startCode = "68 AA 00 AA 00 68 04";
        startCode = startCode.replace(" ", "");
        byte[] startCodeBytes = HexUtil.hexStringToBytes(startCode);
        for (int i = 0; i < startCodeBytes.length; i++) {
            bytes[index++] = startCodeBytes[i];
        }
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[index++] = concentratorIdBytes[1];
        bytes[index++] = concentratorIdBytes[0];
        bytes[index++] = concentratorIdBytes[3];
        bytes[index++] = concentratorIdBytes[2];
        //固定内容命令
        String connectCode1 = "02 A4 70 00 00 02 01";
        connectCode1 = connectCode1.replace(" ", "");
        byte[] connectCodeBytes1 = HexUtil.hexStringToBytes(connectCode1);
        for (int i = 0; i < connectCodeBytes1.length; i++) {
            bytes[index++] = connectCodeBytes1[i];
        }
        //灯具数量
        if (installNum < 16) {
            bytes[index++] = (byte) installNum.intValue();
            bytes[index++] = 0x00;
        } else {
            byte[] installNumBytes = HexUtil.hexStringToBytes(Integer.toHexString(installNum));
            if (installNumBytes.length > 1) {
                bytes[index++] = installNumBytes[0];
                bytes[index++] = installNumBytes[1];
            } else {
                bytes[index++] = installNumBytes[0];
                bytes[index++] = 0x00;
            }
        }
        //需要安装的灯具序号
        for (int i = 0; i < lampNoList.size(); i++) {
            bytes[index++] = (byte) (lampNoList.get(i) & 0xFF);
            bytes[index++] = (byte) (lampNoList.get(i) >> 8 & 0xFF);
            //测试点号： 01 00---0x0001 安装灯具， 0x0000 删除灯具
            bytes[index++] = (byte) addOrDelete;
            bytes[index++] = 0x00;
            //灯具地址： 34 88 50 00 00 00---（灯控器 ID： 000000508834）
            byte[] lampAdressBytes = HexUtil.hexStringToBytes(lampAdressList.get(i));
            for (int j = lampAdressBytes.length - 1; j >= 0; j--) {
                bytes[index++] = lampAdressBytes[j];
            }
            //工作方式： 0---时间表
            bytes[index++] = 0x00;
            //所属组： 01---安装至 1 组
            bytes[index++] = (byte) groupNo;
        }
        //固定内容命令
        String connectCode2 = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        connectCode2 = connectCode2.replace(" ", "");
        byte[] connectCodeBytes2 = HexUtil.hexStringToBytes(connectCode2);
        for (int i = 0; i < connectCodeBytes2.length; i++) {
            bytes[index++] = connectCodeBytes2[i];
        }
        //校验和, 帧校验和是用户数据区所有字节的八位位组算术和，不考虑溢出位。用户数据区包括控制域、地址域、链路用户数据（应用层）三部分。
        //字节数组前7位、后两位不进行算术和校验
        bytes[index++] = getChecksum(bytes);
        //计算长度 L，固定长度的报文头
        byte[] lenghtBystes = getLenghtBytes(index - 1 - 6);
        bytes[1] = lenghtBystes[0];
        bytes[2] = lenghtBystes[1];
        bytes[3] = lenghtBystes[0];
        bytes[4] = lenghtBystes[1];
        //结束字符（16H）
        bytes[index] = 0x16;
        logger.info("灯具安装命令 = {}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean sendBool = sendData(bytes, true);
        //获取安装灯具返回结果
        if (sendBool) {
            try {
                for (int i = 0; i < 15; i++) {
                    Thread.sleep(200);
                    Object object = redisUtil.get(concentratorId + "Execute");
                    logger.info("size = " + i);
                    if (object != null) {
                        if (object.toString().equals("success")) {
                            if (addOrDelete == 1) {
                                logger.info("安装灯具成功");
                                for (i = 0; i < lampAdressList.size(); i++) {
                                    locationControlService.updateStatusByNum(lampAdressList.get(i), 1);
                                    logger.info("修改灯具 {} 为在线状态", lampAdressList.get(i));
                                }
                            } else {
                                logger.info("删除灯具成功");
                            }
                            return true;
                        }
                        if (object.toString().equals("failed")) {
                            if (addOrDelete == 1) {
                                logger.info("安装灯具失败");
                            } else {
                                logger.info("删除灯具失败");
                            }
                            return false;
                        }
                    }
                }
                if (addOrDelete == 1) {
                    logger.info("安装灯具失败");
                } else {
                    logger.info("删除灯具失败");
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                if (addOrDelete == 1) {
                    logger.info("安装灯具失败");
                } else {
                    logger.info("删除灯具失败");
                }
                return false;
            }
        } else {
            logger.info("灯具安装命令下发失败");
            return false;
        }
    }

    /**
     * 按组删除灯具，可一次性删除所有分组
     *
     * @param concentratorId 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     * @param groupNoList    删除灯具组的集合： 删除集中控制器下某一组的灯具
     * @return
     */
    public static boolean removeLampByGroup(String concentratorId, List<Integer> groupNoList) {
        byte[] bytes = new byte[37 + 4 * groupNoList.size()];
        int index = 0;
        //命令开头
        String startCode = "68 96 01 96 01 68 04 20 20 04 40 02 A4 79 00 00 04 01";
        startCode = startCode.replace(" ", "");
        byte[] startCodeBytes = HexUtil.hexStringToBytes(startCode);
        for (int i = 0; i < startCodeBytes.length; i++) {
            bytes[index++] = startCodeBytes[i];
        }
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[7] = concentratorIdBytes[1];
        bytes[8] = concentratorIdBytes[0];
        bytes[9] = concentratorIdBytes[3];
        bytes[10] = concentratorIdBytes[2];
        //删除灯具的组号
        for (int i = 0; i < groupNoList.size(); i++) {
            bytes[index++] = (byte) groupNoList.get(i).intValue();
            bytes[index++] = 0x00;
            bytes[index++] = 0x00;
            bytes[index++] = 0x00;
        }
        //固定内容命令
        String connectCode2 = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        connectCode2 = connectCode2.replace(" ", "");
        byte[] connectCodeBytes2 = HexUtil.hexStringToBytes(connectCode2);
        for (int i = 0; i < connectCodeBytes2.length; i++) {
            bytes[index++] = connectCodeBytes2[i];
        }
        //校验和, 帧校验和是用户数据区所有字节的八位位组算术和，不考虑溢出位。用户数据区包括控制域、地址域、链路用户数据（应用层）三部分。
        //字节数组前7位、后两位不进行算术和校验
        bytes[index++] = getChecksum(bytes);
        //计算长度 L，固定长度的报文头
        byte[] lenghtBystes = getLenghtBytes(index - 1 - 6);
        bytes[1] = lenghtBystes[0];
        bytes[2] = lenghtBystes[1];
        bytes[3] = lenghtBystes[0];
        bytes[4] = lenghtBystes[1];
        //结束字符（16H）
        bytes[index] = 0x16;
        logger.info("按组删除灯具命令 = {}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean sendBool = sendData(bytes, true);
        //获取按组删除灯具返回结果
        if (sendBool) {
            try {
                for (int i = 0; i < 20; i++) {
                    Thread.sleep(500);
                    Object object = redisUtil.get(concentratorId + "Execute");
                    logger.info("size = " + i);
                    if (object != null) {
                        if (object.toString().equals("success")) {
                            logger.info("按组删除灯具成功");
                            return true;
                        }
                        if (object.toString().equals("failed")) {
                            logger.info("按组删除灯具失败");
                            return false;
                        }
                    }
                }
                logger.info("按组删除灯具失败");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("按组删除灯具失败");
                return false;
            }
        } else {
            logger.info("按组删除灯具命令下发失败");
            return false;
        }
    }

    /**
     * 删除集中控制器下所有灯具
     *
     * @param concentratorId 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     * @return
     */
    public static boolean removeAllLamp(String concentratorId) {
        String confirm = "68 72 00 72 00 68 04 20 20 04 40 02 A4 76 00 00 08 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 AD 16";
        confirm = confirm.replace(" ", "");
        byte[] bytes = HexUtil.hexStringToBytes(confirm);
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[7] = concentratorIdBytes[1];
        bytes[8] = concentratorIdBytes[0];
        bytes[9] = concentratorIdBytes[3];
        bytes[10] = concentratorIdBytes[2];
        //校验和
        bytes[bytes.length - 2] = getChecksum(bytes);
        logger.info("删除集中控制器下所有灯具命令 = {}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = sendData(bytes, true);

        return bool;
    }

    /**
     * 单个灯具立即调光
     *
     * @param concentratorId 设备地址： 04 19 11 00 ---（集中器 ID： 19040011）
     * @param lampNo         灯装置序号： 37 00---十六进制数 0x0037， 装置序号为最小为 55，依次累加
     * @param value          调光值： 64---十六进制数，范围 0x00-0x64
     * @return
     */
    public static boolean oneLampControl(String concentratorId, int lampNo, int value) {
        byte[] bytes = new byte[39];
        int index = 0;
        //命令开头
        String startCode = "68 7E 00 7E 00 68 04";
        startCode = startCode.replace(" ", "");
        byte[] startCodeBytes = HexUtil.hexStringToBytes(startCode);
        for (int i = 0; i < startCodeBytes.length; i++) {
            bytes[index++] = startCodeBytes[i];
        }
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[index++] = concentratorIdBytes[1];
        bytes[index++] = concentratorIdBytes[0];
        bytes[index++] = concentratorIdBytes[3];
        bytes[index++] = concentratorIdBytes[2];

        //固定内容命令
        String connectCode1 = "02 A5 76 00 00 01 03";
        connectCode1 = connectCode1.replace(" ", "");
        byte[] connectCodeBytes1 = HexUtil.hexStringToBytes(connectCode1);
        for (int i = 0; i < connectCodeBytes1.length; i++) {
            bytes[index++] = connectCodeBytes1[i];
        }
        //控制的灯装置序号： 37 00---装置序号为 55
        bytes[index++] = (byte) (lampNo & 0xFF);
        bytes[index++] = (byte) (lampNo >> 8 & 0xFF);
        //调光值，亮度： 64---十六进制数，范围 0x00-0x64
        bytes[index++] = (byte) value;
        //固定内容命令
        String connectCode2 = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        connectCode2 = connectCode2.replace(" ", "");
        byte[] connectCodeBytes2 = HexUtil.hexStringToBytes(connectCode2);
        for (int i = 0; i < connectCodeBytes2.length; i++) {
            bytes[index++] = connectCodeBytes2[i];
        }
        //校验和, 帧校验和是用户数据区所有字节的八位位组算术和，不考虑溢出位。用户数据区包括控制域、地址域、链路用户数据（应用层）三部分。
        //字节数组前7位、后两位不进行算术和校验
        bytes[index++] = getChecksum(bytes);
        //结束字符（16H）
        bytes[index] = 0x16;
        logger.info("单个灯具立即调光命令:{}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean sendBool = sendData(bytes, true);
        //获取单个灯具调光返回结果
        if (sendBool) {
            try {
                for (int i = 0; i < 15; i++) {
                    Thread.sleep(200);
                    Object object = redisUtil.get(concentratorId + "Execute");
                    if (object != null) {
                        if (object.toString().equals("success")) {
                            logger.info("单个灯具立即调光成功");
                            return true;
                        }
                        if (object.toString().equals("failed")) {
                            logger.info("单个灯具立即调光失败");
                            return false;
                        }
                    }
                }
                logger.info("单个灯具立即调光失败");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("单个灯具立即调光失败");
                return false;
            }
        } else {
            logger.info("单个灯具立即调光命令下发失败");
            return false;
        }
    }

    /**
     * 按组立即调光，可一次性控制所有分组
     *
     * @param concentratorId 设备地址： 04 19 11 00 ---（集中器 ID： 19040011）
     * @param groupNum       控制的组数量
     * @param groupNoList    控制的组号集合
     * @param valueList      控制的调光值集合
     * @return
     */
    public static boolean lampGroupControl(String concentratorId, int groupNum, List<Integer> groupNoList, List<Integer> valueList) {
        byte[] bytes = new byte[37 + 2 * groupNum];
        int index = 0;
        //命令开头
        String startCode = "68 7E 00 7E 00 68 04";
        startCode = startCode.replace(" ", "");
        byte[] startCodeBytes = HexUtil.hexStringToBytes(startCode);
        for (int i = 0; i < startCodeBytes.length; i++) {
            bytes[index++] = startCodeBytes[i];
        }
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[index++] = concentratorIdBytes[1];
        bytes[index++] = concentratorIdBytes[0];
        bytes[index++] = concentratorIdBytes[3];
        bytes[index++] = concentratorIdBytes[2];
        //固定内容命令
        String connectCode1 = "02 A5 77 00 00 02 03";
        connectCode1 = connectCode1.replace(" ", "");
        byte[] connectCodeBytes1 = HexUtil.hexStringToBytes(connectCode1);
        for (int i = 0; i < connectCodeBytes1.length; i++) {
            bytes[index++] = connectCodeBytes1[i];
        }
        //控制组数量
        bytes[index++] = (byte) groupNum;
        //控制的组号
        for (int i = 0; i < groupNoList.size(); i++) {
            //控制的组号
            bytes[index++] = (byte) groupNoList.get(i).intValue();
            //调光值
            bytes[index++] = (byte) valueList.get(i).intValue();
        }
        //固定内容命令
        String connectCode2 = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        connectCode2 = connectCode2.replace(" ", "");
        byte[] connectCodeBytes2 = HexUtil.hexStringToBytes(connectCode2);
        for (int i = 0; i < connectCodeBytes2.length; i++) {
            bytes[index++] = connectCodeBytes2[i];
        }
        //校验和, 帧校验和是用户数据区所有字节的八位位组算术和，不考虑溢出位。用户数据区包括控制域、地址域、链路用户数据（应用层）三部分。
        //字节数组前7位、后两位不进行算术和校验
        bytes[index++] = getChecksum(bytes);
        //计算长度 L，固定长度的报文头
        byte[] lenghtBystes = getLenghtBytes(index - 1 - 6);
        bytes[1] = lenghtBystes[0];
        bytes[2] = lenghtBystes[1];
        bytes[3] = lenghtBystes[0];
        bytes[4] = lenghtBystes[1];
        //结束字符（16H）
        bytes[index] = 0x16;
        logger.info("按组立即调光命令 = {}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = sendData(bytes, true);

        return bool;
    }

    /**
     * 按组下发时间表，一次最多下发9个分组，发送命令超过255个字节会报错
     *
     * @param concentratorId 设备地址： 04 19 11 00 ---（集中器 ID： 19040011）
     * @param issuedNum      下发时间表的组数量
     * @param groupNoList    下发时间表的组号集合
     * @param timeTableVO    下发时间表集合
     * @return
     */
    public static boolean timeTableIssued(String concentratorId, int issuedNum, List<Integer> groupNoList, TimeTableVO timeTableVO) {
        byte[] bytes = new byte[37 + 19 * issuedNum];
        int index = 0;
        //命令开头
        String startCode = "68 C2 00 C2 00 68 04";
        startCode = startCode.replace(" ", "");
        byte[] startCodeBytes = HexUtil.hexStringToBytes(startCode);
        for (int i = 0; i < startCodeBytes.length; i++) {
            bytes[index++] = startCodeBytes[i];
        }
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[index++] = concentratorIdBytes[1];
        bytes[index++] = concentratorIdBytes[0];
        bytes[index++] = concentratorIdBytes[3];
        bytes[index++] = concentratorIdBytes[2];
        //固定内容命令
        String connectCode1 = "02 A4 77 00 00 40 01";
        connectCode1 = connectCode1.replace(" ", "");
        byte[] connectCodeBytes1 = HexUtil.hexStringToBytes(connectCode1);
        for (int i = 0; i < connectCodeBytes1.length; i++) {
            bytes[index++] = connectCodeBytes1[i];
        }
        //下发时间表的组数量
        bytes[index++] = (byte) issuedNum;
        //下发时间表的组号
        List<String> timeList = timeTableVO.getTimeList();
        List<Integer> valueList = timeTableVO.getValueList();
        for (int i = 0; i < groupNoList.size(); i++) {
            //下发时间表的组号
            bytes[index++] = (byte) groupNoList.get(i).intValue();
            for (int j = 0; j < timeList.size(); j++) {
                //下发时间表的时间
                Integer timeHH = Integer.valueOf(timeList.get(j).substring(0, 2));
                Integer timeMM = Integer.valueOf(timeList.get(j).substring(2, 4));
                bytes[index++] = HexUtil.getHex(timeMM);
                bytes[index++] = HexUtil.getHex(timeHH);
                //下发时间表的调光值
                bytes[index++] = (byte) valueList.get(j).intValue();
            }
        }
        //固定内容命令
        String connectCode2 = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        connectCode2 = connectCode2.replace(" ", "");
        byte[] connectCodeBytes2 = HexUtil.hexStringToBytes(connectCode2);
        for (int i = 0; i < connectCodeBytes2.length; i++) {
            bytes[index++] = connectCodeBytes2[i];
        }
        //校验和, 帧校验和是用户数据区所有字节的八位位组算术和，不考虑溢出位。用户数据区包括控制域、地址域、链路用户数据（应用层）三部分。
        //字节数组前7位、后两位不进行算术和校验
        bytes[index++] = getChecksum(bytes);
        //计算长度 L，固定长度的报文头
        byte[] lenghtBystes = getLenghtBytes(index - 1 - 6);
        bytes[1] = lenghtBystes[0];
        bytes[2] = lenghtBystes[1];
        bytes[3] = lenghtBystes[0];
        bytes[4] = lenghtBystes[1];
        //结束字符（16H）
        bytes[index] = 0x16;
        logger.info("时间表下发命令 = {}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = sendData(bytes, true);
        //下发恢复时间控制命令
        try {
            for (int i = 0; i < 15; i++) {
                Thread.sleep(200);
                Object object = redisUtil.get(concentratorId + "Execute");
                if (object != null) {
                    if (object.toString().equals("success")) {
                        logger.info("时间表下发成功");
                        //恢复时间控制
                        boolean restoreTimeTableControlBool = restoreTimeTableControl(concentratorId);

                        return restoreTimeTableControlBool;
                    }
                    if (object.toString().equals("failed")) {
                        logger.info("时间表下发失败");
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 按组查询时间表，可一次性查询所有分组
     *
     * @param concentratorId 设备地址： 04 19 11 00 ---（集中器 ID： 19040011）
     * @param queryNum       查询时间表的组数量
     * @param groupNoList    查询时间表的组号集合
     * @return
     */
    public static boolean timeTableQuery(String concentratorId, int queryNum, List<Integer> groupNoList) {
        byte[] bytes = new byte[21 + queryNum];
        int index = 0;
        //命令开头
        String startCode = "68 3A 00 3A 00 68 04";
        startCode = startCode.replace(" ", "");
        byte[] startCodeBytes = HexUtil.hexStringToBytes(startCode);
        for (int i = 0; i < startCodeBytes.length; i++) {
            bytes[index++] = startCodeBytes[i];
        }
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[index++] = concentratorIdBytes[1];
        bytes[index++] = concentratorIdBytes[0];
        bytes[index++] = concentratorIdBytes[3];
        bytes[index++] = concentratorIdBytes[2];
        //固定内容命令
        String connectCode1 = "02 AA 71 00 00 01 04";
        connectCode1 = connectCode1.replace(" ", "");
        byte[] connectCodeBytes1 = HexUtil.hexStringToBytes(connectCode1);
        for (int i = 0; i < connectCodeBytes1.length; i++) {
            bytes[index++] = connectCodeBytes1[i];
        }
        //查询时间表的组数量
        bytes[index++] = (byte) queryNum;
        //查询时间表的组号
        for (int i = 0; i < groupNoList.size(); i++) {
            //查询时间表的组号
            bytes[index++] = (byte) groupNoList.get(i).intValue();
        }
        //校验和, 帧校验和是用户数据区所有字节的八位位组算术和，不考虑溢出位。用户数据区包括控制域、地址域、链路用户数据（应用层）三部分。
        //字节数组前7位、后两位不进行算术和校验
        bytes[index++] = getChecksum(bytes);
        //计算长度 L，固定长度的报文头
        byte[] lenghtBystes = getLenghtBytes(index - 1 - 6);
        bytes[1] = lenghtBystes[0];
        bytes[2] = lenghtBystes[1];
        bytes[3] = lenghtBystes[0];
        bytes[4] = lenghtBystes[1];
        //结束字符（16H）
        bytes[index] = 0x16;
        logger.info("时间表查询命令 = {}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = sendData(bytes, true);

        return bool;
    }

    /**
     * 恢复时间控制
     *
     * @param concentratorId 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     * @return
     */
    public static boolean restoreTimeTableControl(String concentratorId) {
        String confirm = "68 72 00 72 00 68 04 04 19 11 00 02 A5 7D 00 00 80 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 D7 16";
        confirm = confirm.replace(" ", "");
        byte[] bytes = HexUtil.hexStringToBytes(confirm);
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[7] = concentratorIdBytes[1];
        bytes[8] = concentratorIdBytes[0];
        bytes[9] = concentratorIdBytes[3];
        bytes[10] = concentratorIdBytes[2];
        //校验和
        bytes[bytes.length - 2] = getChecksum(bytes);
        logger.info("恢复时间控制命令：{}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = sendData(bytes, true);

        return bool;
    }

    /**
     * 设置拨号IP和端口
     *
     * @param concentratorId 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     * @param IP             IP 地址： 77 7B AC 1F---119.123.172.031
     * @param port           端口： E0 0B---3040
     * @return
     */
    public static boolean ipAndPortConfig(String concentratorId, String IP, Integer port) {
        String confirm = "68 16 01 16 01 68 04 20 20 04 40 02 A4 7C 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 DA 11 11 E2 15 27 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C5 16";
        confirm = confirm.replace(" ", "");
        byte[] bytes = HexUtil.hexStringToBytes(confirm);
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[7] = concentratorIdBytes[1];
        bytes[8] = concentratorIdBytes[0];
        bytes[9] = concentratorIdBytes[3];
        bytes[10] = concentratorIdBytes[2];
        //IP
        String[] ipStrings = IP.split("\\.");
        bytes[30] = HexUtil.getByte(Integer.parseInt(ipStrings[0]));
        bytes[31] = HexUtil.getByte(Integer.parseInt(ipStrings[1]));
        bytes[32] = HexUtil.getByte(Integer.parseInt(ipStrings[2]));
        bytes[33] = HexUtil.getByte(Integer.parseInt(ipStrings[3]));
        //端口
        bytes[34] = (byte) (port & 0xFF);
        bytes[35] = (byte) (port >> 8 & 0xFF);
        //校验和
        bytes[bytes.length - 2] = getChecksum(bytes);
        logger.info("设置拨号IP和端口命令：{}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = sendData(bytes, true);

        return bool;
    }

    /**
     * 查询单个灯具的状态信息
     *
     * @param concentratorId 设备地址04 19 11 00---（集中器 ID： 19040011）
     * @param lampAdress     灯控器 ID 号：32 41 26 00 00 00---（ID： 000000264132）
     * @return
     */
    public static boolean queryOnelampStatus(String concentratorId, String lampAdress) {
        String confirm = "68 5E 00 5E 00 68 4B 02 19 59 00 02 10 79 00 00 08 0C 01 09 06 25 00 00 00 01 02 01 CF 66 16";
        confirm = confirm.replace(" ", "");
        byte[] bytes = HexUtil.hexStringToBytes(confirm);
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[7] = concentratorIdBytes[1];
        bytes[8] = concentratorIdBytes[0];
        bytes[9] = concentratorIdBytes[3];
        bytes[10] = concentratorIdBytes[2];
        //灯具地址： 09 06 25 00 00 00---（灯控器 ID： 000000250609）
        byte[] lampAdressBytes = HexUtil.hexStringToBytes(lampAdress);
        int index = 19;
        for (int i = lampAdressBytes.length - 1; i >= 0; i--) {
            bytes[index++] = lampAdressBytes[i];
        }
        //校验和
        bytes[bytes.length - 2] = getChecksum(bytes);
        logger.info("查询单个灯具的状态信息命令：{}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = sendData(bytes, true);

        return bool;
    }

    /**
     * 恢复中科智联默认的主站域名, zk-link.f3322.net,端口2020
     *
     * @param concentratorId 设备地址04 19 11 00---（集中器 ID： 19040011）
     * @return
     */
    public static boolean restoreDefaultDomainName(String concentratorId) {
        String confirm = "68 5A 01 5A 01 68 04 20 20 04 40 02 A4 79 00 00 01 00 00 00 00 00 E4 07 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 11 7A 6B 2D 6C 69 6E 6B 2E 66 33 33 32 32 2E 6E 65 74 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 37 16";
        confirm = confirm.replace(" ", "");
        byte[] bytes = HexUtil.hexStringToBytes(confirm);
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[7] = concentratorIdBytes[1];
        bytes[8] = concentratorIdBytes[0];
        bytes[9] = concentratorIdBytes[3];
        bytes[10] = concentratorIdBytes[2];
        //校验和
        bytes[bytes.length - 2] = getChecksum(bytes);
        logger.info("查询单个灯具的状态信息命令：{}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = sendData(bytes, true);

        return bool;
    }

    /**
     * 设置主站域名，端口
     *
     * @param concentratorId 设备地址04 19 11 00---（集中器 ID： 19040011）
     * @return
     */
    public static boolean domainNameConfig(String concentratorId, String domainName, int port) {
        String confirm = "68 5A 01 5A 01 68 04 20 20 04 40 02 A4 79 00 00 01 00 00 00 00 00 E4 07 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 11 7A 6B 2D 6C 69 6E 6B 2E 66 33 33 32 32 2E 6E 65 74 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 37 16";
        confirm = confirm.replace(" ", "");

        byte[] bytes = new byte[77 + domainName.length()];
        int index = 0;
        //命令开头
        String startCode = "68 5A 01 5A 01 68 04 20 20 04 40 02 A4 79 00 00 01 00 00 00 00 00 E4 07 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        startCode = startCode.replace(" ", "");
        byte[] startCodeBytes = HexUtil.hexStringToBytes(startCode);
        for (int i = 0; i < startCodeBytes.length; i++) {
            bytes[index++] = startCodeBytes[i];
        }
        //添加设备地址（集中器ID）
        byte[] concentratorIdBytes = HexUtil.hexStringToBytes(concentratorId);
        //设备地址排列顺序，设备地址： 04 19 11 00---（集中器 ID： 19040011）
        bytes[7] = concentratorIdBytes[1];
        bytes[8] = concentratorIdBytes[0];
        bytes[9] = concentratorIdBytes[3];
        bytes[10] = concentratorIdBytes[2];
        //添加端口
        bytes[22] = (byte) (port & 0xFF);
        bytes[23] = (byte) (port >> 8 & 0xFF);
        //添加域名长度
        bytes[index++] = (byte) domainName.length();
        //添加域名
        char[] domainNameChars = domainName.toCharArray();
        for (int i = 0; i < domainName.length(); i++) {
            bytes[index++] = (byte) domainNameChars[i];
        }
        //固定内容命令
        String connectCode1 = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        connectCode1 = connectCode1.replace(" ", "");
        byte[] connectCodeBytes1 = HexUtil.hexStringToBytes(connectCode1);
        for (int i = 0; i < connectCodeBytes1.length; i++) {
            bytes[index++] = connectCodeBytes1[i];
        }
        //校验和, 帧校验和是用户数据区所有字节的八位位组算术和，不考虑溢出位。用户数据区包括控制域、地址域、链路用户数据（应用层）三部分。
        //字节数组前7位、后两位不进行算术和校验
        bytes[index++] = getChecksum(bytes);
        //计算长度 L，固定长度的报文头
        byte[] lenghtBystes = getLenghtBytes(index - 1 - 6);
        bytes[1] = lenghtBystes[0];
        bytes[2] = lenghtBystes[1];
        bytes[3] = lenghtBystes[0];
        bytes[4] = lenghtBystes[1];
        //结束字符（16H）
        bytes[index] = 0x16;
        logger.info("设置固定域名IP命令：{}", HexUtil.bytesTohex(bytes));
        //下发命令
        boolean bool = sendData(bytes, true);

        return bool;
    }

    /**
     * 计算校验和。
     * 校验和, 帧校验和是用户数据区所有字节的八位位组算术和，不考虑溢出位。用户数据区包括控制域、地址域、链路用户数据（应用层）三部分。
     * 字节数组前7位、后两位不进行算术和校验。
     *
     * @param bytes
     * @return
     */
    public static byte getChecksum(byte[] bytes) {
        byte[] checksumBytes = new byte[bytes.length - 8];
        System.arraycopy(bytes, 6, checksumBytes, 0, bytes.length - 8);

        int sum = 0;
        byte checksum;
        for (int i = 0; i < checksumBytes.length; i++) {
            sum += new Byte(checksumBytes[i]).intValue();
        }
        checksum = (byte) (sum % 256);

        return checksum;
    }

    /**
     * 获取设备地址
     * 设备地址： 04 19 11 00---（集中器 ID： 19040011）
     */
    public static String getConcentratorId(byte[] protocol) {
        String concentratorId = HexUtil.byteToHex(protocol[8]) + HexUtil.byteToHex(protocol[7]) + HexUtil.byteToHex(protocol[10]) + HexUtil.byteToHex(protocol[9]);

        return concentratorId;
    }

    /**
     * 获取Fn的数据单元格式，信息类 DT（02 01 = F10）
     */
    public static String getFn(byte[] protocol) {
        byte DT1 = protocol[16];
        byte DT2 = protocol[17];
        int n = HexUtil.byteToInt(DT2) * 8;
        for (int i = 0; i < 8; i++) {
            if ((DT1 & (1 << i)) != 0) {
                n = n + i + 1;
            }
        }

        return "F" + n;
    }

    /**
     * 获取APN = 00，Fn = F3灯具 返回数据的灯具序号
     */
    public static int getLampNo(byte[] protocol) {
        byte lampNoHigh = protocol[18];
        byte lampNoLow = protocol[19];

        return ((lampNoLow & 0x00ff) << 8) | (lampNoHigh & 0x00ff) & 0xffff;
    }

    /**
     * 获取长度L，固定长度的报文头
     *
     * @param contentLenght
     * @return
     */
    public static byte[] getLenghtBytes(int contentLenght) {
        String lenghtUnicodeStr = Integer.toBinaryString(contentLenght) + "10";
        int lenght = HexUtil.binaryIntToDecimalism(lenghtUnicodeStr);
        byte[] lenghtBytes = new byte[2];
        lenghtBytes[0] = (byte) (lenght & 0xFF);
        lenghtBytes[1] = (byte) (lenght >> 8 & 0xFF);
        return lenghtBytes;
    }

    /**
     * 获取灯具故障编码集合
     *
     * @param errorCodeByte
     * @return
     */
    public static List<Integer> getErrorCode(Integer errorCodeByte) {
        List<Integer> errorCodeList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if ((errorCodeByte & (1 << i)) != 0) {
                errorCodeList.add(i);
            }
        }
        return errorCodeList;
    }

    /**
     * 向集中控制器发送命令
     *
     * @param bytes
     * @param bool  是否占用
     * @return
     */
    public static boolean sendData(byte[] bytes, boolean bool) {
        logger.info("------------------ZKZL发送数据-----------------------");
        String concentratorId = ZkzlProtocolUtil.getConcentratorId(bytes);
        try {
            //集中控制器是否占用，占用中发送失败，未占用则发送
            Object concentrator = redisUtil.get(concentratorId + "Use");
            if (concentrator != null) {
                Integer use = Integer.valueOf(concentrator.toString());
                if (use == 1) {
                    logger.info("集中控制器 " + concentratorId + " 使用中请稍后再发送命令");
                    addRedisExecuteFailedRecord(concentratorId, "集中控制器 " + concentratorId + " 使用中请稍后再发送命令");
                    return false;
                }
            }

            ChannelHandlerContext channel = (ChannelHandlerContext) nettyService.channelMap.get(concentratorId);
            if (channel != null) {
                logger.info("向集中控制器 {} 发送命令:{}", concentratorId, HexUtil.bytesTohex(bytes));
                channel.writeAndFlush(bytes);
                logger.info("向集中控制器 {} 发送命令成功", concentratorId);
                if (bool) {
                    //添加集中控制器占用，等到集中控制器给到返回结果后可继续下发命令
                    addRedisUseRecord(concentratorId);
                    //redis添加操作记录
                    addRedisExecuteRecord(concentratorId);
                }

                Thread.sleep(2000);
                return true;
            } else {
                logger.info("向集中控制器 {} 发送命令失败", concentratorId);

                return false;
            }
        } catch (Exception e) {
            logger.info("向集中控制器 {} 发送命令失败", concentratorId);
            logger.error(e.getMessage());

            return false;
        }
    }

    /**
     * redis添加集中控制器在线状态
     *
     * @param concentratorId
     */
    public static void addRedisOnlineStatus(String concentratorId) {
        redisUtil.set(concentratorId + "Online", 1);
        redisUtil.expire(concentratorId + "Online", 300);
    }

    /**
     * redis添加集中控制器不可使用状态
     *
     * @param concentratorId
     */
    public static void addRedisUseRecord(String concentratorId) {
        //redis添加集中控制器不可使用状态
        redisUtil.set(concentratorId + "Use", 1);
        redisUtil.expire(concentratorId + "Use", 120);
        //redis添加集中控制器退出时间表控制模式，TimeTable = 0 退出时间表控制模式
        redisUtil.set(concentratorId + "TimeTable", 0);
        redisUtil.set(concentratorId + "ExecuteCommandRecord", 1);
        redisUtil.expire(concentratorId + "ExecuteCommandRecord", 120);
    }

    /**
     * redis恢复集中控制器可使用状态
     *
     * @param concentratorId
     */
    public static void restoreRedisUseRecord(String concentratorId) {
        redisUtil.set(concentratorId + "Use", 0);
        redisUtil.expire(concentratorId + "Use", 120);
    }

    /**
     * redis添加集中控制器操作记录
     *
     * @param concentratorId
     */
    public static void addRedisExecuteRecord(String concentratorId) {
        redisUtil.set(concentratorId + "Execute", "wait");
        redisUtil.expire(concentratorId + "Execute", 300);
    }

    /**
     * redis添加集中控制器操作成功记录
     *
     * @param concentratorId
     */
    public static void addRedisExecuteSuccessRecord(String concentratorId) {
        redisUtil.set(concentratorId + "Execute", "success");
        redisUtil.expire(concentratorId + "Execute", 300);
    }

    /**
     * redis添加集中控制器操作失败记录
     *
     * @param concentratorId
     */
    public static void addRedisExecuteFailedRecord(String concentratorId, String errorInfo) {
        redisUtil.set(concentratorId + "Execute", "failed");
        redisUtil.expire(concentratorId + "Execute", 300);
        redisUtil.set(concentratorId + "ExecuteErrorInfo", errorInfo);
    }
}
