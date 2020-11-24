package com.exc.street.light.resource.utils;

import com.exc.street.light.resource.dto.WebsocketQuery;

public class WebsocketUtil {
	/**
	 * 获取WebSocket发送至前端的json数据
	 * @param <T>
	 * @param t	一键呼叫告警信息、工单推送、单灯控制信息刷新功能的数据体
	 * @param type	一键呼叫告警信息:14,工单推送:15,单灯控制信息刷新：16，在ConstantUtil类里面定义
	 * @return
	 */
	public static <T> String getJsonString(T t,int type) {
		WebsocketQuery<T> wq = new WebsocketQuery<T>();
		wq.setType(type);
		wq.setData(t);
		String jsonString = JacksonUtil.toJsonString(wq);
		return jsonString;
	}
}
