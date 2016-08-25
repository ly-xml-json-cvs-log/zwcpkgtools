/**   
 * @Title: UserStorageServiceImpl.java
 * @Package com.niiwoo.service.local.impl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author seven   
 * @date 2016-7-28 上午10:00:44
 * @version V1.0   
 */

package com.niiwoo.service.local.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.niiwoo.common.utils.DateUtils;
import com.niiwoo.common.utils.IdcardUtils;
import com.niiwoo.common.utils.SysStringUtils;
import com.niiwoo.common.utils.UploadConfig;
import com.niiwoo.dao.mapper.ApplyUserDetailNewMapper;
import com.niiwoo.dao.mapper.UserAccountMapper;
import com.niiwoo.dao.mapper.UserCreditStateMapper;
import com.niiwoo.dao.mapper.UserDetailNewMapper;
import com.niiwoo.dao.mapper.qqj.QQJUserAccountMapper;
import com.niiwoo.dao.mapper.userbasic.UserBasicInfoExtMapper;
import com.niiwoo.dao.mapper.userbasic.UserBasicInfoMapper;
import com.niiwoo.dao.model.UserAccount;
import com.niiwoo.dao.model.UserCreditState;
import com.niiwoo.dao.model.UserDetailNew;
import com.niiwoo.dao.model.qqj.QQJUserAccount;
import com.niiwoo.dao.model.userbasic.UserBasicInfo;
import com.niiwoo.dao.model.userbasic.UserBasicInfoExt;
import com.niiwoo.dto.LoginRegisterDTO;
import com.niiwoo.dto.TuandaiResponse;
import com.niiwoo.service.local.IUserStorageService;
import com.niiwoo.util.Constant;
import com.niiwoo.utils.text.TuandaiUtil;

/**
 * @ClassName: UserStorageServiceImpl
 * @Description: 用户存储服务实现
 * @author seven
 * @date 2016-7-28 上午10:00:44
 * 
 */
@Service("userStorageService")
public class UserStorageServiceImpl implements IUserStorageService {
	private static final Logger logger = LoggerFactory.getLogger(UserStorageServiceImpl.class);
	
	@Resource(name = "stringRedisTemplate")
	private RedisOperations<String, String> stringRedis;
	
	@Autowired
	private UserBasicInfoMapper userBasicInfoMapper;
	
	@Autowired
	private ApplyUserDetailNewMapper applyUserDetailNewMapper;
	
	@Autowired
	private UserDetailNewMapper userDetailNewMapper;
	
	@Autowired
	private UserCreditStateMapper userCreditStateMapper;

	@Autowired
	private UserBasicInfoExtMapper userBasicInfoExtMapper;
	
	@Autowired
	private UserAccountMapper userAccountMapper;
	
