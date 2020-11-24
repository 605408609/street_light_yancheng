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

package com.exc.street.light.log_api.event;

import com.exc.street.light.log_api.service.LogExceptionService;
import com.exc.street.light.log_api.service.LogNormalService;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.entity.log.LogException;
import com.exc.street.light.resource.entity.log.LogNormal;
import com.exc.street.light.resource.entity.ua.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;


/**
 * @Author: XuJiaHao
 * @Description: 异步日志监听事件
 * @Date: Created in 23:16 2020/5/7
 * @Modified:
 */
@Slf4j
@AllArgsConstructor
public class SystemLogListener {
	private final LogNormalService logNormalService;
	private final LogExceptionService logExceptionService;
	private final LogUserService logUserService;

	@Async
	@Order
	@EventListener(SystemLogNormalEvent.class)
	public void saveSysLog(SystemLogNormalEvent event) {
		//正常日志
		LogNormal sysLog = (LogNormal) event.getSource();
		User user = logUserService.get(sysLog.getUserId());
		sysLog.setUserName(user.getName());
		logNormalService.save(sysLog);
	}

	@Async
	@Order
	@EventListener(SystemLogExceptionEvent.class)
	public void saveSysLog(SystemLogExceptionEvent event) {
		//异常日志
		LogException sysLog = (LogException) event.getSource();
		User user = logUserService.get(sysLog.getUserId());
		sysLog.setUserName(user.getName());
		logExceptionService.save(sysLog);
	}
}
