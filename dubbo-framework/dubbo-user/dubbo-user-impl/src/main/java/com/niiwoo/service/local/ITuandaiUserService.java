/**   
* @Title: ITuandaiUserService.java
* @Package com.niiwoo.external.service.impl
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-25 下午6:10:40
* @version V1.0   
*/


package com.niiwoo.service.local;

import com.niiwoo.dto.TuandaiRequest;
import com.niiwoo.dto.TuandaiResponse;

/**
 * @ClassName: ITuandaiUserService
 * @Description: 团贷网用户服务
 * @author seven
 * @date 2016-7-25 下午6:10:40
 * 
 */

public interface ITuandaiUserService {
	/**
	* @Title: post 
	* @Description: 请求团贷接口
	* @param request
	* @return TuandaiResponse    返回类型  
	 */
	public TuandaiResponse post(TuandaiRequest request);
	
}
