/**   
* @Title: RegisterSuccEvent.java
* @Package com.niiwoo.event
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-8-3 下午2:09:16
* @version V1.0   
*/


package com.niiwoo.event;

import com.niiwoo.dao.model.userbasic.UserBasicInfo;

/**
 * @ClassName: RegisterSuccEvent
 * @Description: 注册成功事件
 * @author seven
 * @date 2016-8-3 下午2:09:16
 * 
 */

public class RegisterSuccEvent extends NiiwooEvent {
	private static final long serialVersionUID = 1L;
	
	private UserBasicInfo userBasicInfo;

	public RegisterSuccEvent(UserBasicInfo userBasicInfo){
		super(userBasicInfo);
		this.userBasicInfo = userBasicInfo;
	}

	public UserBasicInfo getUserBasicInfo() {
		return userBasicInfo;
	}
}
