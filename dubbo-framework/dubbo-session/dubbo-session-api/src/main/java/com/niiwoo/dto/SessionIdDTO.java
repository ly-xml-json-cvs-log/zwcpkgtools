/**   
* @Title: SessionIdDTO.java
* @Package com.niiwoo.dubbo.session.DTO
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-13 下午6:23:49
* @version V1.0   
*/


package com.niiwoo.dto;

import java.io.Serializable;

/**
 * @ClassName: SessionIdDTO
 * @Description: sessionId包装类
 * @author seven
 * @date 2016-7-13 下午6:23:49
 * 
 */

public class SessionIdDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String sessionId; //会话Id

	public SessionIdDTO(String sessionId){
		this.sessionId = sessionId;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
}
