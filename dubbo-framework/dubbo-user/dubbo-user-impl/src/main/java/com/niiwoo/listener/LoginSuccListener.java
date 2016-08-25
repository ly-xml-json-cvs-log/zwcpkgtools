/**   
* @Title: LoginSuccListener.java
* @Package com.niiwoo.listener
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-8-3 下午6:17:11
* @version V1.0   
*/


package com.niiwoo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niiwoo.event.LoginSuccEvent;

/**
 * @ClassName: LoginSuccListener
 * @Description: 登录成功事件监听
 * @author seven
 * @date 2016-8-3 下午6:17:11
 * 
 */

public class LoginSuccListener implements NiiwooEventListener<LoginSuccEvent> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onNiwooEvent(LoginSuccEvent niiwooEvent) {
		logger.info("登录事件响应");
		if(niiwooEvent != null)
			logger.info(niiwooEvent.toString());
	}

	@Override
	public int getOrder() {
		return 1;
	}

}
