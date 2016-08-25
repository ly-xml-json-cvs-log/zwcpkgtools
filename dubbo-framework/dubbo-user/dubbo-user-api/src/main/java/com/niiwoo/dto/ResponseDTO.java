/**   
* @Title: ResponseDTO.java
* @Package com.niiwoo.dto
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-27 下午4:18:19
* @version V1.0   
*/


package com.niiwoo.dto;

import java.io.Serializable;

import com.niiwoo.enums.RtnEnum;

/**
 * @ClassName: ResponseDTO
 * @Description: 返回结果
 * @author seven
 * @date 2016-7-27 下午4:18:19
 * 
 */

public class ResponseDTO<T> implements Serializable{
	private static final long serialVersionUID = 1L;

	private RtnEnum rtnEnum;
	
	private String msg = "ok";
	
	private T t;
	
	public RtnEnum getRtnEnum() {
		return rtnEnum;
	}

	public void setRtnEnum(RtnEnum rtnEnum) {
		this.rtnEnum = rtnEnum;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}
}
