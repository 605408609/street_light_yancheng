package com.exc.street.light.ed.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.co.client.CoverMqttClient;
import com.exc.street.light.co.client.service.CoverMqttClientService;
import com.exc.street.light.co.vo.AlarmMsgVo;
import com.exc.street.light.co.vo.DeviceParamVo;
import com.exc.street.light.co.vo.DeviceParameterCode;
import com.exc.street.light.co.client.service.ResolveService;
import com.exc.street.light.co.utils.Constants;
import com.exc.street.light.co.utils.Util;
import com.exc.street.light.ed.po.KafkaMessage;
import com.exc.street.light.ed.service.EdManholeCoverDeviceService;
import com.exc.street.light.ed.service.KafkaMessageService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.WebsocketQuery;
import com.exc.street.light.resource.entity.ed.EdManholeCoverDevice;
import com.exc.street.light.resource.vo.resp.EdRespAlarmVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author tanhonghang
 * @create 2020/10/14 21:23
 */
@Service
public class ResolveServiceImpl implements ResolveService {

    @Autowired
    private EdManholeCoverDeviceService edManholeCoverDeviceService;
    @Autowired
    private CoverMqttClientService coverMqttClientService;

    @Override
    public boolean subScriptionAll() {
        List<EdManholeCoverDevice> lists = edManholeCoverDeviceService.selectAll();
        if (lists.size() == 0) {
            return false;
        }
        try {
            for (EdManholeCoverDevice edManholeCoverDevice : lists) {
                String num = edManholeCoverDevice.getNum();
                if (StringUtils.isNotBlank(num)) {
                    // 每个设备订阅主题
                    coverMqttClientService.snSubScription(num);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public void resolveCallBack(String jsonStr) {
        if (jsonStr == null) {
            return;
        }
        Map map = JSONObject.parseObject(jsonStr, Map.class);
        String msgType = (String) map.get("msgType");
        if ("error".equals(map.get("status"))) {
            return;
        }

        JSONArray msgJsonArray = (JSONArray) map.get("msg");
        List<Map> listObject = JSONObject.parseArray(msgJsonArray.toJSONString(), Map.class);

        // 设备参数
        if (Constants.DEVICE_PARAM_TYPE.equals(msgType)) {
            // 获取设备号
            String num = (String) map.get("snCode");
            EdManholeCoverDevice edManholeCoverDevice = edManholeCoverDeviceService.getEdManholeCoverDeviceByCondition(null, null, num);
            if (edManholeCoverDevice == null) {
                return;
            }
            DeviceParamVo deviceParamVo = new DeviceParamVo();
            deviceParamVo.setSnCode((String) map.get("snCode"));
            for (Map<String, String> stringMap : listObject) {
                String attributeCode = stringMap.get("attributeCode");
                String attributeData = stringMap.get("attributeData");
                switch (attributeCode) {
                    case DeviceParameterCode.DEVICE_TIME:
                        deviceParamVo.setDeviceTime(Util.getDateByString(attributeData));
                        break;
                    case DeviceParameterCode.DIP_ANGLE:
                        deviceParamVo.setDipAngle(Integer.parseInt(attributeData));
                        edManholeCoverDevice.setRealData(Double.parseDouble(attributeData));
                        break;
                    case DeviceParameterCode.DIP_ANGLE_LIMIT:
                        deviceParamVo.setDipAngleLimit(Integer.parseInt(attributeData));
                        edManholeCoverDevice.setLimitUpper(Double.parseDouble(attributeData));
                        break;
                    case DeviceParameterCode.ELECTRICITY_QUANTITY:
                        deviceParamVo.setElectricity(Integer.parseInt(attributeData));
                        break;
                    case DeviceParameterCode.ICCID:
                        deviceParamVo.setICCID(attributeData);
                        break;
                    case DeviceParameterCode.PRODUCT_TYPE:
                        deviceParamVo.setProductType(attributeData);
                        break;
                    case DeviceParameterCode.SIGNAL_INTENSITY:
                        deviceParamVo.setSignalIntensity(attributeData);
                        break;
                    case DeviceParameterCode.SOFTWARE_VERSION:
                        deviceParamVo.setVersion(attributeData);
                        edManholeCoverDevice.setDeviceVersion(attributeData);
                        break;
                    case DeviceParameterCode.UPLOAD_INTERVAL:
                        deviceParamVo.setUploadInterval(attributeData);
                        edManholeCoverDevice.setUploadCycle(Integer.parseInt(attributeData));
                        break;
                    case DeviceParameterCode.STATUS:
                        deviceParamVo.setStatus(Util.transferDipStatus(attributeData));
                        edManholeCoverDevice.setStatus(Util.transferDipStatus(attributeData));
                        break;
                    default:
                }
            }
            // 设备参数修改
            edManholeCoverDeviceService.updateById(edManholeCoverDevice);

        }

        // 告警
        if(Constants.DEVICE_ALARM_TYPE.equals(msgType)){
            AlarmMsgVo alarmMsgVo = new AlarmMsgVo();
            alarmMsgVo.setSnCode((String) map.get("snCode"));
            Map alarmMap  = listObject.get(0);
            System.out.println(alarmMap.get("alarmCommandCode"));
            alarmMsgVo.setAlarmCommandCode(Integer.parseInt((String) alarmMap.get("alarmCommandCode")));
            alarmMsgVo.setAlarmTime(Util.getDateByString((String) alarmMap.get("alarmTime")));
            alarmMsgVo.setAlarmTypeCode(Integer.parseInt((String) alarmMap.get("alarmTypeCode")));
            // TODO 新增告警信息
            edManholeCoverDeviceService.alarmHandling(alarmMsgVo);
        }
    }
}
