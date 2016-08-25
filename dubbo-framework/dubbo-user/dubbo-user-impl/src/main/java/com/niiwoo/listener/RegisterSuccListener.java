/**   
* @Title: RegisterSuccListener.java
* @Package com.niiwoo.listener
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-8-3 下午6:19:42
* @version V1.0   
*/


package com.niiwoo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niiwoo.event.RegisterSuccEvent;

/**
 * @ClassName: RegisterSuccListener
 * @Description: 注册成功事件监听
 * @author seven
 * @date 2016-8-3 下午6:19:42
 * 
 */

public class RegisterSuccListener implements NiiwooEventListener<RegisterSuccEvent> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onNiwooEvent(RegisterSuccEvent niiwooEvent) {
		logger.info("注册事件响应");
		if(niiwooEvent != null)
			logger.info(niiwooEvent.toString());
		
	}

	@Override
	public int getOrder() {
		return 1;
	}

}
