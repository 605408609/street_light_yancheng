//package com.exc.street.light.ss;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.exc.street.light.resource.core.HttpClientResult;
//import com.exc.street.light.resource.core.Result;
//import com.exc.street.light.resource.entity.dlm.SlLampPost;
//import com.exc.street.light.resource.utils.HttpClientUtils;
//import com.exc.street.light.resource.utils.HttpUtil;
//import com.exc.street.light.resource.utils.JacksonUtil;
//
///**
// * SsApplicationTest
// *
// * @author liufei
// * @date 2020/06/05
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SsApplicationTest {
//	private static final Logger logger = LoggerFactory.getLogger(SsApplicationTest.class);
//
//	/**
//	 * 第一次新增摄像头、设置为灯杆默认弹窗显示的摄像头
//	 */
//	@Test
//	public void setDefaultCamera() {
//
//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("Content-Type", "application/json;charset=UTF-8");
//		headers.put("token",
//				"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicGhvbmUiOiIxNTk4ODg4ODg4OCIsImlhdCI6MTU5NTA1OTM4OCwiZXhwIjoxNTk1MTQ1Nzg4fQ.HCTJBaTU_vHV42sYtDsqaBPX4rICAqzEwWRZE7UUrDI");
//
//		String getObjectUrl = "http://127.0.0.1:60027/api/dlm/sl/lamp/post/" + 4;
//		String updateObjectUrl = "http://127.0.0.1:60027/api/dlm/sl/lamp/post/update";
//		String jsonStr = null;
//		try {
//			HttpClientResult hcr = HttpClientUtils.doGet(getObjectUrl);
//			jsonStr = hcr.getContent();
//			logger.info("获取灯杆的信息是：" + jsonStr);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		Result<SlLampPost> rs = JacksonUtil.json2Object(jsonStr, Result.class, SlLampPost.class);
//		if (rs != null && rs.getData() != null) {
//			Integer s = rs.getData().getDefaultCameraId();
//			SlLampPost slLampPost = rs.getData();
//			slLampPost.setDefaultCameraId(2);
//			if (s == null) {
//				logger.info("DefaultCameraId 为空，设置当前摄像头为灯杆默认的弹窗显示摄像头");
//				HttpUtil.put(updateObjectUrl, JacksonUtil.toJsonString(slLampPost), headers);
//			} else {
//				logger.debug("DefaultCameraId 为空，不修改当前灯杆默认的弹窗显示的摄像头");
//			}
//		}
//
//	}
//	
//	@Test
//	public void setDefaultCameraId() {
//
//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("Content-Type", "application/json;charset=UTF-8");
//		headers.put("token",
//				"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicGhvbmUiOiIxNTk4ODg4ODg4OCIsImlhdCI6MTU5NTA1OTM4OCwiZXhwIjoxNTk1MTQ1Nzg4fQ.HCTJBaTU_vHV42sYtDsqaBPX4rICAqzEwWRZE7UUrDI");
//
//		String getObjectUrl = "http://127.0.0.1:60027/api/dlm/sl/lamp/post/" + 4;
//		String updateObjectUrl = "http://127.0.0.1:60027/api/dlm/sl/lamp/post/update";
//		String jsonStr = null;
//		try {
//			HttpClientResult hcr = HttpClientUtils.doGet(getObjectUrl);
//			jsonStr = hcr.getContent();
//			logger.info("获取灯杆的信息是：" + jsonStr);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		Result<SlLampPost> rs = JacksonUtil.json2Object(jsonStr, Result.class, SlLampPost.class);
//		if (rs != null && rs.getData() != null) {
//			Integer s = rs.getData().getDefaultCameraId();
//			SlLampPost slLampPost = rs.getData();
//			slLampPost.setDefaultCameraId(2);
//			if (s == null) {
//				logger.info("DefaultCameraId 为空，设置当前摄像头为灯杆默认的弹窗显示摄像头");
//				HttpUtil.put(updateObjectUrl, JacksonUtil.toJsonString(slLampPost), headers);
//			} else {
//				logger.debug("DefaultCameraId 为空，不修改当前灯杆默认的弹窗显示的摄像头");
//			}
//		}
//
//	}
//	
//}