	@Autowired
	private QQJUserAccountMapper qqjUserAccountMapper;
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public UserBasicInfo userSync(LoginRegisterDTO loginDTO, TuandaiResponse tuandaiResponse) {
		UserBasicInfo userBasicInfo = userBasicInfoMapper
				.getUserBasicInfoById(tuandaiResponse.getUserId());
		if (userBasicInfo == null) {
			// 注册你我金融账户
			userBasicInfo = new UserBasicInfo();
			userBasicInfo.setId(tuandaiResponse.getUserId());
			userBasicInfo.setUserName(tuandaiResponse.getUname());
			userBasicInfo.setPwd(TuandaiUtil.md5Passwrod(loginDTO.getPassword()));
			userBasicInfo
					.setNickName(getNickName(tuandaiResponse.getUname(), 0));
			userBasicInfo.setMobileNo(tuandaiResponse.getMobileNo());
			userBasicInfo.setEmail(tuandaiResponse.getEmail());
			userBasicInfo
					.setRealName(StringUtils.isEmpty(tuandaiResponse
							.getRealName()) ? Constant.EMPTY_STRING : tuandaiResponse
							.getRealName().trim());
			userBasicInfo.setIdentityCard(tuandaiResponse.getIdentityCard());
			userBasicInfo.setType(Integer.parseInt(tuandaiResponse
					.getUserTypeId()));
			userBasicInfo.setFromType(Constant.FROM_TUANDAI); // 团贷网用户
			userBasicInfo.setChannelId(loginDTO.getChannelId());
			userBasicInfo = this.addUser(userBasicInfo);
		} else {
			// 手机，邮箱，身份认证，登录密码做同步
			boolean isChange = false;
			// 同步身份信息
			if (!StringUtils.isEmpty(tuandaiResponse.getIdentityCard())
					&& !tuandaiResponse.getIdentityCard().equals(
							userBasicInfo.getIdentityCard())
					&& !StringUtils.isEmpty(tuandaiResponse.getSex())
					&& !StringUtils.isEmpty(tuandaiResponse.getUserTypeId())
					&& !StringUtils.isEmpty(tuandaiResponse.getRealName())) {
				userBasicInfo
						.setIdentityCard(tuandaiResponse.getIdentityCard());
				userBasicInfo.setCredTypeId(Constant.CRED_TYPE);
				if (!StringUtils.isEmpty(tuandaiResponse.getSex())) {
					userBasicInfo.setSex(Integer.parseInt(tuandaiResponse
							.getSex()));
				}
				Date birthday = DateUtils.parseDateFromString(IdcardUtils
						.getBirthByIdCard(tuandaiResponse.getIdentityCard()),
						DateUtils.DATE_FORMAT);
				userBasicInfo.setBirthday(birthday);
				userBasicInfo.setRealName(StringUtils.isEmpty(tuandaiResponse
						.getRealName()) ? Constant.EMPTY_STRING : tuandaiResponse.getRealName()
						.trim());
				isChange = true;
			}
			// 同步密码
			String pwd = TuandaiUtil.md5Passwrod(loginDTO.getPassword());
			if (!userBasicInfo.getPwd().equals(pwd)) {
				userBasicInfo.setPwd(pwd);
				isChange = true;
			}
			// 同步手机号码
			if (!StringUtils.isEmpty(tuandaiResponse.getMobileNo())
					&& !tuandaiResponse.getMobileNo().equals(
							userBasicInfo.getMobileNo())) {
				userBasicInfo.setMobileNo(tuandaiResponse.getMobileNo());
				isChange = true;
			}
			// 同步邮箱
			if (!StringUtils.isEmpty(tuandaiResponse.getEmail())
					&& !tuandaiResponse.getEmail().equals(
							userBasicInfo.getEmail())) {
				userBasicInfo.setEmail(tuandaiResponse.getEmail());
				isChange = true;
			}
			// 如果身份证不为空的话，更改身份认证状态
			if (!StringUtils.isEmpty(userBasicInfo.getIdentityCard())) {
				userBasicInfo.setIsValidateIdentity(1);
			}
			// 邮箱不为空的话，更改邮箱认证状态
			if (!StringUtils.isEmpty(userBasicInfo.getEmail())) {
				userBasicInfo.setIsValidateEmail(1);
			}
			// 手机不为空的话，更改手机认证状态
			if (!StringUtils.isEmpty(userBasicInfo.getMobileNo())) {
				userBasicInfo.setIsValidateMobile(1);
			}

			if (isChange) {
				userBasicInfoMapper.updateUserBasicInfo(userBasicInfo);
			}
		}
		return userBasicInfo;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public UserBasicInfo addUser(LoginRegisterDTO registerDTO) {
		UserBasicInfo userBasicInfo = new UserBasicInfo();
		userBasicInfo.setId(registerDTO.getUserId());
		userBasicInfo.setUserName(registerDTO.getUserName());
		userBasicInfo.setNickName(registerDTO.getNickName());
		userBasicInfo.setHeadImage(registerDTO.getHeadImage());
		
		userBasicInfo.setPwd(TuandaiUtil.md5Passwrod(registerDTO.getPassword()));
		
		userBasicInfo.setType(StringUtils.isEmpty(registerDTO.getUserTypeId())
				? Constant.PERSON_TYPE : Integer.parseInt(registerDTO.getUserTypeId()));
		userBasicInfo.setFromType(StringUtils.isEmpty(registerDTO.getFromType())
				? Constant.FROM_NIIWOO : Integer.parseInt(registerDTO.getFromType()));
		userBasicInfo.setRegisterFrom(registerDTO.getDevType());
		
		userBasicInfo.setMobileNo(registerDTO.getPhone());
		userBasicInfo.setEmail(registerDTO.getEmail());
		userBasicInfo.setChannelId(registerDTO.getChannelId());
		return addUser(userBasicInfo);
	}
	
	@Override
	public UserBasicInfo getUserBasicInfoById(String userId) {
		return userBasicInfoMapper.getUserBasicInfoById(userId);
	}
	
	private UserBasicInfo addUser(UserBasicInfo userBasicInfo) {
		if(userBasicInfo == null)
			throw new IllegalArgumentException("用户信息不能为空");
		// 插入用户基本信息表
		userBasicInfo
				.setUserName(StringUtils.isEmpty(userBasicInfo.getUserName()) ? TuandaiUtil
						.getRandUserName() : userBasicInfo.getUserName());
		userBasicInfo.setHeadImage(removeSiteUrl(userBasicInfo.getHeadImage()));
		userBasicInfo.setPayPwd(Constant.EMPTY_STRING);
		userBasicInfo.setRegisterFrom(StringUtils.isEmpty(userBasicInfo
				.getRegisterFrom()) ? Constant.DEVICE_PC : userBasicInfo.getRegisterFrom());
		userBasicInfo.setUserStatus(1);
		userBasicInfo
				.setIsValidateEmail(StringUtils.isEmpty(userBasicInfo.getEmail()) ? 0
						: 1);
		userBasicInfo.setIsValidateMobile(StringUtils.isEmpty(userBasicInfo
				.getMobileNo()) ? 0 : 1);
		userBasicInfo.setIsValidateIdentity(!StringUtils.isEmpty(userBasicInfo
				.getIdentityCard())
				&& (userBasicInfo.getIdentityCard().length() == 15 || userBasicInfo
						.getIdentityCard().length() == 18) ? 1 : 0);

		if (userBasicInfo.getType() == 1
				&& !StringUtils.isEmpty(userBasicInfo.getIdentityCard())) {
			int sex = 1;
			try {
				sex = "F".equals(IdcardUtils.getGenderByIdCard(userBasicInfo
						.getIdentityCard())) ? 2 : 1;
				Date birthday = DateUtils.parseDateFromString(
						IdcardUtils.getBirthByIdCard(userBasicInfo.getIdentityCard()),
						DateUtils.DATE_FORMAT);
				userBasicInfo.setBirthday(birthday);
			} catch (Exception e) {
				logger.error("身份证格式错误:" + userBasicInfo.getIdentityCard(), e);
			}
			userBasicInfo.setSex(sex);
			userBasicInfo
					.setRealName(StringUtils.isEmpty(userBasicInfo.getRealName()) ? Constant.EMPTY_STRING
							: userBasicInfo.getRealName().trim());
		}
		userBasicInfoMapper.addUserBasicInfo(userBasicInfo);

		initUserDetailNew(userBasicInfo.getUserId());
		initUserCreditState(userBasicInfo.getUserId());
		initUserBasicInfoExt(userBasicInfo.getUserId());
		initUserAccount(userBasicInfo.getUserId());
		initQQJUserAccount(userBasicInfo.getUserId());
		return userBasicInfo;
	}
	
	private UserDetailNew initUserDetailNew(String userId){
		//初始化用户详细信息表
		UserDetailNew userDetail = applyUserDetailNewMapper.selectByPrimaryKey(userId);
		if(userDetail == null){
			userDetail = new UserDetailNew();
			userDetail.setGuarantorApproveStatusId((byte) -2);
			userDetail.setBorrowerApproveStatusId((byte) -2);
			userDetail.setIsBorrower((byte) 0);
			userDetail.setIsGuarantor((byte) 0);
			userDetail.setUserId(userId);
			userDetail.setCreateTime(new Date());
			applyUserDetailNewMapper.insertSelective(userDetail);
			userDetailNewMapper.insertSelective(userDetail);
		}
		return userDetail;
	}
	
	private UserCreditState initUserCreditState(String userId){
		//初始化用户认证表
		UserCreditState userCreditState = userCreditStateMapper.selectByPrimaryKey(userId);
		if(userCreditState == null){
			userCreditState = new UserCreditState();
			userCreditState.setUserId(userId);
			userCreditState.setCreateTime(new Date());
			userCreditState.setBorrowedLimit(BigDecimal.ZERO);
			userCreditState.setGuarantorLimit(BigDecimal.ZERO);
			userCreditStateMapper.insertSelective(userCreditState);
		}
		return userCreditState;
	}
	
	private UserBasicInfoExt initUserBasicInfoExt(String userId){
		//初始化用户基本信息扩展表
		UserBasicInfoExt ext = userBasicInfoExtMapper.selectByPrimaryKey(userId);
		if(ext == null){
			ext = new UserBasicInfoExt();
			ext.setUserId(userId);
			ext.setCreateTime(new Date());
			ext.setUpdateTime(new Date());
			userBasicInfoExtMapper.insertSelective(ext);
		}
		return ext;
	}
	
	private UserAccount initUserAccount(String userId){
		// 初始化用户账户表
		UserAccount userAccount = userAccountMapper.selectByUserId(userId);
		if (userAccount == null) {
			userAccount = new UserAccount();
			userAccount.setUserid(userId);
			userAccountMapper.insertSelective(userAccount);
		}
		return userAccount;
	}
	
	private QQJUserAccount initQQJUserAccount(String userId){
		// 初始化悄悄借用户账户表
		QQJUserAccount qqjUserAccount = qqjUserAccountMapper.selectByPrimaryKey(userId);
		if (qqjUserAccount == null) {
			qqjUserAccount = new QQJUserAccount();
			qqjUserAccount.setUserId(userId);
			qqjUserAccount.setCreateTime(new Date());
			qqjUserAccount.setUpdateTime(new Date());
			qqjUserAccountMapper.insertSelective(qqjUserAccount);
		}
		return qqjUserAccount;
	}
	
    private String getNickName(String nickName, int num){
    	if(StringUtils.isEmpty(nickName)) return createNickName();
    	if(userBasicInfoMapper.checkNickName(nickName) == 0){
    		return nickName;
    	}else if(num >= 3){
    		return TuandaiUtil.getNickNameRand();
    	}else{
    		nickName = nickName.endsWith("_" + num) ? nickName.substring(0, nickName.length() - 2) : nickName;
    		num ++;
    		return getNickName(nickName+"_"+num, num);
    	}
    }
    
    private String createNickName() {
        int currentNumber = 0;
    	try{
        	SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
            String redisKey = String.format("%s_%s", Constant.QUANTGROUP_ADD_USER_NUMBER_TODAY, 
            		sdf.format(new Date()));
            //新注册用户昵称自动设为 niiwoo1603250001,数字参照项目标题生成规则
            String  redisUserNumber = stringRedis.opsForValue().get(redisKey);
            if (StringUtils.isEmpty(redisUserNumber)) {
                Integer number = userBasicInfoMapper.queryTodayAddUserNumber();
                if (null == number)
                    number = 0;
                stringRedis.opsForValue().set(redisKey, SysStringUtils.toString(number));
            }
            stringRedis.opsForValue().increment(redisKey, 1);
            redisUserNumber = stringRedis.opsForValue().get(redisKey);
            if (StringUtils.isEmpty(redisUserNumber)) {
                redisUserNumber = "1000";
            }
            currentNumber = NumberUtils.toInt(redisUserNumber);
        }catch(Exception e){
        	logger.error("获取当前用户号码错误", e);
        	currentNumber = new Random().nextInt(1000) + 1;
        }
    	return "niiwoo".concat(new SimpleDateFormat("yyMMdd").format(new Date())).concat(currentNumber + "");
    }
    
	private String removeSiteUrl(String headImage){
		//去除已http开头的根地址
		return !StringUtils.isEmpty(headImage)
				&& headImage.toLowerCase().startsWith("http://") 
				? headImage.replace(UploadConfig.getSiteUrl(), "") : headImage;
	}

}
