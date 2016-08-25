/**   
* @Title: TuandaiResponseDTO.java
* @Package com.niiwoo.dto
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-27 下午4:40:50
* @version V1.0   
*/


package com.niiwoo.dto;

/**
 * @ClassName: TuandaiResponseDTO
 * @Description: 团贷返回结果
 * @author seven
 * @date 2016-7-27 下午4:40:50
 * 
 */

public class TuandaiResponse {
	//成功编码
	private static final String SUCC_CODE = "00";
	//异常编码
	private static final String EXCEPTION_CODE = "100";
	
	//用户ID	userId	String	
	private String userId;
	
	//用户名	uname	string
	private String uname;
	
	//真实姓名	realName	string
	private String realName;
	
	//证件号码	identityCard	string
	private String identityCard;
	
	//性别	sex	int	1男，2女
	private String sex;
	
	//证件类型	CredTypeId	int	证件类型 1身份证，2港澳台胞证，3回乡证
	private String CredTypeId;
	
	//用户类型	UserTypeId	int	用户类型 1个人用户，2企业用户
	private String UserTypeId;
	
	//手机号	mobileNo	string
	private String mobileNo;
	
	//邮箱	Email	string
	private String Email;
	
	//响应状态	status	string	00成功其它是失败
	private String status;
	
	//描述	desc	string	失败说明
	private String desc;
	
	//错误次数
	private int LoginErrorNum; 

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCredTypeId() {
		return CredTypeId;
	}

	public void setCredTypeId(String credTypeId) {
		CredTypeId = credTypeId;
	}

	public String getUserTypeId() {
		return UserTypeId;
	}

	public void setUserTypeId(String userTypeId) {
		UserTypeId = userTypeId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public boolean isSucc(){
		return SUCC_CODE.equals(status);
	}
	
	public boolean isException(){
		return EXCEPTION_CODE.equals(status);
	}

	public int getLoginErrorNum() {
		return LoginErrorNum;
	}

	public void setLoginErrorNum(int loginErrorNum) {
		LoginErrorNum = loginErrorNum;
	}
	
}
