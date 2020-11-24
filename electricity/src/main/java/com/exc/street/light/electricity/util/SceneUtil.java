package com.exc.street.light.electricity.util;

import com.exc.street.light.resource.dto.electricity.ChannelControlDeviceParameter;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.dto.electricity.Scene;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景工具类
 *
 * @author Linshiwen
 * @date 2018/5/29
 */
public class SceneUtil {
    /**
     * 获取相反的场景对象
     *
     * @param scene 原场景对象
     * @return 相反场景对象
     */
    public static Scene getReverse(Scene scene) {
        Scene reverseScene = new Scene();
        BeanUtils.copyProperties(scene, reverseScene);
        int sn = scene.getSn();
        reverseScene.setSn(sn + 16);
        reverseScene.setName("场景" + sn + "相反的场景");
        List<ControlObject> controlObjects = scene.getControlObjects();
        List<ControlObject> objectList = new ArrayList<>();
        for (ControlObject controlObject : controlObjects) {
            ControlObject controlObject1 = new ControlObject();
            BeanUtils.copyProperties(controlObject, controlObject1);
            if (controlObject.getTagValue() == 0) {
                controlObject1.setTagValue(1);
            } else {
                controlObject1.setTagValue(0);
            }
            objectList.add(controlObject1);
        }
        reverseScene.setControlObjects(objectList);
        return reverseScene;
    }

    /**
     * 按场景的回路参数生成字节数组
     *
     * @param controlObjects
     * @param Parameters
     * @return
     */
    public static byte[] getSceneData(List<ControlObject> controlObjects, List<ChannelControlDeviceParameter> Parameters){
        ControlObject controlObject;
        List<ChannelControlDeviceParameter> channelControlDeviceParameters = Parameters;
        //生成协议
        for (int i = 0; i < controlObjects.size(); ++i){
            for (int j = 0;j < channelControlDeviceParameters.size() ; ++j){
                controlObject = controlObjects.get(i);
                if(channelControlDeviceParameters.get(j).getAddress() == controlObject.getDeviceAddress()){
                    if(controlObject.getTagValue() == 1){
                        int tmp = channelControlDeviceParameters.get(j).getChannel1();
                        int tmp2 = 1;
                        tmp = tmp | (tmp2 << (controlObject.getControlId()-1));
                        channelControlDeviceParameters.get(j).setChannel1(tmp);
                    }else{
                        int tmp = channelControlDeviceParameters.get(j).getChannel2();
                        int tmp2 = 1;
                        tmp = tmp | (tmp2 << (controlObject.getControlId()-1));
                        channelControlDeviceParameters.get(j).setChannel2(tmp);
                    }
                }
            }
        }
        //组合
        byte[] bytes = new byte[9 * channelControlDeviceParameters.size()];
        for (int i = 0;i < channelControlDeviceParameters.size() ; ++i) {
            int address = channelControlDeviceParameters.get(i).getAddress();
            int channel1 = channelControlDeviceParameters.get(i).getChannel1();
            int channel2 = channelControlDeviceParameters.get(i).getChannel2();

            bytes[i * 9] = (byte) address;
            bytes[i * 9 + 1] = (byte) (channel1 >> 0);
            bytes[i * 9 + 2] = (byte) (channel1 >> 8);
            bytes[i * 9 + 3] = (byte) (channel2 >> 0);
            bytes[i * 9 + 4] = (byte) (channel2 >> 8);

            int delayedTime = ConstantUtil.CHANNEL_CONTROL_DELAYED_TIME;
            bytes[i * 9 + 5] = (byte) (delayedTime >> 24);
            bytes[i * 9 + 6] = (byte) (delayedTime >> 16);
            bytes[i * 9 + 7] = (byte) (delayedTime >> 8);
            bytes[i * 9 + 8] = (byte) (delayedTime >> 0);
        }
        return bytes;
    }
}
