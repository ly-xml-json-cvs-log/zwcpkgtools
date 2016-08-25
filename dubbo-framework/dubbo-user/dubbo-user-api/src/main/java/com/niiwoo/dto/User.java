/**   
* @Title: User.java
* @Package com.niiwoo.model
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2014-10-17 上午10:14:30
* @version V1.0   
*/


package com.niiwoo.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: User
 * @Description: 登录或注册之后的返回信息
 * @author seven
 * @date 2014-10-17 上午10:14:30
 */

public class User implements Serializable{
	private static final long serialVersionUID = 1L;

	//用户id	
	private String userId;
	
	//用户名	
	private String userName;	
	
	//用户昵称	
	private String nickName;
	
	//真实姓名	
	private String realName;
	
	//身份证号	
	private String identityCard;
	
	//用户类型	
	private String userTypeId;
	
	//用户头像	
	private String headImage;
	
	//用户生日
	private Date birthday;
	
	//性别
	private String sex;
	
	//用户来源	
	private String fromType;
	
	//手机号	
	private String mobileNo;
	
	//邮箱	
	private String email;
	
	private Date addDate;
	
	//登录错误次数
	private int loginErrorNum;
	
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(String userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public int getLoginErrorNum() {
		return loginErrorNum;
	}

	public void setLoginErrorNum(int loginErrorNum) {
		this.loginErrorNum = loginErrorNum;
	}

}
