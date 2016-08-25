/**   
* @Title: IUserStorageService.java
* @Package com.niiwoo.service.local
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-28 上午9:57:37
* @version V1.0   
*/


package com.niiwoo.service.local;

import com.niiwoo.dao.model.userbasic.UserBasicInfo;
import com.niiwoo.dto.LoginRegisterDTO;
import com.niiwoo.dto.TuandaiResponse;

/**
 * @ClassName: IUserStorageService
 * @Description: 用户存储服务实现
 * @author seven
 * @date 2016-7-28 上午9:57:37
 * 
 */

public interface IUserStorageService {
	/**
	* @Title: userSync 
	* @Description: 登录成功后同步团贷网用户信息
	* @param loginDTO
	* @param tuandaiResponse
	* @return UserBasicInfo    返回类型  
	 */
	public UserBasicInfo userSync(LoginRegisterDTO loginDTO, TuandaiResponse tuandaiResponse);
	
	/**
	* @Title: addUser 
	* @Description: 新增用户
	* @param registerDTO
	* @return UserBasicInfo    返回类型  
	 */
	public UserBasicInfo addUser(LoginRegisterDTO registerDTO);
	
	/**
	* @Title: getUserBasicInfoById 
	* @Description: 获取用户信息
	* @param userId
	* @return UserBasicInfo    返回类型  
	 */
	public UserBasicInfo getUserBasicInfoById(String userId);
}
