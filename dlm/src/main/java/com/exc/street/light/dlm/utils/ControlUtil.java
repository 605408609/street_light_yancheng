package com.exc.street.light.dlm.utils;


import com.exc.street.light.resource.dto.electricity.ChannelControlDeviceParameter;
import com.exc.street.light.resource.dto.electricity.ControlCommand;

import java.util.List;

/**
 * @Author: XuJiaHao
 * @Description: 回路控制辅助类
 * @Date: Created in 11:24 2020/3/14
 * @Modified:
 */
public class ControlUtil {

    /**
     * 按协议获取控制协议的字节数组
     *
     * @param controlCommands
     * @param Parameters
     * @return
     */
    public static byte[] getControlData(List<ControlCommand> controlCommands, List<ChannelControlDeviceParameter> Parameters){
        ControlCommand controlCommand;
        List<ChannelControlDeviceParameter> channelControlDeviceParameters = Parameters;
        //生成协议
        for (int i = 0; i < controlCommands.size(); ++i){
            for (int j = 0;j < channelControlDeviceParameters.size() ; ++j){
                controlCommand = controlCommands.get(i);
                if(channelControlDeviceParameters.get(j).getAddress() == controlCommand.getDeviceAddress()){
                    if(controlCommand.getValue() == 1){
                        int tmp = channelControlDeviceParameters.get(j).getChannel1();
                        int tmp2 = 1;
                        tmp = tmp | (tmp2 << (controlCommand.getControlId()-1));
                        channelControlDeviceParameters.get(j).setChannel1(tmp);
                    }else{
                        int tmp = channelControlDeviceParameters.get(j).getChannel2();
                        int tmp2 = 1;
                        tmp = tmp | (tmp2 << (controlCommand.getControlId()-1));
                        channelControlDeviceParameters.get(j).setChannel2(tmp);
                    }
                }
            }
        }
        //组合
        byte[] bytes = new byte[7 * channelControlDeviceParameters.size()];
        for (int i = 0;i < channelControlDeviceParameters.size() ; ++i) {
            int address = channelControlDeviceParameters.get(i).getAddress();
            int channel1 = channelControlDeviceParameters.get(i).getChannel1();
            int channel2 = channelControlDeviceParameters.get(i).getChannel2();
            int channel3 = channelControlDeviceParameters.get(i).getChannel3();

            bytes[i * 7] = (byte) address;
            bytes[i * 7 + 1] = (byte) (channel1 >> 0);
            bytes[i * 7 + 2] = (byte) (channel1 >> 4);
            bytes[i * 7 + 3] = (byte) (channel2 >> 0);
            bytes[i * 7 + 4] = (byte) (channel2 >> 4);
            bytes[i * 7 + 5] = (byte) (channel3 >> 0);
            bytes[i * 7 + 6] = (byte) (channel3 >> 4);
        }
        return bytes;
    }
}
