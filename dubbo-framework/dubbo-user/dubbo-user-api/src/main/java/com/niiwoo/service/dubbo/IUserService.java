/**   
* @Title: IUserService.java
* @Package com.niiwoo.dubbo.service
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-7-19 上午11:20:33
* @version V1.0   
*/


package com.niiwoo.service.dubbo;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.niiwoo.dto.LoginRegisterDTO;
import com.niiwoo.dto.ResponseDTO;
import com.niiwoo.dto.User;

/**
 * @ClassName: IUserService
 * @Description: 用户服务
 * @author seven
 * @date 2016-7-19 上午11:20:33
 * 
 */
@Path("user")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface IUserService {
    /**
     * 用户登录
     * @param loginDTO
     */
    @POST
    @Path("login")
	public ResponseDTO<User> userLogin(LoginRegisterDTO loginDTO);
	
    /**
     * 用户注册
     * @param registerDTO
     */
    @POST
    @Path("register")
	public ResponseDTO<User> registerUser(LoginRegisterDTO registerDTO);
    
    /**
     * 获取用户信息
     * @param userId
     */
    @GET
    @Path("get/{userId}")
    public ResponseDTO<User> getUser(@PathParam("userId") String userId);
}
