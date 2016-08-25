/**   
* @Title: RegisterRequest.java
* @Package com.niiwoo.request
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2014-11-4 下午2:06:28
* @version V1.0   
*/


package com.niiwoo.dto;

import java.io.Serializable;

/**
 * @ClassName: RegisterRequest
 * @Description: 注册请求封装类
 * @author seven
 * @date 2014-11-4 下午2:06:28
 * 
 */

public class LoginRegisterDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	//用户Id
	private String userId;
	
	//用户名	
	private String userName;
	
	//昵称	
	private String nickName;
	
	//密码	
	private String password;
	
	//手机号码	
	private String phone;
	
	//邮箱	
	private String email;
	
	//注册来源	(1团贷网，2好帮贷，3你我金融，4房宝宝)
	private String fromType;
	
	//注册IP	
	private String ip;
	
	//注册类型	( 1，手机注册 2，邮箱提交注册)
	private String regType;
	
	//用户类型	(用户类型1个人，2企业)
	private String userTypeId; 
	
	//设备名称	(IOS,Android,PC)
	private String devType;
	
	private String headImage;
	
	//极光推送Id
	private String registrationId; 
	
	//邀请人手机号码 
	private String inviteUserPhone; 
	
	//设备唯一标识
	private String did; 
	
	//渠道号
	private String channelId; 
	
	private String mac;
	
	private String androidId;
	
	//邀请人用户Id
    private String inviteUserId; 
    
    private String latitude;
    
    private String longitude;
    
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public String getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(String userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getInviteUserPhone() {
		return inviteUserPhone;
	}

	public void setInviteUserPhone(String inviteUserPhone) {
		this.inviteUserPhone = inviteUserPhone;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getAndroidId() {
		return androidId;
	}

	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}

	public String getInviteUserId() {
		return inviteUserId;
	}

	public void setInviteUserId(String inviteUserId) {
		this.inviteUserId = inviteUserId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
