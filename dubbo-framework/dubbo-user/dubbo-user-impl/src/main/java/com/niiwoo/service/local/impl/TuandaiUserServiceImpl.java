/**   
* @Title: TuandaiUserServiceImpl.java
* @Package com.niiwoo.external.service.impl
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-25 下午6:19:19
* @version V1.0   
*/


package com.niiwoo.service.local.impl;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.niiwoo.dto.TuandaiRequest;
import com.niiwoo.dto.TuandaiResponse;
import com.niiwoo.service.local.ITuandaiUserService;
import com.niiwoo.util.HttpUtil;

/**
 * @ClassName: TuandaiUserServiceImpl
 * @Description: 团贷网用户服务实现
 * @author seven
 * @date 2016-7-25 下午6:19:19
 * 
 */
@Service("tuandaiUserService")
public class TuandaiUserServiceImpl implements ITuandaiUserService{
	private static final Logger logger = LoggerFactory.getLogger(TuandaiUserServiceImpl.class);
	//最大执行时间
	private static final int executeTime = 5000;
	
	
	@Override
	public TuandaiResponse post(TuandaiRequest request) {
		final String requestUrl = request.getRequestUrl();
        final Map<String, String> param = request.getParam();
        String responseStr = null;
        if(!request.isAsyn()){
        	responseStr = post(requestUrl, param);
        }else{
        	 //异步调用东莞登录接口，如果超过5秒没有返回，直接从本地库中查询用户数据
            ExecutorService executor = Executors.newSingleThreadExecutor();   
    	    FutureTask<String> futureTask =   
    	           new FutureTask<String>(new Callable<String>() {
    	             @Override
    				public String call() {   
    	            	 return post(requestUrl, param);
    	           }});   
    	    executor.execute(futureTask);   
    	    try {   
    	    	//取得结果，同时设置超时执行时间为5秒。
    	    	responseStr = futureTask.get(executeTime, TimeUnit.MILLISECONDS); 
    	    } catch (InterruptedException e) {   
    	        futureTask.cancel(true);   
    	    } catch (ExecutionException e) {   
    	        futureTask.cancel(true);   
    	    } catch (TimeoutException e) {   
    	        futureTask.cancel(true); 
    	        logger.error("call tuandai api timeout,url:{},param:{}", requestUrl, JSONObject.toJSONString(param));
    	    } catch (Exception e) {
    	    	 futureTask.cancel(true);
    	    	 logger.error("call tuandai api error,url:{},param:{}", requestUrl, JSONObject.toJSONString(param));
    	    } finally {   
    	        executor.shutdown();   
    	    } 
        }
        TuandaiResponse tuandaiResponse = StringUtils.isEmpty(responseStr)
        		? null : JSONObject.parseObject(responseStr, TuandaiResponse.class);
	    return tuandaiResponse;
	}

	private String post(String requestUrl, Map<String, String> param){
		logger.debug("call tuandai api start,url:{},param:{}", requestUrl, JSONObject.toJSONString(param));
		String response =  HttpUtil.sendPostHttp(requestUrl, param);
		logger.debug("call tuandai api end,url:{},param:{}", requestUrl, JSONObject.toJSONString(param));
		return response;
	}
}
