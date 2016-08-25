/**   
* @Title: SessionServiceImpl.java
* @Package com.niiwoo.dubbo.session.impl
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-11 上午11:34:08
* @version V1.0   
*/


package com.niiwoo.service.dubbo.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;

import com.alibaba.dubbo.config.annotation.Service;
import com.niiwoo.dto.SessionIdDTO;
import com.niiwoo.dto.UnifiedSession;
import com.niiwoo.service.dubbo.ISessionService;

/**
 * @ClassName: SessionServiceImpl
 * @Description: session管理
 * @author seven
 * @date 2016-7-11 上午11:34:08
 * 
 */
@Service(protocol = {"dubbo","rest"})
public class SessionServiceImpl implements ISessionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String SESSION_PREFIX = "niiwoo-app.session_"; 
	
	private static final String USER_DEV_PREFIX = "niiwoo-app.user_dev_sessionId_"; 
	
	private static final List<String> APP_LIST = Arrays.asList(new String[]{"Android", "IOS"});
	
	@Autowired
	private RedisOperations<String, String> stringRedisTemplate;
	
	@Autowired
	private RedisOperations<String, Object> objRedisTemplate;
	
	@Override
	public void addSession(UnifiedSession session) {
    	try{
        	if(session == null)
        		return;
        	logger.info("dubbo session service add session:{}", session);
    		String sessionKey = SESSION_PREFIX + session.getSessionId();
    		objRedisTemplate.opsForValue().set(sessionKey, session);
    		String userDevKey = getUserSessionIdKey(session.getDevType(), session.getUserId());
    		stringRedisTemplate.opsForValue().set(userDevKey, session.getSessionId());
    	}catch(Exception e){
    		logger.error("添加session错误: ", e);
    	}
	}

	@Override
	public UnifiedSession getSession(String sessionId) {
		try{
			logger.info("dubbo session service get session, id:{}", sessionId);
			String key = SESSION_PREFIX + sessionId;
    		if(objRedisTemplate.hasKey(key)){
    			return (UnifiedSession) objRedisTemplate.opsForValue().get(key);
    		}
    	}catch(Exception e){
    		logger.error("获取session错误: ", e);
    	}
		return null;
	}
	
	@Override
	public SessionIdDTO getSession(String devType, String userId) {
		try{
			logger.info("dubbo session service get session, userId:{},devType:{}", userId, devType);
			String key = getUserSessionIdKey(devType, userId);
    		if(stringRedisTemplate.hasKey(key)){
    			String sessionId = stringRedisTemplate.opsForValue().get(key);
    			return new SessionIdDTO(sessionId);
    		}
    	}catch(Exception e){
    		logger.error("获取用户已存在的sessionId错误: ", e);
    	}
    	return null;
	}
	
	@Override
	public void removeSession(String sessionId) {
		try{
			logger.info("dubbo session service remove session, id:{}", sessionId);
			UnifiedSession session = getSession(sessionId);
			if(session == null)
				return;
			String sessionKey = SESSION_PREFIX + sessionId;
        	if(objRedisTemplate.hasKey(sessionKey)){
        		objRedisTemplate.delete(sessionKey);
        	}
        	String userDevKey = getUserSessionIdKey(session.getDevType(), session.getUserId());
        	if(stringRedisTemplate.hasKey(userDevKey)){
        		stringRedisTemplate.delete(userDevKey);
        	}
    	}catch(Exception e){
    		logger.error("从缓存中删除session错误: ", e);
    	}
	}
	
	private String getUserSessionIdKey(String deviceType, String userId){
		deviceType = APP_LIST.contains(deviceType) ? "APP" : deviceType; 
		return  USER_DEV_PREFIX + deviceType + "_" + userId;
	}

}
