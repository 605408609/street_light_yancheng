/**   
 * Copyright © 2019 dream horse Info. Tech Ltd. All rights reserved.
 * @Package: com.gitee.flying.cattle.mdg.aid
 * @author: flying-cattle  
 * @date: 2019年4月9日 下午8:15:25 
 */
package com.exc.street.light.lj.core;

/**
 * Copyright: Copyright (c) 2019 
 * 
 * <p>说明： 用户服务层</P>
 * @version: V1.0
 * @author: flying.cattle
 */
public class Const {
	/********************** 响应 ****************************/
	public static final int CODE_SUCCESS = 200; // 成功
	public static final int CODE_FAILED = 400; // 失败
	public static final int UNAUTHORIZED = 401; // 未认证
	public static final int FORBIDDEN = 403; // 未授权
	public static final int NOT_FOUND = 404; // 接口不存在
	public static final int INTERNAL_SERVER_ERROR = 500; // 内部错误
	public static final String OPERATE_SUCCESS = "success"; // 成功
	public static final String OPERATE_FAILED = "failed"; // 失败

	public static final String HANDLE_PATH = ".*/((index.html)|(index.jsp)).*";
}
