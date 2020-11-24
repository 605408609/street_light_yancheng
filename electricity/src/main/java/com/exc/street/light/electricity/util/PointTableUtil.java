package com.exc.street.light.electricity.util;


import com.exc.street.light.resource.dto.electricity.GatewayParameter;
import com.exc.street.light.resource.entity.electricity.CanChannel;
import com.exc.street.light.resource.entity.electricity.CanDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: XuJiaHao
 * @Description: 点表数据合成工具类
 * @Date: Created in 9:54 2020/3/13
 * @Modified:
 */
public class PointTableUtil {

    /**
     * 通过点表获取模块信息
     *
     * @param gatewayParameters
     * @return
     */
    public static List<CanDevice> getDeviceList(ArrayList<GatewayParameter> gatewayParameters){
        ArrayList<CanDevice> list = new ArrayList<>();
        if(gatewayParameters.size() >0 ){
            //添加场景模块
            CanDevice canDevice =new CanDevice();
            canDevice.setCanIndex(1);
            String id=Integer.toString((8));
            canDevice.setCanId("1.1."+id);
            canDevice.setAddress("64");
            canDevice.setModuleType("1378");
            canDevice.setWaitTime(3000);
            canDevice.setName("场景模块-"+id);
            canDevice.setBatchNumber("000000000000");
            list.add(canDevice);
            //添加驱动模块,包含电流模块，交流接触器模块
            for (int i = 0; i < gatewayParameters.size(); ++i){
                String id2=Integer.toString((i+9));
                if(gatewayParameters.get(i).getModuleDeviceType() == 4){
                    CanDevice canDevice2 = new CanDevice();
                    canDevice2.setCanIndex(i+2);
                    canDevice2.setCanId("1.1."+id2);
                    canDevice2.setModuleType(gatewayParameters.get(i).getUnitType());
                    canDevice2.setAddress(Integer.toString(gatewayParameters.get(i).getDeviceAddress()));
                    canDevice2.setWaitTime(3000);
                    canDevice2.setBatchNumber(gatewayParameters.get(i).getBatchNumber());
                    canDevice2.setName("电流模块-"+id2);
//                    canDevice2.setName(gatewayParameters.get(i).getDeviceName());
                    list.add(canDevice2);
                }else if(gatewayParameters.get(i).getModuleDeviceType() == 5){
                    CanDevice canDevice2 = new CanDevice();
                    canDevice2.setCanIndex(i+2);
                    canDevice2.setCanId("1.1."+id2);
                    canDevice2.setModuleType(gatewayParameters.get(i).getUnitType());
                    canDevice2.setBatchNumber(gatewayParameters.get(i).getBatchNumber());
                    canDevice2.setAddress(Integer.toString(gatewayParameters.get(i).getDeviceAddress()));
                    canDevice2.setWaitTime(3000);
                    canDevice2.setName("4路交流接触器模块-"+id2);
//                    canDevice2.setName(gatewayParameters.get(i).getDeviceName());
                    list.add(canDevice2);
                } else if(gatewayParameters.get(i).getModuleDeviceType() == 6){
                    CanDevice canDevice2 = new CanDevice();
                    canDevice2.setCanIndex(i+2);
                    canDevice2.setCanId("1.1."+id2);
                    canDevice2.setModuleType(gatewayParameters.get(i).getUnitType());
                    canDevice2.setBatchNumber(gatewayParameters.get(i).getBatchNumber());
                    canDevice2.setAddress(Integer.toString(gatewayParameters.get(i).getDeviceAddress()));
                    canDevice2.setWaitTime(3000);
//                    canDevice2.setName("8路交流接触器模块-"+id2);
                    canDevice2.setName(gatewayParameters.get(i).getDeviceName());
                    list.add(canDevice2);
                }else if(gatewayParameters.get(i).getModuleDeviceType() == 7){
                    CanDevice canDevice2 = new CanDevice();
                    canDevice2.setCanIndex(i+2);
                    canDevice2.setCanId("1.1."+id2);
                    canDevice2.setModuleType(gatewayParameters.get(i).getUnitType());
                    canDevice2.setBatchNumber(gatewayParameters.get(i).getBatchNumber());
                    canDevice2.setAddress(Integer.toString(gatewayParameters.get(i).getDeviceAddress()));
                    canDevice2.setWaitTime(3000);
                    canDevice2.setName("12路交流接触器模块-"+id2);
//                    canDevice2.setName(gatewayParameters.get(i).getDeviceName());
                    list.add(canDevice2);
                } else {
                    CanDevice canDevice2 = new CanDevice();
                    canDevice2.setCanIndex(i+2);
                    canDevice2.setCanId("1.1."+id2);
                    canDevice2.setModuleType(gatewayParameters.get(i).getUnitType());
                    canDevice2.setBatchNumber(gatewayParameters.get(i).getBatchNumber());
                    canDevice2.setAddress(Integer.toString(gatewayParameters.get(i).getDeviceAddress()));
                    canDevice2.setWaitTime(3000);
                    canDevice2.setName("驱动模块-"+id2);
//                    canDevice2.setName(gatewayParameters.get(i).getDeviceName());
                    list.add(canDevice2);
                }
            }
        }
        return list;
    }

