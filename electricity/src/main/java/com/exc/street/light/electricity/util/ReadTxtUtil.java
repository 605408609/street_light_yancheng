package com.exc.street.light.electricity.util;


import com.exc.street.light.resource.entity.electricity.CanChannel;
import com.exc.street.light.resource.entity.electricity.CanDevice;
import com.exc.street.light.resource.entity.electricity.ComChannel;
import com.exc.street.light.resource.entity.electricity.ComDevice;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析强电点表txt文件
 *
 * @author Linshiwen
 * @date 2018/5/4
 */
public class ReadTxtUtil {
    /**
     * 解析设备地址
     *
     * @param file 导入的txt文件
     * @return 强电设备集合
     */
    public static String readAddress(File file) {
        String s = null;
        BufferedReader reader = null;
        String address = null;
        try {
            //设置编码格式为gb2312
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb2312"));
            s = reader.readLine();
            int i = 0;
            //can设备标识符后三行开始为设备信息
            int j = 3;
            while (s != null) {
                //can设备标识符
                if (s.contains(ConstantUtil.CAN_DEVICE_ADDRESS)) {
                    while (i < j) {
                        reader.readLine();
                        i++;
                    }
                    s = reader.readLine();
                    while (s != null) {
                        if (StringUtils.isBlank(s)) {
                            s = reader.readLine();
                            continue;
                        } else if (s.contains(ConstantUtil.CAN_DEVICE_ADDRESS_END)) {
                            //读取到can设备地址结束标识符时,结束循环
                            break;
                        } else {
                            //解析并设置can设备属性
                            String[] split = StringUtils.split(s, " ");
                            address = split[0];
                            address = address.substring(2);
                            address = ConstantUtil.CAN_DEVICE_ADDRESS_HEAD + address;
                        }
                        s = reader.readLine();
                    }
                }
                s = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return address;
    }

    /**
     * 解析强电设备
     *
     * @param file 导入的txt文件
     * @return 强电设备集合
     */
    public static List<CanDevice> readCanDevice(File file) {
        String s = null;
        BufferedReader reader = null;
        List<CanDevice> list = new ArrayList<CanDevice>();
        try {
            //设置编码格式为gb2312
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb2312"));
            s = reader.readLine();
            int i = 0;
            //can设备标识符后三行开始为设备信息
            int j = 3;
            while (s != null) {
                //can设备标识符
                if (s.contains(ConstantUtil.CAN_DEVICE_TAG)) {
                    while (i < j) {
                        reader.readLine();
                        i++;
                    }
                    s = reader.readLine();
                    while (s != null) {
                        if (StringUtils.isBlank(s)) {
                            s = reader.readLine();
                            continue;
                        } else if (s.contains(ConstantUtil.COM_DEVICE_TAG)) {
                            //读取到com设备标识符时,结束循环
                            break;
                        } else {
                            //解析并设置can设备属性
                            String[] split = StringUtils.split(s, " ");
                            CanDevice canDevice = new CanDevice();
                            canDevice.setCanIndex(Integer.valueOf(split[0]));
                            canDevice.setCanId(split[2]);
                            canDevice.setModuleType(split[3]);
                            canDevice.setWaitTime(Integer.valueOf(split[4]));
                            canDevice.setName(split[6]);
                            list.add(canDevice);
                        }
                        s = reader.readLine();
                    }
                }
                s = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 解析电表设备
     *
     * @param file 导入的txt文件
     * @return 电表设备集合
     */
    public static List<ComDevice> readComDevice(File file) {
        String s = null;
        BufferedReader reader = null;
        List<ComDevice> list = new ArrayList<ComDevice>();
        try {
            //设置编码格式为gb2312
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb2312"));
            s = reader.readLine();
            int i = 0;
            //com设备标识符后三行开始为设备信息
            int j = 3;
            while (s != null) {
                //com设备标识符
                if (s.contains(ConstantUtil.COM_DEVICE_TAG)) {
                    while (i < j) {
                        reader.readLine();
                        i++;
                    }
                    s = reader.readLine();
                    while (s != null) {
                        if (StringUtils.isBlank(s)) {
                            s = reader.readLine();
                            continue;
                        } else if (s.contains(ConstantUtil.COM_DEVICE_END)) {
                            //读取到com设备结束标识符时,结束循环
                            break;
                        } else {
                            //解析并设置can设备属性
                            String[] split = StringUtils.split(s, " ");
                            ComDevice comDevice = new ComDevice();
                            comDevice.setCanPort(Integer.valueOf(split[0]));
                            comDevice.setType(Integer.valueOf(split[1]));
                            comDevice.setDeviceAddress(Integer.valueOf(split[2]));
                            comDevice.setModuleAddress(split[3]);
                            comDevice.setCycleTime(Integer.valueOf(split[4]));
                            comDevice.setWaitTime(Integer.valueOf(split[5]));
                            comDevice.setFailureValue(Integer.valueOf(split[6]));
                            comDevice.setName(split[7]);
                            list.add(comDevice);
                        }
                        s = reader.readLine();
                    }
                }
                s = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 解析强电回路
     *
     * @param file
     * @return
     */
    public static List<CanChannel> readCanChannel(File file) {
        String s = null;
        BufferedReader reader = null;
        List<CanChannel> list = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb2312"));
            s = reader.readLine();
            int i = 0;
            //can回路标识符后三行开始为设备信息
            int j = 3;
            list = new ArrayList<CanChannel>();
            while (s != null) {
                //can回路标识符
                if (s.contains(ConstantUtil.CAN_CHANNEL_TAG)) {
                    while (i < j) {
                        reader.readLine();
                        i++;
                    }
                    s = reader.readLine();
                    while (s != null) {
                        if (StringUtils.isBlank(s)) {
                            s = reader.readLine();
                            continue;
                        } else if (s.contains(ConstantUtil.CAN_CHANNEL_END)) {
                            //读取到can回路结束标识符时,结束循环
                            break;
                        } else {
                            //解析并设置can回路属性
                            String[] split = StringUtils.split(s, " ");
                            CanChannel channel = new CanChannel();
                            channel.setTagId(Integer.valueOf(split[0]));
                            channel.setCanIndex(Integer.valueOf(split[1]));
                            channel.setAddress(Integer.valueOf(split[2]));
                            channel.setSid(Integer.valueOf(split[3]));
                            channel.setUpperValue(Double.valueOf(split[4]));
                            channel.setLowerValue(Double.valueOf(split[5]));
                            channel.setName(split[7]);
                            list.add(channel);
                        }
                        s = reader.readLine();
                    }
                }
                s = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(list);
        return list;
    }

    /**
     * 解析电表回路
     *
     * @param file
     * @return
     */
    public static List<ComChannel> readComChannel(File file) {
        String s = null;
        BufferedReader reader = null;
        List<ComChannel> list = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb2312"));
            s = reader.readLine();
            int i = 0;
            //com回路标识符后三行开始为设备信息
            int j = 3;
            list = new ArrayList<ComChannel>();
            while (s != null) {
                //com回路标识符
                if (s.contains(ConstantUtil.COM_CHANNEL_TAG)) {
                    while (i < j) {
                        reader.readLine();
                        i++;
                    }
                    s = reader.readLine();
                    while (s != null) {
                        if (StringUtils.isBlank(s)) {
                            s = reader.readLine();
                            continue;
                        } else {
                            //解析并设置com回路属性
                            String[] split = StringUtils.split(s, " ");
                            ComChannel channel = new ComChannel();
                            channel.setTagId(Integer.valueOf(split[0]));
                            channel.setAddress(Integer.valueOf(split[1]));
                            channel.setType(Integer.valueOf(split[2]));
                            channel.setDeviceAddress(Integer.valueOf(split[3]));
                            channel.setComPort(Integer.valueOf(split[6]));
                            channel.setBadValue(Double.valueOf(split[7]));
                            channel.setUpperValue(Double.valueOf(split[8]));
                            channel.setLowerValue(Double.valueOf(split[9]));
                            channel.setName(split[10]);
                            list.add(channel);
                        }
                        s = reader.readLine();
                    }
                }
                s = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(list);
        return list;
    }
}
