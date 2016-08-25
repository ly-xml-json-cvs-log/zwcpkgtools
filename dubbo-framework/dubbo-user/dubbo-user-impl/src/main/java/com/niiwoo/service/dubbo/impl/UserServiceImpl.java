/**   
* @Title: UserServiceImpl.java
* @Package com.niiwoo.dubbo.service.impl
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-25 下午5:49:43
* @version V1.0   
*/


package com.niiwoo.service.dubbo.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.niiwoo.dao.model.userbasic.UserBasicInfo;
import com.niiwoo.dto.LoginRegisterDTO;
import com.niiwoo.dto.ResponseDTO;
import com.niiwoo.dto.TuandaiRequest;
import com.niiwoo.dto.TuandaiResponse;
import com.niiwoo.dto.User;
import com.niiwoo.enums.RtnEnum;
import com.niiwoo.event.LoginSuccEvent;
import com.niiwoo.event.RegisterSuccEvent;
import com.niiwoo.multicaster.NiiwooEventMulticaster;
import com.niiwoo.service.dubbo.IUserService;
import com.niiwoo.service.local.ITuandaiUserService;
import com.niiwoo.service.local.IUserStorageService;
import com.niiwoo.util.Constant;

/**
 * @ClassName: UserServiceImpl
 * @Description: 用户服务实现类
 * @author seven
 * @date 2016-7-25 下午5:49:43
 * 
 */
@Service(protocol = {"dubbo","rest"})
public class UserServiceImpl implements IUserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Value("${tuandai.apiAddress}")
	private String tuandaiApiAddress;
	
	@Value("${tuandai.encryKey}")
	private String tuandaiEncryKey;
	
	@Autowired
	private ITuandaiUserService tuandaiUserService;
	
	@Autowired
	private IUserStorageService userStorageService;
	
	@Autowired
	private NiiwooEventMulticaster niiwooEventMulticaster;
	
	@Override
	public ResponseDTO<User> userLogin(LoginRegisterDTO loginDTO) {
		if(StringUtils.isEmpty(loginDTO.getFromType()))
			loginDTO.setFromType(String.valueOf(Constant.FROM_NIIWOO));
		if(StringUtils.isEmpty(loginDTO.getDevType()))
			loginDTO.setDevType(Constant.DEVICE_PC);
		TuandaiRequest tuandaiRequest = TuandaiRequest.buildLoginRequest(tuandaiApiAddress, 
				tuandaiEncryKey, loginDTO);
		TuandaiResponse tuandaiResponse = tuandaiUserService.post(tuandaiRequest);
		ResponseDTO<User> responseDTO = new ResponseDTO<>();
		if(tuandaiResponse == null){
			logger.error("login response is null,userName:{}", loginDTO.getUserName());
			responseDTO.setRtnEnum(RtnEnum.FAILURE);
		}else if(tuandaiResponse.isSucc()){
			logger.debug("login succ,userName:{},response:{}", loginDTO.getUserName(),
					JSONObject.toJSONString(tuandaiResponse));
			responseDTO.setRtnEnum(RtnEnum.SUCCESS);
			UserBasicInfo userBasicInfo = userStorageService.userSync(loginDTO, tuandaiResponse);
			responseDTO.setT(convert(userBasicInfo));
			//分发登录成功事件
			fireLoginSuccEvent(userBasicInfo);
		}else if(tuandaiResponse.isException()){
			logger.error("login exception,userName:{},response:{}", loginDTO.getUserName(),
					JSONObject.toJSONString(tuandaiResponse));
			responseDTO.setRtnEnum(RtnEnum.EXCEPTION);
		}else{
			logger.debug("login error,userName:{},response:{}", loginDTO.getUserName(),
					JSONObject.toJSONString(tuandaiResponse));
			responseDTO.setRtnEnum(RtnEnum.FAILURE);
			responseDTO.setMsg(tuandaiResponse.getDesc());
			User user = new User();
			user.setLoginErrorNum(tuandaiResponse.getLoginErrorNum());
			responseDTO.setT(user);
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO<User> registerUser(LoginRegisterDTO registerDTO) {
		if(StringUtils.isEmpty(registerDTO.getFromType()))
			registerDTO.setFromType(String.valueOf(Constant.FROM_NIIWOO));
		if(StringUtils.isEmpty(registerDTO.getDevType()))
			registerDTO.setDevType(Constant.DEVICE_PC);
		TuandaiRequest tuandaiRequest = TuandaiRequest.buildRegisterRequest(tuandaiApiAddress, 
				tuandaiEncryKey, registerDTO);
		TuandaiResponse tuandaiResponse = tuandaiUserService.post(tuandaiRequest);
		ResponseDTO<User> responseDTO = new ResponseDTO<>();
		if(tuandaiResponse == null){
			logger.error("register response is null,userName:{}", registerDTO.getUserName());
			responseDTO.setRtnEnum(RtnEnum.FAILURE);
		}else if(tuandaiResponse.isSucc()){
			registerDTO.setUserId(tuandaiResponse.getUserId());
			logger.debug("register succ,userName:{},response:{}", registerDTO.getUserName(),
					 JSONObject.toJSONString(tuandaiResponse));
			responseDTO.setRtnEnum(RtnEnum.SUCCESS);
			UserBasicInfo userBasicInfo = userStorageService.addUser(registerDTO);
			responseDTO.setT(convert(userBasicInfo));
			//分发注册成功事件
			fireRegisterSuccEvent(userBasicInfo);
		}else{
			logger.debug("register error,userName:{},response:{}", registerDTO.getUserName(),
					 JSONObject.toJSONString(tuandaiResponse));
			responseDTO.setRtnEnum(RtnEnum.FAILURE);
			responseDTO.setMsg(tuandaiResponse.getDesc());
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO<User> getUser(String userId) {
		UserBasicInfo userBasicInfo = userStorageService.getUserBasicInfoById(userId);
		RtnEnum rtnEnum = userBasicInfo == null 
				? RtnEnum.NOTEXIST : RtnEnum.SUCCESS;
		User user = rtnEnum == RtnEnum.SUCCESS
				? convert(userBasicInfo) : null;
		ResponseDTO<User> response = new ResponseDTO<>();
		response.setRtnEnum(rtnEnum);
		response.setT(user);
		return response;
	}

	private User convert(UserBasicInfo userBasicInfo){
		User user = new User();
		if(userBasicInfo == null)
			return user;
		user.setUserId(userBasicInfo.getUserId());
		user.setUserName(userBasicInfo.getUserName());
		user.setNickName(userBasicInfo.getNickName());
		user.setRealName(userBasicInfo.getRealName());
		user.setIdentityCard(userBasicInfo.getIdentityCard());
		user.setSex(String.valueOf(userBasicInfo.getSex()));
		user.setBirthday(userBasicInfo.getBirthday());
		user.setEmail(userBasicInfo.getEmail());
		user.setMobileNo(userBasicInfo.getMobileNo());
		user.setFromType(String.valueOf(userBasicInfo.getFromType()));
		user.setAddDate(userBasicInfo.getAddDate());
		user.setHeadImage(userBasicInfo.getHeadImage());
		return user;
	}
	
	private void fireLoginSuccEvent(UserBasicInfo userBasicInfo){
		LoginSuccEvent event = new LoginSuccEvent(userBasicInfo);
		niiwooEventMulticaster.multicastEvent(event);
	}
	
	private void fireRegisterSuccEvent(UserBasicInfo userBasicInfo){
		RegisterSuccEvent event = new RegisterSuccEvent(userBasicInfo);
		niiwooEventMulticaster.multicastEvent(event);
	}
}