    /**
     * 根据网关点表数据获取通过详细信息
     *
     * @param gatewayParameters 网关点表模块数据类
     * @return
     */
    public static List<CanChannel> getCanChannelList(ArrayList<GatewayParameter> gatewayParameters){
        ArrayList<CanChannel> list = new ArrayList<>();
        int baseTagId = 161;
        GatewayParameter gatewayParameter;
        String nameSuffix;
        if(gatewayParameters.size() > 0){
            for (int i = 0; i < gatewayParameters.size(); ++i) {

                gatewayParameter = gatewayParameters.get(i);
                //判断设备的类型 4、8、12
                if (gatewayParameter.getModuleDeviceType() == 1) {
                    for (int j = 0; j < 4; ++j) {
                        nameSuffix = Integer.toString(1 + j);
                        CanChannel channel = new CanChannel();
                        channel.setTagId(baseTagId);
                        channel.setCanIndex(i+2);
                        channel.setControlId(1+j);
                        channel.setAddress(gatewayParameter.getDeviceAddress());
                        channel.setDeviceAddress(gatewayParameter.getDeviceAddress());
                        channel.setSid(ConstantUtil.CHANNEL_DRIVE_DATA_TYPE_ID);
                        channel.setUpperValue(ConstantUtil.CHANNEL_UPPER_VALUE);
                        channel.setLowerValue(ConstantUtil.CHANNEL_LOWER_VALUE);
                        channel.setName(ConstantUtil.CHANNEL_DRIVE_NAME_PREFIX + nameSuffix);
                        list.add(channel);
                        baseTagId++;
                    }
                } else if (gatewayParameters.get(i).getModuleDeviceType() == 2) {
                    for (int j = 0; j < 8; ++j) {
                        nameSuffix = Integer.toString(1 + j);
                        CanChannel channel = new CanChannel();
                        channel.setTagId(baseTagId);
                        channel.setCanIndex(i + 2);
                        channel.setControlId(1+j);
                        channel.setDeviceAddress(gatewayParameter.getDeviceAddress());
                        channel.setAddress(gatewayParameter.getDeviceAddress());
                        channel.setSid(ConstantUtil.CHANNEL_DRIVE_DATA_TYPE_ID);
                        channel.setUpperValue(ConstantUtil.CHANNEL_UPPER_VALUE);
                        channel.setLowerValue(ConstantUtil.CHANNEL_LOWER_VALUE);
                        channel.setName(ConstantUtil.CHANNEL_DRIVE_NAME_PREFIX + nameSuffix);
                        list.add(channel);
                        baseTagId++;
                    }
                } else if (gatewayParameters.get(i).getModuleDeviceType() == 3) {
                    for (int j = 0; j < 12; ++j) {
                        nameSuffix = Integer.toString(1 + j);
                        CanChannel channel = new CanChannel();
                        channel.setTagId(baseTagId);
                        channel.setCanIndex(i + 2);
                        channel.setControlId(1+j);
                        channel.setDeviceAddress(gatewayParameter.getDeviceAddress());
                        channel.setAddress(gatewayParameter.getDeviceAddress());
                        channel.setSid(ConstantUtil.CHANNEL_DRIVE_DATA_TYPE_ID);
                        channel.setUpperValue(ConstantUtil.CHANNEL_UPPER_VALUE);
                        channel.setLowerValue(ConstantUtil.CHANNEL_LOWER_VALUE);
                        channel.setName(ConstantUtil.CHANNEL_DRIVE_NAME_PREFIX + nameSuffix);
                        list.add(channel);
                        baseTagId++;
                    }
                }
                //电流检测模块
                else if (gatewayParameters.get(i).getModuleDeviceType() == 4) {
                    for (int j = 0; j < 12; ++j) {
                        nameSuffix = Integer.toString(1 + j);
                        CanChannel channel = new CanChannel();
                        channel.setTagId(baseTagId);
                        channel.setCanIndex(i + 2);
                        channel.setControlId(1 + j);
                        if(gatewayParameters.get(i).getElectricityModuleList() != null){
                            if(j<3){
                                channel.setBindAddress(gatewayParameters.get(i).getElectricityModuleList().get(0).getDeviceAddress());
                                channel.setBindChannelControlId(gatewayParameters.get(i).getElectricityModuleList().get(0).getChannelNum());
                            }else if(j >= 3 && j < 6){
                                channel.setBindAddress(gatewayParameters.get(i).getElectricityModuleList().get(1).getDeviceAddress());
                                channel.setBindChannelControlId(gatewayParameters.get(i).getElectricityModuleList().get(1).getChannelNum());
                            }else if(j >= 6 && j < 9){
                                channel.setBindAddress(gatewayParameters.get(i).getElectricityModuleList().get(2).getDeviceAddress());
                                channel.setBindChannelControlId(gatewayParameters.get(i).getElectricityModuleList().get(2).getChannelNum());
                            }else{
                                channel.setBindAddress(gatewayParameters.get(i).getElectricityModuleList().get(3).getDeviceAddress());
                                channel.setBindChannelControlId(gatewayParameters.get(i).getElectricityModuleList().get(3).getChannelNum());
                            }
                        }
                        channel.setDeviceAddress(gatewayParameter.getDeviceAddress());
                        channel.setAddress(gatewayParameter.getDeviceAddress());
                        channel.setSid(ConstantUtil.CHANNEL_ELECTRICITY_DATA_TYPE_ID);
                        channel.setUpperValue(ConstantUtil.CHANNEL_UPPER_VALUE);
                        channel.setLowerValue(ConstantUtil.CHANNEL_LOWER_VALUE);
                        channel.setName(ConstantUtil.CHANNEL_ELECTRICITY_NAME_PREFIX + nameSuffix);
                        list.add(channel);
                        baseTagId++;
                    }
                }else if(gatewayParameters.get(i).getModuleDeviceType() == 5){
                    for (int j = 0; j < 4; ++j) {
                        nameSuffix = Integer.toString(1 + j);
                        CanChannel channel = new CanChannel();
                        channel.setTagId(baseTagId);
                        channel.setCanIndex(i + 2);
                        channel.setControlId(1 + j);
                        channel.setDeviceAddress(gatewayParameter.getDeviceAddress());
                        channel.setAddress(gatewayParameter.getDeviceAddress());
                        channel.setSid(ConstantUtil.CHANNEL_DRIVE_DATA_TYPE_ID);
                        channel.setUpperValue(ConstantUtil.CHANNEL_UPPER_VALUE);
                        channel.setLowerValue(ConstantUtil.CHANNEL_LOWER_VALUE);
                        channel.setName(ConstantUtil.CHANNEL_ACC_NAME_PREFIX + nameSuffix);
                        list.add(channel);
                        baseTagId++;
                    }
                }else if(gatewayParameters.get(i).getModuleDeviceType() == 6){
                    for (int j = 0; j < 8; ++j) {
                        nameSuffix = Integer.toString(1 + j);
                        CanChannel channel = new CanChannel();
                        channel.setTagId(baseTagId);
                        channel.setCanIndex(i + 2);
                        channel.setControlId(1 + j);
                        channel.setDeviceAddress(gatewayParameter.getDeviceAddress());
                        channel.setAddress(gatewayParameter.getDeviceAddress());
                        channel.setSid(ConstantUtil.CHANNEL_DRIVE_DATA_TYPE_ID);
                        channel.setUpperValue(ConstantUtil.CHANNEL_UPPER_VALUE);
                        channel.setLowerValue(ConstantUtil.CHANNEL_LOWER_VALUE);
                        channel.setName(ConstantUtil.CHANNEL_ACC_NAME_PREFIX + nameSuffix);
                        list.add(channel);
                        baseTagId++;
                    }
                }else if(gatewayParameters.get(i).getModuleDeviceType() == 7){
                    for (int j = 0; j < 12; ++j) {
                        nameSuffix = Integer.toString(1 + j);
                        CanChannel channel = new CanChannel();
                        channel.setTagId(baseTagId);
                        channel.setCanIndex(i + 2);
                        channel.setControlId(1 + j);
                        channel.setDeviceAddress(gatewayParameter.getDeviceAddress());
                        channel.setAddress(gatewayParameter.getDeviceAddress());
                        channel.setSid(ConstantUtil.CHANNEL_DRIVE_DATA_TYPE_ID);
                        channel.setUpperValue(ConstantUtil.CHANNEL_UPPER_VALUE);
                        channel.setLowerValue(ConstantUtil.CHANNEL_LOWER_VALUE);
                        channel.setName(ConstantUtil.CHANNEL_ACC_NAME_PREFIX + nameSuffix);
                        list.add(channel);
                        baseTagId++;
                    }
                }

            }
            //添加场景Channel
            for (int k = 0; k < 32 ; ++k){
                nameSuffix = Integer.toString(1 + k);
                CanChannel channel = new CanChannel();
                channel.setTagId(1+k);
                channel.setCanIndex(1);
                channel.setAddress(64);
                channel.setDeviceAddress(64);
                channel.setControlId(1+k);
                channel.setSid(ConstantUtil.CHANNEL_SCENE_DATA_TYPE_ID);
                channel.setUpperValue(ConstantUtil.CHANNEL_UPPER_VALUE);
                channel.setLowerValue(ConstantUtil.CHANNEL_LOWER_VALUE);
                channel.setName(ConstantUtil.CHANNEL_SCENE_NAME_PREFIX + nameSuffix);
                list.add(channel);
            }
        }
        return list;
    }
}
