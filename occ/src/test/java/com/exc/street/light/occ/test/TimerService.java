package com.exc.street.light.occ.test;

public class TimerService {
//	/**
//	 * 定时测试
//	 */
//	@Async
//	@Scheduled(cron = "0/30 * * * * ? ")
//	private void test() {
//		AhPlay ap = new AhPlay();
////		String deviceNum = "DS-PEA22-F0120200117AACHE12620326C";
//		String deviceNum = "DS-PEA22-F0120200117AACHE12620402C";
//		
//		ap.setDeviceNum(deviceNum.trim());
//		
////		Integer lampId  = TimerService.selectLampIdByDeviceNum(deviceNum);//灯杆ID
//		OccAhPlayVO lampId  =getLampId(ap);
//		System.out.println("----------- lampId: " + lampId);
//       ap.setLampId(lampId);
//       
//       ap.setCreateTime(new Date());
//       
//		ap.setStatus(0);
//		ap.setCreateTime(new Date());
//		TimerService.sendMessage(0, WebsocketUtil.getJsonString(ap, ConstantUtil.OCC_TYPE));
////		TimerService.insert(ConstantUtil.STATUS_START, deviceNum,lampId);
//		TimerService.insertAhPlay(ap);
////		System.out.println("进入测试方法0429 -- " + WebsocketUtil.getJsonString(ap, ConstantUtil.OCC_TYPE));
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ap.setStatus(1);
//		TimerService.sendMessage(0, WebsocketUtil.getJsonString(ap, ConstantUtil.OCC_TYPE));
////		TimerService.insert(ConstantUtil.STATUS_RESTORE, deviceNum,lampId);
//		
//		TimerService.insertAhPlay(ap);
//
//	}
}
