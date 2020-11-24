/*
 *
 *  *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.exc.street.light.log_api.aspect;

import cn.hutool.Hutool;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.log_api.event.SystemLogExceptionEvent;
import com.exc.street.light.log_api.event.SystemLogNormalEvent;
import com.exc.street.light.log_api.util.SystemLogUtils;
import com.exc.street.light.log_api.util.SpringContextHolder;
import com.exc.street.light.resource.entity.log.LogException;
import com.exc.street.light.resource.entity.log.LogNormal;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * @Author: XuJiaHao
 * @Description: 日志AOP
 * @Date: Created in 23:16 2020/5/7
 * @Modified:
 */


@Slf4j
@Aspect
public class SystemLogAspect {

	@Pointcut("@annotation(com.exc.street.light.log_api.annotation.SystemLog)")
	public void log(){}

	@SneakyThrows
	@AfterReturning(value = "log() && @annotation(systemLog)", returning = "Result")
	public void saveLog(JoinPoint point, SystemLog systemLog, Object Result) {
		String params = getRequestParams(point.getArgs());
		String strClassName = point.getTarget().getClass().getName();
		String strMethodName = point.getSignature().getName();
		log.info("[类名]:{},[方法]:{}", strClassName, strMethodName);

//		// 从切面织入点处通过反射机制获取织入点处的方法
//		MethodSignature signature = (MethodSignature) point.getSignature();
//		// 获取切入点所在的方法
//		Method method = signature.getMethod();
//		// 获取操作
//		SystemLog opLog = method.getAnnotation(SystemLog.class);
		if (systemLog != null) {
			String logModul = systemLog.logModul();
			String logType = systemLog.logType();
			String logDesc = systemLog.logDesc();
			LogNormal logNormal= SystemLogUtils.getSysNormalLog(params,logModul,logType,logDesc, JSON.toJSONString(Result));
			//event处理日志
			log.info("进入日志处理!");
			SpringContextHolder.publishEvent(new SystemLogNormalEvent(logNormal));
		}
	}

	@SneakyThrows
	@AfterThrowing(value = "log() && @annotation(systemLog)", throwing = "e")
	public void saveExceptionLog(JoinPoint point, SystemLog systemLog,  Throwable e) {
		log.info("出现了异常");
		String params = getRequestParams(point.getArgs());
		if (systemLog != null) {
			String logModul = systemLog.logModul();
			String logType = systemLog.logType();
			String logDesc = systemLog.logDesc();
			String reason = stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace());
			LogException logException= SystemLogUtils.getSysExceptionLog(params,logModul,logType,logDesc,reason);
			//event处理日志
			SpringContextHolder.publishEvent(new SystemLogExceptionEvent(logException));
		}
	}

	public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
		StringBuffer strbuff = new StringBuffer();
		for (StackTraceElement stet : elements) {
			strbuff.append(stet + "\n");
		}
		String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
		return message;
	}

	private String getRequestParams(Object[] args){
		ArrayList<Object> arguments = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile || args[i] == null) {
				//ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
				//ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
				continue;
			}else {
				arguments.add(args[i]);
			}

		}
		String params = "";
		if (arguments != null) {
			try {
				params = JSONObject.toJSONString(arguments);
			} catch (Exception e) {
				params = arguments.toString();
			}
		}
		return params.substring(1,params.length()-1);
	}

}
