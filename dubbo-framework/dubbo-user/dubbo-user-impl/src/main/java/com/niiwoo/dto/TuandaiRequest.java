/**   
 * @Title: TuandaiRequest.java
 * @Package com.niiwoo.dto
 * @Description: 团贷请求
 * @author seven   
 * @date 2016-7-27 下午4:57:57
 * @version V1.0   
 */

package com.niiwoo.dto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.niiwoo.util.Constant;
import com.niiwoo.utils.text.TuandaiUtil;

/**
 * @ClassName: TuandaiRequest
 * @Description: 团贷请求
 * @author seven
 * @date 2016-7-27 下午4:57:57
 * 
 */

public class TuandaiRequest {
	//请求参数
	private static final String JSONSTRING = "jsonString";
	
	//签名
	private static final String TOKEN = "token";
	
	// 登录用户名
	private static final String LOGIN_USERNAME = "userName";

	// 登录密码
	private static final String LOGIN_PASSWORD = "password";

	// 登录来源 3你我金融
	private static final String LOGIN_FROMTYPE = "fromType";

	// IOS,Android,PC
	private static final String DEVTYPE = "DevType";

	// 登录客户端IP
	private static final String LOGIN_IP = "IP";

	// 注册用户名
	private static final String REGISTER_USERNAME = "UserName";

	// 昵称
	private static final String NICKNAME = "NickName";

	// 注册密码
	private static final String REGISTER_PASSWORD = "PassWord";

	// 手机号码
	private static final String PHONE = "Phone";

	// 邮箱
	private static final String EMAIL = "Email";

	// 注册来源 (1团贷网，2好帮贷，3你我金融，4房宝宝)
	private static final String REGISTER_FROMTYPE = "FromType";
	
	// 注册IP
	private static final String REGISTER_IP = "StrIP";

	// 注册类型 ( 1，手机注册 2，邮箱提交注册)
	private static final String REGTYPE = "RegType";

	// 用户类型 (用户类型1个人，2企业)
	private static final String USERTYPEID = "UserTypeId";

	// 请求参数
	private Map<String, String> param;

	// 请求地址
	private String requestUrl;

	// 是否异步请求
	private boolean asyn;

	public Map<String, String> getParam() {
		return param;
	}

	public String getRequestUrl() {
		return requestUrl;
	}
	
	public boolean isAsyn() {
		return asyn;
	}

	private TuandaiRequest(String requestUrl, Map<String, String> param) {
		this.requestUrl = requestUrl;
		this.param = param;
	}

	private TuandaiRequest(String requestUrl, Map<String, String> param,
			boolean asyn) {
		this.requestUrl = requestUrl;
		this.param = param;
		this.asyn = asyn;
	}

	public static TuandaiRequest buildLoginRequest(String requestUrl,
			String tuandaiEncryKey, LoginRegisterDTO loginDTO) {
		if (StringUtils.isBlank(requestUrl)
				|| StringUtils.isBlank(tuandaiEncryKey) || loginDTO == null
				|| StringUtils.isBlank(loginDTO.getUserName())
				|| StringUtils.isBlank(loginDTO.getPassword()))
			throw new IllegalArgumentException("请求参数错误");
		Map<String, String> data = new LinkedHashMap<String, String>();
		data.put(LOGIN_USERNAME, loginDTO.getUserName());
		data.put(LOGIN_PASSWORD, loginDTO.getPassword());
		data.put(LOGIN_FROMTYPE, String.valueOf(Constant.FROM_NIIWOO));
		data.put(DEVTYPE, loginDTO.getDevType());
		data.put(LOGIN_IP, loginDTO.getIp());

		Map<String, String> param = getRequestParam(data,
				tuandaiEncryKey);
		return new TuandaiRequest(requestUrl + TuandaiUtil.LOGIN_API, param, true);
	}

	public static TuandaiRequest buildRegisterRequest(String requestUrl,
			String tuandaiEncryKey, LoginRegisterDTO registerDTO) {
		if (StringUtils.isBlank(requestUrl)
				|| StringUtils.isBlank(tuandaiEncryKey) || registerDTO == null
				|| StringUtils.isBlank(registerDTO.getUserName())
				|| StringUtils.isBlank(registerDTO.getPassword()))
			throw new IllegalArgumentException("请求参数错误");
		Map<String, String> data = new LinkedHashMap<String, String>();
		data.put(REGISTER_USERNAME, registerDTO.getUserName());
		data.put(NICKNAME, registerDTO.getNickName());
		data.put(REGISTER_PASSWORD, registerDTO.getPassword());
		data.put(PHONE, registerDTO.getPhone());
		data.put(EMAIL, registerDTO.getEmail());
		data.put(REGISTER_FROMTYPE, String.valueOf(Constant.FROM_NIIWOO));
		data.put(REGISTER_IP, registerDTO.getIp());
		data.put(REGTYPE, registerDTO.getRegType());
		data.put(USERTYPEID, registerDTO.getUserTypeId());
		data.put(DEVTYPE, registerDTO.getDevType());

		Map<String, String> param = getRequestParam(data,
				tuandaiEncryKey);
		return new TuandaiRequest(requestUrl + TuandaiUtil.REGISTER_API, param);
	}
	
	private static Map<String, String> getRequestParam(Map<String,String> data, String encryKey){
		Map<String, String> param=new HashMap<String, String>();
		String jsonString = JSONObject.toJSONString(data);
		// 对请求参数列表进行签名
        String token = TuandaiUtil.sha1AndMd5(jsonString, encryKey);
        param.put(JSONSTRING, jsonString);
        param.put(TOKEN, token);
        return param;
	}

}
