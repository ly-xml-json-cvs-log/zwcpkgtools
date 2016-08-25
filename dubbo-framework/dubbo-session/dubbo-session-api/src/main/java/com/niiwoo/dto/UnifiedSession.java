/**   
* @Title: Session.java
* @Package com.niiwoo.dubbo.session.model
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-11 上午9:51:54
* @version V1.0   
*/


package com.niiwoo.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: Session
 * @Description: session内容
 * @author seven
 * @date 2016-7-11 上午9:51:54
 * 
 */

public class UnifiedSession implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String sessionId; //会话Id
	
	@NotNull
	private String userId; //用户ID
	
	public String devType; //登录设备(IOS,PC,Android)
	
	private String registrationId; //极光推送Id
	
	private long expireTime; //过期时间

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	
    @Override
    public String toString() {
        return "UnifiedSession (" +
                "id=" + sessionId +
                ", userId='" + userId + '\'' +
                ", devType='" + devType + '\'' +
                ", registrationId='" + registrationId + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ')';
    }
}
