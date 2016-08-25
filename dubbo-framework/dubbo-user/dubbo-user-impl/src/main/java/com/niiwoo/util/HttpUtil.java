package com.niiwoo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @ClassName: HTTPUtil
* @Description: http链接工具类
* @author seven
* @date 2014-11-7 下午1:41:42
 */
public class HttpUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	private static HttpClient httpClient=null;
	
	public static HttpClient getHttpClient(){
		if(httpClient==null){
			MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
			HttpConnectionManagerParams params = connectionManager.getParams();
			params.setDefaultMaxConnectionsPerHost(100);
			params.setMaxTotalConnections(300);
			params.setConnectionTimeout(30000);
			params.setSoTimeout(30000);
			HttpClientParams clientParams = new HttpClientParams();
			// 忽略cookie 避免 Cookie rejected 警告
			clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
			httpClient = new HttpClient(clientParams, connectionManager);
		}
		return httpClient;
	}
	public static String sendPostHttp(String url,Map<String, String> param){
		
		//HttpClient	httpclient =getHttpClient();
		PostMethod method=new PostMethod(url);
		String responseBody="";
        try {
        	List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
         	for(Entry<String, String> entry:param.entrySet()){
         		nameValuePairs.add(new NameValuePair(entry.getKey(),entry.getValue()));
        	}
         	method.setQueryString(nameValuePairs.toArray(new NameValuePair[]{}));
//        	method.setQueryString(new NameValuePair[] { new NameValuePair("token", token), new NameValuePair("jsonString", str)});
			int state = getHttpClient().executeMethod(method);
			if (state == HttpStatus.SC_OK) {
				  responseBody = method.getResponseBodyAsString();
		//		LOG.debug(responseBody);
				// 非json格式，要物事处理
				if (responseBody.startsWith("\"")) {
					responseBody = responseBody.substring(1);
				}
				if (responseBody.endsWith("\"")) {
					responseBody = responseBody.substring(0, responseBody.length() - 1);
				}
				responseBody = responseBody.replaceAll("\\\\", "");
			    
			} else {
			//	LOG.error("链接返回状态不对。State=" + state);
				throw new Exception("链接返回状态不对。State=" + state);
			}
			logger.debug("responseBody:" + responseBody);
		} catch (Exception e) {
			e.printStackTrace();
				//throw new Exception("内部错误:" + e.getMessage());
		} finally {
			method.releaseConnection();
		}
        
		return responseBody;
	}
	
	public static String sendPostHttp2(String url,Map<String, String> param){
		
		//HttpClient	httpclient =getHttpClient();
		PostMethod method=new PostMethod(url);
		String responseBody="";
        try {
        	List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
         	for(Entry<String, String> entry:param.entrySet()){
         		nameValuePairs.add(new NameValuePair(entry.getKey(),entry.getValue()));
        	}
         	method.setQueryString(nameValuePairs.toArray(new NameValuePair[]{}));
//        	method.setQueryString(new NameValuePair[] { new NameValuePair("token", token), new NameValuePair("jsonString", str)});
			int state = getHttpClient().executeMethod(method);
			if (state == HttpStatus.SC_OK) {
				  responseBody = method.getResponseBodyAsString();
		//		LOG.debug(responseBody);
				// 非json格式，要物事处理
				if (responseBody.startsWith("\"")) {
					responseBody = responseBody.substring(1);
				}
				if (responseBody.endsWith("\"")) {
					responseBody = responseBody.substring(0, responseBody.length() - 1);
				}
				responseBody = responseBody.replaceAll("\\\\", "");
			    
			} else {
			//	LOG.error("链接返回状态不对。State=" + state);
				throw new Exception("链接返回状态不对。State=" + state);
			}
			logger.debug("responseBody:" + responseBody);
		} catch (Exception e) {
			throw new RuntimeException("请求错误:" , e);
		} finally {
			method.releaseConnection();
		}
        
		return responseBody;
	}
	
	public static String sendGetHttp(String url,Map<String, String> param){
		GetMethod method=new GetMethod(url);
		String responseBody="";
        try {
        	List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
         	for(Entry<String, String> entry:param.entrySet()){
         		nameValuePairs.add(new NameValuePair(entry.getKey(),entry.getValue()));
        	}
         	method.setQueryString(nameValuePairs.toArray(new NameValuePair[]{}));
//        	method.setQueryString(new NameValuePair[] { new NameValuePair("token", token), new NameValuePair("jsonString", str)});
			int state = getHttpClient().executeMethod(method);
			if (state == HttpStatus.SC_OK) {
				  responseBody = method.getResponseBodyAsString();
		//		LOG.debug(responseBody);
				// 非json格式，要物事处理
				if (responseBody.startsWith("\"")) {
					responseBody = responseBody.substring(1);
				}
				if (responseBody.endsWith("\"")) {
					responseBody = responseBody.substring(0, responseBody.length() - 1);
				}
				responseBody = responseBody.replaceAll("\\\\", "");
			    
			} else {
			//	LOG.error("链接返回状态不对。State=" + state);
				throw new Exception("链接返回状态不对。State=" + state);
			}
			logger.debug("responseBody:" + responseBody);
		} catch (Exception e) {
			e.printStackTrace();
				//throw new Exception("内部错误:" + e.getMessage());
		} finally {
			method.releaseConnection();
		}
        
		return responseBody;
	}
	
	public static String getMapValue(Map<String, String> param){
		StringBuffer str=new StringBuffer("param:[");
		for(Entry<String, String> e:param.entrySet()){
			str.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		str.append("]");
		return str.toString();
	}
	
	public static void main(String[] args) {
	}
}
