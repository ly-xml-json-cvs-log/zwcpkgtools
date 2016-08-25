/**   
* @Title: RtnEnum.java
* @Package com.niiwoo.enums
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-27 下午3:56:22
* @version V1.0   
*/


package com.niiwoo.enums;

/**
 * @ClassName: RtnEnum
 * @Description: 返回枚举
 * @author seven
 * @date 2016-7-27 下午3:56:22
 * 
 */

public enum RtnEnum {
	SUCCESS(0, "成功"), EXCEPTION(100, "异常"), FAILURE(101, "错误"), NOTEXIST(102, "用户不存在");
	private int rtn;
	
	private String msg;
	
	private RtnEnum(int rtn, String msg){
		this.rtn = rtn;
		this.msg = msg;
	}

	public int getRtn() {
		return rtn;
	}

	public String getMsg() {
		return msg;
	}
}
